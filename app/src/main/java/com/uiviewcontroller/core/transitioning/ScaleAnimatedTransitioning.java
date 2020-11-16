package com.uiviewcontroller.core.transitioning;//package com.etraffic.charge_in_hande.support.uikit.transitioning;
//
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;
//import android.animation.PropertyValuesHolder;
//import android.view.View;
//
//import com.etraffic.charge_in_hande.support.uikit.UINavigationController;
//import com.etraffic.charge_in_hande.support.uikit.UIViewController;
//import com.etraffic.charge_in_hande.support.uikit.ViewControllerLifecycle;
//import com.etraffic.charge_in_hande.support.uikit.protocol.UIViewControllerAnimatedTransitioning;
//import com.etraffic.charge_in_hande.support.uikit.protocol.UIViewControllerContextTransitioning;
//
//public class ScaleAnimatedTransitioning implements UIViewControllerAnimatedTransitioning {
//
//    private final UINavigationController.Operation operation;
//    private final String TRANSLATION_X = "translationX";
//    private final String SCALE_X = "scaleX";
//    private final String SCALE_Y = "scaleY";
//    private final long duration = 250;
//
//    public ScaleAnimatedTransitioning(UINavigationController.Operation operation) {
//        this.operation = operation;
//    }
//
//    @Override
//    public long transitionDuration(UIViewControllerContextTransitioning transitionContext) {
//        return duration;
//    }
//
//    @Override
//    public void animateTransition(UIViewControllerContextTransitioning UIViewControllerContextTransitioning) {
//        if (operation == UINavigationController.Operation.PUSH) {
//            push(UIViewControllerContextTransitioning);
//        } else {
//            pop(UIViewControllerContextTransitioning);
//        }
//    }
//
//    private void push(UIViewControllerContextTransitioning UIViewControllerContextTransitioning) {
//        UIViewController fromController = UIViewControllerContextTransitioning.getFromViewController();
//        UIViewController toController = UIViewControllerContextTransitioning.getToViewController();
//        View fromView = UIViewControllerContextTransitioning.getFromView();
//        View toView = UIViewControllerContextTransitioning.getToView();
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.setDuration(duration);
//        if (fromView != null) {
//            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(
//                    fromView,
//                    PropertyValuesHolder.ofFloat(SCALE_X, 1F, .95F),
//                    PropertyValuesHolder.ofFloat(SCALE_Y, 1F, .95F)
//            );
//            animatorSet.playTogether(animator);
//            animator.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    super.onAnimationStart(animation);
//                    UIViewControllerContextTransitioning.update(fromController, ViewControllerLifecycle.VIEW_WILL_DISAPPEAR);
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    UIViewControllerContextTransitioning.update(fromController, ViewControllerLifecycle.VIEW_DID_DISAPPEAR);
//                }
//            });
//        }
//        if (toView != null) {
//            ObjectAnimator animator =
//                    ObjectAnimator.ofFloat(
//                            toView,
//                            TRANSLATION_X,
//                            toView.getWidth(),
//                            0F);
//            animatorSet.playTogether(animator);
//            animator.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    super.onAnimationStart(animation);
//                    UIViewControllerContextTransitioning.update(toController, ViewControllerLifecycle.VIEW_WILL_APPEAR);
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    UIViewControllerContextTransitioning.update(toController, ViewControllerLifecycle.VIEW_DID_APPEAR);
//                    UIViewControllerContextTransitioning.completeTransition();
//                }
//            });
//        }
//        animatorSet.start();
//    }
//
//    private void pop(UIViewControllerContextTransitioning UIViewControllerContextTransitioning) {
//        UIViewController fromController = UIViewControllerContextTransitioning.getFromViewController();
//        UIViewController toController = UIViewControllerContextTransitioning.getToViewController();
//        View fromView = UIViewControllerContextTransitioning.getFromView();
//        View toView = UIViewControllerContextTransitioning.getToView();
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.setDuration(duration);
//        if (fromView != null) {
//            ObjectAnimator animator =
//                    ObjectAnimator.ofFloat(
//                            fromView,
//                            TRANSLATION_X,
//                            0F,
//                            fromView.getWidth());
//            animatorSet.playTogether(animator);
//            animator.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    super.onAnimationStart(animation);
//                    UIViewControllerContextTransitioning.update(fromController, ViewControllerLifecycle.VIEW_WILL_DISAPPEAR);
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    UIViewControllerContextTransitioning.update(fromController, ViewControllerLifecycle.VIEW_DID_DISAPPEAR);
//                }
//            });
//        }
//        if (toView != null) {
//            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(
//                    toView,
//                    PropertyValuesHolder.ofFloat(SCALE_X, .95F, 1F),
//                    PropertyValuesHolder.ofFloat(SCALE_Y, .95F, 1F)
//            );
//            animatorSet.playTogether(animator);
//            animator.addListener(new AnimatorListenerAdapter() {
//
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    super.onAnimationStart(animation);
//                    UIViewControllerContextTransitioning.update(toController, ViewControllerLifecycle.VIEW_WILL_APPEAR);
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    UIViewControllerContextTransitioning.update(toController, ViewControllerLifecycle.VIEW_DID_APPEAR);
//                    UIViewControllerContextTransitioning.completeTransition();
//                }
//            });
//        }
//        animatorSet.start();
//    }
//}
