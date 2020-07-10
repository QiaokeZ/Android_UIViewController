package com.android_viewcontroller

import android.animation.*
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.RadioButton
import com.android_viewcontroller.core.*
import kotlinx.android.synthetic.main.view_a.view.*

class AViewController(context: Context) : ViewController(context) {

    interface AViewControllerDelegate {
        fun aViewController(aViewController: AViewController, radioButtonClicked: RadioButton)
    }

    var delegate: AViewControllerDelegate? = null

    override fun loadView(): View {
        return LayoutInflater.from(context).inflate(R.layout.view_a, null)
    }

    override fun viewDidLoad() {
        super.viewDidLoad()
        contentView.button.setOnClickListener {
            navigation?.push(BViewController(context))
        }
        contentView.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            if (i == R.id.radioButton) {
                EventManager.post("setTransitionType", 1)
            }
            if (i == R.id.radioButton2) {
                EventManager.post("setTransitionType", 2)
            }
            if (i == R.id.radioButton3) {
                EventManager.post("setTransitionType", 3)
            }
        }
    }
}

//class NavigationControllerDelegate1 : NavigationControllerDelegate {
//
//    override fun transitioning(
//        navigationController: NavigationController?,
//        operation: TransitioningContext.Operation?,
//        from: ViewController?,
//        to: ViewController?
//    ): AnimatedTransitioning = ScaleAnimatedTransition()
//
//    class ScaleAnimatedTransition : AnimatedTransitioning {
//
//        val duration: Long = 300
//
//        override fun animateTransition(context: TransitioningContext?) {
//            if (context?.operation == TransitioningContext.Operation.push) {
//                push(context)
//            } else if (context?.operation == TransitioningContext.Operation.pop) {
//                pop(context)
//            }
//        }
//
//        private fun push(context: TransitioningContext?) {
//            val fromView = context?.getContentView(TransitioningContext.ViewKey.from)
//            val toView = context?.getContentView(TransitioningContext.ViewKey.to)
//            val animatorSet = AnimatorSet()
//            fromView?.let {
//                val animator = ObjectAnimator.ofPropertyValuesHolder(
//                    it,
//                    PropertyValuesHolder.ofFloat("scaleX", 1F, .95F),
//                    PropertyValuesHolder.ofFloat("scaleY", 1F, .95F)
//                )
//                animator.duration = duration
//                animatorSet.playTogether(animator)
//                animator.addListener(object : AnimatorListenerAdapter() {
//                    override fun onAnimationStart(animation: Animator) {
//                        super.onAnimationStart(animation)
////                        context.updateViewState(TransitioningContext.ViewState.viewWillDisappear)
//                    }
//
//                    override fun onAnimationEnd(animation: Animator) {
//                        super.onAnimationEnd(animation)
////                        context.updateViewState(TransitioningContext.ViewState.viewDidDisappear)
//                    }
//                })
//            }
//            toView?.let {
//                val animator =
//                    ObjectAnimator.ofFloat(it, "translationX", it.width.toFloat(), 0F)
//                animator.duration = duration
//                animatorSet.playTogether(animator)
//                animator.addListener(object : AnimatorListenerAdapter() {
//                    override fun onAnimationStart(animation: Animator) {
//                        super.onAnimationStart(animation)
////                        context.updateViewState(TransitioningContext.ViewState.viewWillAppear)
//                    }
//
//                    override fun onAnimationEnd(animation: Animator) {
//                        super.onAnimationEnd(animation)
////                        context.updateViewState(TransitioningContext.ViewState.viewDidAppear)
//                    }
//                })
//            }
//            animatorSet.start()
//        }
//
//        private fun pop(context: TransitioningContext?) {
//            val fromView = context?.getContentView(TransitioningContext.ViewKey.from)
//            val toView = context?.getContentView(TransitioningContext.ViewKey.to)
//            val animatorSet = AnimatorSet()
//            fromView?.let {
//                val animator =
//                    ObjectAnimator.ofFloat(it, "translationX", 0F, it.width.toFloat())
//                animator.duration = duration
//                animatorSet.playTogether(animator)
//                animator.addListener(object : AnimatorListenerAdapter() {
//                    override fun onAnimationStart(animation: Animator) {
//                        super.onAnimationStart(animation)
////                        context.updateViewState(TransitioningContext.ViewState.viewWillDisappear)
//                    }
//
//                    override fun onAnimationEnd(animation: Animator) {
//                        super.onAnimationEnd(animation)
////                        context.updateViewState(TransitioningContext.ViewState.viewDidDisappear)
//                    }
//                })
//            }
//            toView?.let {
//                val animator = ObjectAnimator.ofPropertyValuesHolder(
//                    it,
//                    PropertyValuesHolder.ofFloat("scaleX", .95F, 1F),
//                    PropertyValuesHolder.ofFloat("scaleY", .95F, 1F)
//                )
//                animator.duration = duration
//                animatorSet.playTogether(animator)
//                animator.addListener(object : AnimatorListenerAdapter() {
//                    override fun onAnimationStart(animation: Animator) {
//                        super.onAnimationStart(animation)
////                        context.updateViewState(TransitioningContext.ViewState.viewWillAppear)
//                    }
//
//                    override fun onAnimationEnd(animation: Animator) {
//                        super.onAnimationEnd(animation)
////                        context.updateViewState(TransitioningContext.ViewState.viewDidAppear)
//                    }
//                })
//            }
//            animatorSet.start()
//        }
//    }
//}


