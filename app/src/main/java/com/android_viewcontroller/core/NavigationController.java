package com.android_viewcontroller.core;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NavigationController extends ViewController {

    private int transitioningStateFlags = 0;
    private NavigationControllerDelegate delegate;
    private AnimatedTransitioning popGestureTransitioning;

    public NavigationController(Context context, ViewController root) {
        super(context);
        initialize(root);
    }

    private void initialize(ViewController root) {
        if (root != null) {
            children.add(root);
        }
    }

    public void push(ViewController controller) {
        if (!checkPush(controller)) return;
        controller.parent = this;
        controller.contentView = controller._notifyLoadView();
        controller.contentView.setVisibility(View.INVISIBLE);
        containerView.addView(controller.contentView);
        children.add(controller);
        controller.contentView.post(() -> {
            controller._notifyViewDidLoad();
            showController(TransitioningContext.Operation.push, visible, controller);
        });
    }

    public ViewController pop() {
        if (children.size() <= 1) return null;
        List<ViewController> controllers = pop(children.get(children.size() - 2));
        if (controllers != null && controllers.size() > 0) {
            return controllers.get(0);
        }
        return null;
    }

    public List<ViewController> popToRoot() {
        if (children.size() == 0) return null;
        return pop(children.get(0));
    }

    public List<ViewController> pop(ViewController to) {
        if (!checkPop(to)) return null;
        List controllers = new ArrayList();
        for (int i = children.size() - 1; i >= 0; i--) {
            ViewController controller = children.get(i);
            if (controller == to) {
                break;
            } else {
                controllers.add(controller);
                if (controller != visible) {
                    controller.parent = null;
                    containerView.removeView(controller.contentView);
                    children.remove(controller);
                }
            }
        }
        showController(TransitioningContext.Operation.pop, visible, to);
        return controllers;
    }

    public void setDelegate(NavigationControllerDelegate delegate) {
        this.delegate = delegate;
    }

    public NavigationControllerDelegate getDelegate() {
        return delegate;
    }

    private void checkContainerView() {
        if (containerView == null) {
            throw new RuntimeException("Please call the makeContainerView method first");
        }
    }

    private boolean checkPush(ViewController controller) {
        checkContainerView();
        if (controller == null || controller instanceof NavigationController || children.contains(controller)) {
            return false;
        }
        return true;
    }

    private boolean checkPop(ViewController controller) {
        if (controller == null || controller == visible || children.size() <= 1 || !children.contains(controller)) {
            return false;
        }
        return true;
    }

    private int setTransitioningState(int mask, boolean t) {
        if (t) {
            transitioningStateFlags |= (1 << mask);
        } else {
            transitioningStateFlags &= ~(1 << mask);
        }
        return transitioningStateFlags;
    }

    private boolean getTransitioningState(int mask) {
        return 0 != (transitioningStateFlags & (1 << mask));
    }

    private void dispatchTransitionState(TransitioningContext.TransitionState state, TransitioningContext.Operation operation, ViewController from, ViewController to) {
        switch (state) {
            case fromViewWillDisappear:
            case toViewWillDisappear:
                if (!getTransitioningState(1)) {
                    setTransitioningState(1, true);
                    from._notifyViewWillDisappear();
                }
                break;
            case toViewWillAppear:
            case fromViewWillAppear:
                if (!getTransitioningState(2)) {
                    setTransitioningState(2, true);
                    to._notifyViewWillAppear();
                    to.contentView.setVisibility(View.VISIBLE);
                }
                break;
            case fromViewDidDisappear:
            case toViewDidDisappear:
                if (!getTransitioningState(3)) {
                    setTransitioningState(3, true);
                    from._notifyViewDidDisappear();
                    if (operation == TransitioningContext.Operation.push) {
                        from.contentView.setVisibility(View.INVISIBLE);
                    } else if (operation == TransitioningContext.Operation.pop) {
                        containerView.removeView(from.contentView);
                        from.parent = null;
                        children.remove(from);
                    }
                }
                break;
            case toViewDidAppear:
            case fromViewDidAppear:
                if (!getTransitioningState(4)) {
                    setTransitioningState(4, true);
                    to._notifyViewDidAppear();
                    visible = to;
                }
                break;
            case transitionCancel:
                popGestureTransitioning = null;
                to.contentView.setVisibility(View.INVISIBLE);
                to._notifyViewWillDisappear();
                to._notifyViewDidDisappear();
                from._notifyViewWillAppear();
                from._notifyViewDidAppear();
                resetTransitioningState();
                break;
            case transitionFinish:
                popGestureTransitioning = null;
                resetTransitioningState();
                break;
        }
    }

    private void resetTransitioningState() {
        setTransitioningState(1, false);
        setTransitioningState(2, false);
        setTransitioningState(3, false);
        setTransitioningState(4, false);
    }

    @Override
    public ContainerView makeContainerView() {
        ContainerView containerView = super.makeContainerView();
        containerView.setTouchEventListener(event -> {
            dispatchContainerViewTouchEvent(event);
        });
        return containerView;
    }

    private void dispatchContainerViewTouchEvent(MotionEvent event) {
        if (children != null && children.size() > 1) {
            ViewController to = children.get(children.size() - 2);
            TransitioningContext context = new TransitioningContext(event, visible, visible.contentView, to, to.contentView, containerView);
            if (popGestureTransitioning != null) {
                context.setTransitionStateChangeListener((state) -> {
                    dispatchTransitionState(state, TransitioningContext.Operation.pop, visible, to);
                });
                popGestureTransitioning.animateTransition(context);
            } else {
                if (delegate != null) {
                    if (delegate.shouldPopGestureTransition(this, visible, to)) {
                        popGestureTransitioning = delegate.popGestureTransitioning(this, visible, to);
                    }
                }
                if (popGestureTransitioning == null) {
                    DefaultNavigationControllerDelegate delegate = new DefaultNavigationControllerDelegate();
                    if (delegate.shouldPopGestureTransition(this, visible, to)) {
                        popGestureTransitioning = delegate.popGestureTransitioning(this, visible, to);
                    }
                }
                if (popGestureTransitioning != null) {
                    context.setTransitionStateChangeListener((state) -> {
                        dispatchTransitionState(state, TransitioningContext.Operation.pop, visible, to);
                    });
                    popGestureTransitioning.animateTransition(context);
                }
            }
        }
    }

    private void showController(TransitioningContext.Operation operation, ViewController from, ViewController to) {
        TransitioningContext context = new TransitioningContext(operation, from, from.contentView, to, to.contentView, containerView);
        AnimatedTransitioning transition = null;
        if (delegate != null) {
            transition = delegate.transitioning(this, operation, from, to);
        }
        if (transition == null) {
            DefaultNavigationControllerDelegate delegate = new DefaultNavigationControllerDelegate();
            transition = delegate.transitioning(this, operation, from, to);
        }
        if (transition != null) {
            context.setTransitionStateChangeListener((state) -> {
                dispatchTransitionState(state, operation, from, to);
            });
            transition.animateTransition(context);
        }
    }

    private class DefaultNavigationControllerDelegate implements NavigationControllerDelegate {

        @Nullable
        @Override
        public AnimatedTransitioning transitioning(NavigationController navigationController, TransitioningContext.Operation operation, ViewController from, ViewController to) {
            return new DefaultAnimateTransition();
        }

        @Override
        public boolean shouldPopGestureTransition(NavigationController navigationController, ViewController from, ViewController to) {
            return true;
        }

        @Nullable
        @Override
        public AnimatedTransitioning popGestureTransitioning(NavigationController navigationController, ViewController from, ViewController to) {
            return new DefaultPopGestureTransition();
        }

        class DefaultAnimateTransition implements AnimatedTransitioning {

            final String propertyName = "translationX";

            @Override
            public void animateTransition(TransitioningContext context) {
                if (context.getOperation() == TransitioningContext.Operation.push) {
                    push(context);
                } else if (context.getOperation() == TransitioningContext.Operation.pop) {
                    pop(context);
                }
            }

            private void push(TransitioningContext context) {
                View fromView = context.getContentView(TransitioningContext.ViewKey.from);
                View toView = context.getContentView(TransitioningContext.ViewKey.to);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(context.getDefaultTransitionDuration());
                if (fromView != null) {
                    float offsetX = context.getContainerView().getWidth() / 4;
                    ObjectAnimator animator = ObjectAnimator.ofFloat(fromView, propertyName, 0f, -offsetX);
                    animatorSet.playTogether(animator);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            context.updateTransitionState(TransitioningContext.TransitionState.fromViewWillDisappear);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            context.updateTransitionState(TransitioningContext.TransitionState.fromViewDidDisappear);
                        }
                    });
                }
                if (toView != null) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(toView, propertyName, toView.getWidth(), 0);
                    animatorSet.playTogether(animator);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            context.updateTransitionState(TransitioningContext.TransitionState.toViewWillAppear);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            context.updateTransitionState(TransitioningContext.TransitionState.toViewDidAppear);
                            context.updateTransitionState(TransitioningContext.TransitionState.transitionFinish);
                        }
                    });
                }
                animatorSet.start();
            }

            private void pop(TransitioningContext context) {
                View fromView = context.getContentView(TransitioningContext.ViewKey.from);
                View toView = context.getContentView(TransitioningContext.ViewKey.to);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(context.getDefaultTransitionDuration());
                if (fromView != null) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(fromView, propertyName, 0, fromView.getWidth());
                    animatorSet.playTogether(animator);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            context.updateTransitionState(TransitioningContext.TransitionState.fromViewWillDisappear);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            context.updateTransitionState(TransitioningContext.TransitionState.fromViewDidDisappear);
                        }
                    });
                }
                if (toView != null) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(toView, propertyName, (int) toView.getX(), 0);
                    animatorSet.playTogether(animator);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            context.updateTransitionState(TransitioningContext.TransitionState.toViewWillAppear);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            context.updateTransitionState(TransitioningContext.TransitionState.toViewDidAppear);
                            context.updateTransitionState(TransitioningContext.TransitionState.transitionFinish);
                        }
                    });
                }
                animatorSet.start();
            }
        }

        class DefaultPopGestureTransition implements AnimatedTransitioning {

            private float downX;
            private float disX;
            private VelocityTracker velocityTracker;
            private final float down_max_x = 100;
            private final int velocity = 1500;

            @Override
            public void animateTransition(TransitioningContext context) {
                View fromView = context.getContentView(TransitioningContext.ViewKey.from);
                View toView = context.getContentView(TransitioningContext.ViewKey.to);
                MotionEvent event = context.getEvent();
                if (fromView != null && toView != null && event != null) {
                    final float initialX = context.getContainerView().getWidth() / 4;
                    int index = event.getActionIndex();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            dispatchActionDown(event);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (event.getPointerId(index) == 0) {
                                dispatchActionMove(event, fromView, toView, context, initialX);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_POINTER_UP:
                            dispatchActionUpAndCancel(event, fromView, toView, context, initialX);
                            break;
                    }
                }
            }



            private void dispatchActionDown(MotionEvent event) {
                velocityTracker = VelocityTracker.obtain();
                downX = event.getX();
            }

            private void dispatchActionMove(MotionEvent event, View fromView, View toView, TransitioningContext context, float initialX) {
                if (downX < down_max_x && velocityTracker != null) {
                    velocityTracker.addMovement(event);
                    velocityTracker.computeCurrentVelocity(velocity);
                    disX = event.getX() - downX;
                    if (disX >= 0) {
                        context.updateTransitionState(TransitioningContext.TransitionState.fromViewWillDisappear);
                        fromView.setX(disX);
                        context.updateTransitionState(TransitioningContext.TransitionState.toViewWillAppear);
                        toView.setX(-initialX + initialX * (disX / toView.getWidth()));
                    } else {
                        disX = 0;
                    }
                }
            }

            private void dispatchActionUpAndCancel(MotionEvent event, View fromView, View toView, TransitioningContext context, float initialX) {
                if (velocityTracker != null) {
                    if (downX != event.getX() && downX < down_max_x) {
                        String propertyName = "translationX";
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.setDuration(context.getDefaultTransitionDuration() / 2);
                        if (disX > context.getContainerView().getWidth() / 2 || velocityTracker.getXVelocity() > velocity) {
                            ObjectAnimator fromViewAnimator = ObjectAnimator.ofFloat(fromView, propertyName, disX, fromView.getWidth());
                            fromViewAnimator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    context.updateTransitionState(TransitioningContext.TransitionState.fromViewDidDisappear);
                                }
                            });
                            ObjectAnimator toViewAnimator = ObjectAnimator.ofFloat(toView, propertyName, toView.getX(), 0);
                            toViewAnimator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    context.updateTransitionState(TransitioningContext.TransitionState.toViewDidAppear);
                                    context.updateTransitionState(TransitioningContext.TransitionState.transitionFinish);
                                }
                            });
                            animatorSet.playTogether(fromViewAnimator);
                            animatorSet.playTogether(toViewAnimator);
                        } else {
                            ObjectAnimator fromViewAnimator = ObjectAnimator.ofFloat(fromView, propertyName, disX, 0);
                            ObjectAnimator toViewAnimator = ObjectAnimator.ofFloat(toView, propertyName, toView.getX(), -initialX);
                            toViewAnimator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    context.updateTransitionState(TransitioningContext.TransitionState.transitionCancel);
                                }
                            });
                            animatorSet.playTogether(fromViewAnimator);
                            animatorSet.playTogether(toViewAnimator);
                        }
                        animatorSet.start();
                    }
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
            }
        }
    }
}
