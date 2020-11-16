package com.uiviewcontroller.core.transitioning;//package com.etraffic.charge_in_hande.support.uikit.transitioning;
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;
//import android.animation.PropertyValuesHolder;
//import android.view.MotionEvent;
//import android.view.VelocityTracker;
//import android.view.View;
//
//import com.etraffic.charge_in_hande.support.uikit.UIViewController;
//import com.etraffic.charge_in_hande.support.uikit.ViewControllerLifecycle;
//import com.etraffic.charge_in_hande.support.uikit.protocol.UIViewControllerAnimatedTransitioning;
//import com.etraffic.charge_in_hande.support.uikit.protocol.UIViewControllerContextTransitioning;
//
//
//public class ScalePopGestureAnimatedTransitioning implements UIViewControllerAnimatedTransitioning {
//
//    private float startX;
//    private float disX;
//    private VelocityTracker velocityTracker;
//    private final float max_x_range = 200F;
//    private final int velocity = 1500;
//    private final float initialScale = .95F;
//    private final long duration = 100;
//    private final String TRANSLATION_X = "translationX";
//    private final String SCALE_X = "scaleX";
//    private final String SCALE_Y = "scaleY";
//
//    private UIViewController fromController;
//    private UIViewController toController;
//    private View fromView;
//    private View toView;
//
//    @Override
//    public long transitionDuration(UIViewControllerContextTransitioning transitionContext) {
//        return 0;
//    }
//
//    public void prepare(UIViewControllerContextTransitioning transitionContext) {
//        fromController = transitionContext.getFromViewController();
//        fromView = transitionContext.getFromView();
//        toController = transitionContext.getToViewController();
//        toView = transitionContext.getToView();
//    }
//
//    @Override
//    public void animateTransition(UIViewControllerContextTransitioning transitionContext) {
//        MotionEvent event = transitionContext.getMotionEvent();
//        int index = event.getActionIndex();
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                dispatchActionDown(event);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (event.getPointerId(index) == 0) {
//                    dispatchActionMove(event, transitionContext);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_POINTER_UP:
//                dispatchActionUpAndCancel(event, transitionContext);
//                break;
//        }
//    }
//
//    private void dispatchActionDown(MotionEvent event) {
//        velocityTracker = VelocityTracker.obtain();
//        startX = event.getX();
//    }
//
//    private void dispatchActionMove(MotionEvent event, UIViewControllerContextTransitioning transitionContext) {
//        if (startX < max_x_range && velocityTracker != null) {
//            velocityTracker.addMovement(event);
//            velocityTracker.computeCurrentVelocity(velocity);
//            disX = event.getX() - startX;
//            if (disX >= 0) {
//                transitionContext.update(fromController, ViewControllerLifecycle.VIEW_WILL_DISAPPEAR);
//                fromView.setX(disX);
//                transitionContext.update(toController, ViewControllerLifecycle.VIEW_WILL_APPEAR);
//                toView.setScaleX((float) (initialScale + 0.05 * (disX / toView.getWidth())));
//                toView.setScaleY(toView.getScaleX());
//            } else {
//                disX = 0;
//            }
//        }
//    }
//
//    private void dispatchActionUpAndCancel(MotionEvent event, UIViewControllerContextTransitioning transitionContext) {
//        if (velocityTracker != null) {
//            if (startX != event.getX() && startX < max_x_range) {
//                AnimatorSet animatorSet = new AnimatorSet();
//                animatorSet.setDuration(duration);
//                if (disX > transitionContext.getContainerView().getWidth() / 2 || velocityTracker.getXVelocity() > velocity) {
//                    ObjectAnimator fromViewAnimator = ObjectAnimator.ofFloat(fromView, TRANSLATION_X, disX, fromView.getWidth());
//                    fromViewAnimator.addListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//                            transitionContext.update(fromController, ViewControllerLifecycle.VIEW_DID_DISAPPEAR);
//                        }
//                    });
//                    ObjectAnimator toViewAnimator = ObjectAnimator.ofPropertyValuesHolder(
//                            toView,
//                            PropertyValuesHolder.ofFloat(SCALE_X, toView.getScaleY(), 1F),
//                            PropertyValuesHolder.ofFloat(SCALE_Y, toView.getScaleY(), 1F)
//                    );
//                    toViewAnimator.addListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//                            transitionContext.update(toController, ViewControllerLifecycle.VIEW_DID_APPEAR);
//                            transitionContext.completeTransition();
//                        }
//                    });
//                    animatorSet.playTogether(fromViewAnimator);
//                    animatorSet.playTogether(toViewAnimator);
//                } else {
//                    ObjectAnimator fromViewAnimator = ObjectAnimator.ofFloat(fromView, TRANSLATION_X, disX, 0f);
//                    ObjectAnimator toViewAnimator = ObjectAnimator.ofPropertyValuesHolder(
//                            toView,
//                            PropertyValuesHolder.ofFloat(SCALE_X, toView.getScaleX(), initialScale),
//                            PropertyValuesHolder.ofFloat(SCALE_Y, toView.getScaleY(), initialScale)
//                    );
//                    toViewAnimator.addListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//                            transitionContext.cancelTransition();
//                        }
//                    });
//                    animatorSet.playTogether(fromViewAnimator);
//                    animatorSet.playTogether(toViewAnimator);
//                }
//                animatorSet.start();
//            }
//            velocityTracker.recycle();
//            velocityTracker = null;
//        }
//    }
//}
