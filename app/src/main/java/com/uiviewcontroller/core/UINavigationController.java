package com.uiviewcontroller.core;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;

import com.uiviewcontroller.core.protocol.UIViewControllerAnimatedTransitioning;
import com.uiviewcontroller.core.protocol.UIViewControllerContextTransitioning;

import java.util.ArrayList;
import java.util.List;

public class UINavigationController extends UIViewController {

    public enum Operation {
        PUSH,
        POP
    }

    private UIViewController visibleViewController;

    public UINavigationController(Context context, UIViewController rootViewController) {
        super(context);
        this.visibleViewController = rootViewController;
        addChildViewController(rootViewController);
    }

    public UIView createView() {
        UIView view = getView();
        view.post(() -> {
            beginAppearanceTransition(true);
            if (visibleViewController != null) {
                getView().addView(visibleViewController.createView());
                visibleViewController.getView().post(() -> {
                    endAppearanceTransition();
                });
            }
        });
        return view;
    }

    protected UIViewControllerAnimatedTransitioning navigationController(UINavigationController navigationController, Operation operation, UIViewController from, UIViewController to) {
        return new DefaultAnimatedTransitioning(operation);
    }

    public void pushViewController(UIViewController viewController) {
        if (_priv.isUpdating) return;
        assert (!(viewController instanceof UITabBarController));
        assert (!(getViewControllers().contains(viewController)));
        assert (viewController.getParentViewController() == null || viewController.getParentViewController() == this);
        if (viewController.getParentViewController() != this) {
            addChildViewController(viewController);
        }
        updateVisibleViewController();
    }

    public UIViewController popViewController() {
        if (getViewControllers().size() <= 1) return null;
        UIViewController formerTopViewController = topViewController();
        if (formerTopViewController == visibleViewController) {
            formerTopViewController.willMoveToParentViewController(null);
        }
        formerTopViewController.removeFromParentViewController();
        updateVisibleViewController();
        return formerTopViewController;
    }

    public List<UIViewController> popToRootViewController() {
        return popToViewController(getViewControllers().get(0));
    }

    public List<UIViewController> popToViewController(UIViewController viewController) {
        List popped = new ArrayList();
        if (getViewControllers().contains(viewController)) {
            while (topViewController() != viewController) {
                UIViewController poppedController = popViewController();
                if (poppedController != null) {
                    popped.add(poppedController);
                } else {
                    break;
                }
            }
        }
        return popped;
    }

    public UIViewController getVisibleViewController() {
        return visibleViewController;
    }

    public List<UIViewController> getViewControllers() {
        return getChildViewControllers();
    }

    private UIViewController topViewController() {
        return getViewControllers().get(getViewControllers().size() - 1);
    }

    private void updateVisibleViewController() {
        _priv.isUpdating = true;
        UIViewController oldVisibleViewController = visibleViewController;
        UIViewController newVisibleViewController = topViewController();
        boolean isPushing = (oldVisibleViewController.getParentViewController() != null);
        newVisibleViewController.getView();
        oldVisibleViewController.beginAppearanceTransition(false);
        newVisibleViewController.beginAppearanceTransition(true);
        Operation operation = isPushing ? Operation.PUSH : Operation.POP;
        UIViewControllerAnimatedTransitioning animator = navigationController(this, operation, oldVisibleViewController, newVisibleViewController);
        _UIViewControllerTransitionContext transitionContext = new _UIViewControllerTransitionContext(getView(), oldVisibleViewController, newVisibleViewController);
        animator.animateTransition(transitionContext);
        long duration = animator.transitionDuration(transitionContext);
        _priv.mainHandler.postDelayed(() -> {
            getView().removeView(oldVisibleViewController.getView());
            oldVisibleViewController.endAppearanceTransition();
            newVisibleViewController.endAppearanceTransition();
            if (isPushing) {
                oldVisibleViewController.didMoveToParentViewController(null);
            } else {
                newVisibleViewController.didMoveToParentViewController(this);
            }
            _priv.isUpdating = false;
            visibleViewController = newVisibleViewController;
        }, duration);
    }

    private static class DefaultAnimatedTransitioning implements UIViewControllerAnimatedTransitioning {

        final UINavigationController.Operation operation;
        final String TRANSLATION_X = "translationX";
        final long duration = 200;

        public DefaultAnimatedTransitioning(UINavigationController.Operation operation) {
            this.operation = operation;
        }

        @Override
        public long transitionDuration(UIViewControllerContextTransitioning transitionContext) {
            return duration;
        }

        @Override
        public void animateTransition(UIViewControllerContextTransitioning transitionContext) {
            if (operation == UINavigationController.Operation.PUSH) {
                push(transitionContext.getFromView(), transitionContext.getToView(), transitionContext);
            } else {
                pop(transitionContext.getFromView(), transitionContext.getToView(), transitionContext);
            }
        }

        private void push(View fromView, View toView, UIViewControllerContextTransitioning transitionContext) {
            transitionContext.getContainerView().addView(toView);
            final float offsetX = transitionContext.getContainerView().getWidth() / 4;
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(duration);
            ObjectAnimator fromViewAnimator = ObjectAnimator.ofFloat(fromView, TRANSLATION_X, 0f, -offsetX);
            ObjectAnimator toViewAnimator = ObjectAnimator.ofFloat(toView, TRANSLATION_X, transitionContext.getContainerView().getWidth(), 0);
            animatorSet.playTogether(fromViewAnimator);
            animatorSet.playTogether(toViewAnimator);
            toViewAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    transitionContext.completeTransition(!transitionContext.getTransitionWasCancelled());
                }
            });
            animatorSet.start();
        }

        private void pop(View fromView, View toView, UIViewControllerContextTransitioning transitionContext) {
            transitionContext.getContainerView().addView(toView, 0);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(duration);
            ObjectAnimator fromViewAnimator = ObjectAnimator.ofFloat(fromView, TRANSLATION_X, 0, fromView.getWidth());
            ObjectAnimator toViewAnimator = ObjectAnimator.ofFloat(toView, TRANSLATION_X, (int) toView.getX(), 0);
            animatorSet.playTogether(fromViewAnimator);
            animatorSet.playTogether(toViewAnimator);
            toViewAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    transitionContext.completeTransition(!transitionContext.getTransitionWasCancelled());
                }
            });
            animatorSet.start();
        }
    }
}