//class NavigationControllerDelegate2 : NavigationControllerDelegate {
//
//    override fun transitioning(
//        navigationController: NavigationController?,
//        operation: TransitioningContext.Operation?,
//        from: ViewController?,
//        to: ViewController?
//    ): AnimatedTransitioning = ZoomInAnimatedTransition()
//
//    class ZoomInAnimatedTransition : AnimatedTransitioning {
//
//        val duration: Long = 300
//
//        override fun animateTransition(context: TransitioningContext?) {
//            if (context?.operation == TransitioningContext.Operation.push) {
//                push(context)
//            } else if (context?.operation == TransitioningContext.Operation.pop) {
//                pop(context)
//            }
//        }
//
//        private fun push(context: TransitioningContext?) {
//            val fromView = context?.getContentView(TransitioningContext.ViewKey.from)
//            val toView = context?.getContentView(TransitioningContext.ViewKey.to)
//            fromView?.let {
//                val from = 1F
//                val to = 1F
//                val pivotValue = .5F
//                val anim = ScaleAnimation(
//                    from, to, from, to,
//                    Animation.RELATIVE_TO_SELF, pivotValue, Animation.RELATIVE_TO_SELF, pivotValue
//                )
//                anim.duration = duration
//                it.startAnimation(anim)
//            }
//
//            toView?.let {
//                val from = 1.4F
//                val to = 1F
//                val pivotValue = .5F
//                val scaleAnim = ScaleAnimation(
//                    from, to, from, to,
//                    Animation.RELATIVE_TO_SELF, pivotValue, Animation.RELATIVE_TO_SELF, pivotValue
//                )
//                scaleAnim.duration = duration
//                it.startAnimation(scaleAnim)
//            }
//        }
//
//        private fun pop(context: TransitioningContext?) {
//            val toView = context?.getContentView(TransitioningContext.ViewKey.to)
//            toView?.let {
//                val from = .95F
//                val to = 1F
//                val pivotValue = .5F
//                val anim = ScaleAnimation(
//                    from, to, from, to,
//                    Animation.RELATIVE_TO_SELF, pivotValue, Animation.RELATIVE_TO_SELF, pivotValue
//                )
//                anim.duration = duration
//                it.startAnimation(anim)
//            }
//        }
//    }
//}


//    private AnimatedTransitioning defaultTransition(TransitioningContext.Operation operation, ViewController from, ViewController to) {
//        return new DefaultAnimateTransition();
//    }

//    private static class DefaultAnimateTransition implements AnimatedTransitioning {
//
//        final String propertyName = "translationX";
//
//        @Override
//        public void animateTransition(TransitioningContext context) {
//            if (context.getOperation() == TransitioningContext.Operation.push) {
//                push(context);
//            } else if (context.getOperation() == TransitioningContext.Operation.pop) {
//                pop(context);
//            }
//        }
//
//        private void push(TransitioningContext context) {
//            View fromView = context.getContentView(TransitioningContext.ViewKey.from);
//            View toView = context.getContentView(TransitioningContext.ViewKey.to);
//            AnimatorSet animatorSet = new AnimatorSet();
//            animatorSet.setDuration(context.getDefaultTransitionDuration());
//            if (fromView != null) {
//                float offsetX = context.getContainerView().getWidth() / 4;
//                ObjectAnimator animator = ObjectAnimator.ofFloat(fromView, propertyName, 0f, -offsetX);
//                animatorSet.playTogether(animator);
//                animator.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        super.onAnimationStart(animation);
//                        context.updateTransitionState(TransitioningContext.TransitionState.fromViewWillDisappear);
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        context.updateTransitionState(TransitioningContext.TransitionState.fromViewDidDisappear);
//                    }
//                });
//            }
//            if (toView != null) {
//                ObjectAnimator animator = ObjectAnimator.ofFloat(toView, propertyName, toView.getWidth(), 0);
//                animatorSet.playTogether(animator);
//                animator.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        super.onAnimationStart(animation);
//                        context.updateTransitionState(TransitioningContext.TransitionState.toViewWillAppear);
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        context.updateTransitionState(TransitioningContext.TransitionState.toViewDidAppear);
//                        context.updateTransitionState(TransitioningContext.TransitionState.transitionFinish);
//                    }
//                });
//            }
//            animatorSet.start();
//        }
//
//        private void pop(TransitioningContext context) {
//            View fromView = context.getContentView(TransitioningContext.ViewKey.from);
//            View toView = context.getContentView(TransitioningContext.ViewKey.to);
//            AnimatorSet animatorSet = new AnimatorSet();
//            animatorSet.setDuration(context.getDefaultTransitionDuration());
//            if (fromView != null) {
//                ObjectAnimator animator = ObjectAnimator.ofFloat(fromView, propertyName, 0, fromView.getWidth());
//                animatorSet.playTogether(animator);
//                animator.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        super.onAnimationStart(animation);
//                        context.updateTransitionState(TransitioningContext.TransitionState.fromViewWillDisappear);
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        context.updateTransitionState(TransitioningContext.TransitionState.fromViewDidDisappear);
//                    }
//                });
//            }
//            if (toView != null) {
//                ObjectAnimator animator = ObjectAnimator.ofFloat(toView, propertyName, (int) toView.getX(), 0);
//                animatorSet.playTogether(animator);
//                animator.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        super.onAnimationStart(animation);
//                        context.updateTransitionState(TransitioningContext.TransitionState.toViewWillAppear);
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        context.updateTransitionState(TransitioningContext.TransitionState.toViewDidAppear);
//                        context.updateTransitionState(TransitioningContext.TransitionState.transitionFinish);
//                    }
//                });
//            }
//            animatorSet.start();
//        }
//    }