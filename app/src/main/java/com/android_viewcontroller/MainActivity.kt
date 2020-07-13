package com.android_viewcontroller

import android.animation.*
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android_viewcontroller.core.*

class MainActivity : AppCompatActivity() {

    lateinit var navigationController: NavigationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigationController = NavigationController(this, AViewController(this))
        navigationController.setLifecycleCallbacks(MyViewControllerLifecycleCallbacks())
        val view = navigationController.makeContainerView()
        setContentView(view)

        EventManager.addObserver(this, "setTransitionType", {
            it?.let {
                when (it) {
                    1 -> {
                        navigationController.delegate = null
                    }
                    2 -> {
                        navigationController.delegate = MyNavigationControllerDelegate1()
                    }
                    3 -> {

                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        if (navigationController.children.size == 1) {
            super.onBackPressed()
        } else {
            navigationController.pop()
        }
    }
}

class MyViewControllerLifecycleCallbacks : ViewControllerLifecycleCallbacks {
    override fun viewDidAppear(viewController: ViewController?) {
        Log.e(viewController?.javaClass?.name, "- viewDidAppear")
    }

    override fun viewWillAppear(viewController: ViewController?) {
        Log.e(viewController?.javaClass?.name, "- viewWillAppear")
    }

    override fun loadView(viewController: ViewController?) {
        Log.e(viewController?.javaClass?.name, "- loadView")
    }

    override fun viewWillDisappear(viewController: ViewController?) {
        Log.e(viewController?.javaClass?.name, "- viewWillDisappear")
    }

    override fun viewDidLoad(viewController: ViewController?) {
        Log.e(viewController?.javaClass?.name, "- viewDidLoad")
    }

    override fun viewDidDisappear(viewController: ViewController?) {
        Log.e(viewController?.javaClass?.name, "- viewDidDisappear")
    }
}

class MyNavigationControllerDelegate1 : NavigationControllerDelegate {

    override fun transitioning(
        navigationController: NavigationController?,
        operation: TransitioningContext.Operation?,
        from: ViewController?,
        to: ViewController?
    ): AnimatedTransitioning? = ScaleAnimatedTransition()

    override fun shouldPopGestureTransition(
        navigationController: NavigationController?,
        from: ViewController?,
        to: ViewController?
    ): Boolean = true

    override fun popGestureTransitioning(
        navigationController: NavigationController?,
        from: ViewController?,
        to: ViewController?
    ): AnimatedTransitioning? {
        return ScalePopGestureTransition()
    }

    class ScaleAnimatedTransition : AnimatedTransitioning {

        override fun animateTransition(context: TransitioningContext?) {
            if (context?.operation == TransitioningContext.Operation.push) {
                push(context)
            } else if (context?.operation == TransitioningContext.Operation.pop) {
                pop(context)
            }
        }

        private fun push(context: TransitioningContext) {
            val fromView = context?.getContentView(TransitioningContext.ViewKey.from)
            val toView = context?.getContentView(TransitioningContext.ViewKey.to)
            val animatorSet = AnimatorSet()
            animatorSet.duration = context?.defaultTransitionDuration
            fromView?.let {
                val animator = ObjectAnimator.ofPropertyValuesHolder(
                    it,
                    PropertyValuesHolder.ofFloat("scaleX", 1F, .95F),
                    PropertyValuesHolder.ofFloat("scaleY", 1F, .95F)
                )
                animatorSet.playTogether(animator)
                animator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)
                        context.updateTransitionState(TransitioningContext.TransitionState.fromViewWillDisappear)
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        context.updateTransitionState(TransitioningContext.TransitionState.fromViewDidDisappear)
                    }
                })
            }
            toView?.let {
                val animator =
                    ObjectAnimator.ofFloat(it, "translationX", it.width.toFloat(), 0F)
                animatorSet.playTogether(animator)
                animator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)
                        context.updateTransitionState(TransitioningContext.TransitionState.toViewWillAppear)
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        context.updateTransitionState(TransitioningContext.TransitionState.toViewDidAppear)
                        context.updateTransitionState(TransitioningContext.TransitionState.transitionFinish)
                    }
                })
            }
            animatorSet.start()
        }

        private fun pop(context: TransitioningContext) {
            val fromView = context?.getContentView(TransitioningContext.ViewKey.from)
            val toView = context?.getContentView(TransitioningContext.ViewKey.to)
            val animatorSet = AnimatorSet()
            animatorSet.duration = context?.defaultTransitionDuration
            fromView?.let {
                val animator =
                    ObjectAnimator.ofFloat(it, "translationX", 0F, it.width.toFloat())
                animatorSet.playTogether(animator)
                animator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)
                        context.updateTransitionState(TransitioningContext.TransitionState.fromViewWillDisappear)
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        context.updateTransitionState(TransitioningContext.TransitionState.fromViewDidDisappear)
                    }
                })
            }
            toView?.let {
                val animator = ObjectAnimator.ofPropertyValuesHolder(
                    it,
                    PropertyValuesHolder.ofFloat("scaleX", .95F, 1F),
                    PropertyValuesHolder.ofFloat("scaleY", .95F, 1F)
                )
                animatorSet.playTogether(animator)
                animator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)
                        context.updateTransitionState(TransitioningContext.TransitionState.toViewWillAppear)
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        context.updateTransitionState(TransitioningContext.TransitionState.toViewDidAppear)
                        context.updateTransitionState(TransitioningContext.TransitionState.transitionFinish)
                    }
                })
            }
            animatorSet.start()
        }
    }

    class ScalePopGestureTransition : AnimatedTransitioning {

        private var downX = 0f
        private var disX = 0f
        private val down_max_x = 100f
        private val velocity = 1500
        private val initialScale = .95F
        private var velocityTracker: VelocityTracker? = null

        override fun animateTransition(context: TransitioningContext) {
            val fromView = context.getContentView(TransitioningContext.ViewKey.from)
            val toView = context.getContentView(TransitioningContext.ViewKey.to)
            val event = context.event
            if (fromView == null || toView == null || event == null) return

            val index = event.actionIndex
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dispatchActionDown(event);
                }
                MotionEvent.ACTION_MOVE -> {
                    if (event.getPointerId(index) == 0) {
                        dispatchActionMove(event, fromView, toView, context)
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {
                    dispatchActionUpAndCancel(event, fromView, toView, context)
                }
            }
        }

        private fun dispatchActionDown(event: MotionEvent) {
            velocityTracker = VelocityTracker.obtain()
            downX = event.x
        }

        private fun dispatchActionMove(
            event: MotionEvent,
            fromView: View,
            toView: View,
            context: TransitioningContext
        ) {
            if (downX < down_max_x) {
                velocityTracker?.let {
                    it.addMovement(event)
                    it.computeCurrentVelocity(velocity)
                }
                disX = event.x - downX
                if (disX >= 0) {
                    context.updateTransitionState(TransitioningContext.TransitionState.fromViewWillDisappear)
                    fromView.x = disX
                    context.updateTransitionState(TransitioningContext.TransitionState.toViewWillAppear)
                    toView.scaleX = (initialScale + 0.05 * (disX / toView.width)).toFloat()
                    toView.scaleY = toView.scaleX
                } else {
                    disX = 0f
                }
            }
        }

        private fun dispatchActionUpAndCancel(
            event: MotionEvent,
            fromView: View,
            toView: View,
            context: TransitioningContext
        ) {
            if (velocityTracker != null) {
                if (downX != event.x && downX < down_max_x) {
                    val propertyName = "translationX"
                    val animatorSet = AnimatorSet()
                    animatorSet.duration = context.defaultTransitionDuration / 2
                    if (disX > context.containerView.width / 2 || velocityTracker!!.xVelocity > velocity) {
                        val fromViewAnimator = ObjectAnimator.ofFloat(
                            fromView,
                            propertyName,
                            disX,
                            fromView.width.toFloat()
                        )
                        fromViewAnimator.addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)
                                context.updateTransitionState(TransitioningContext.TransitionState.fromViewDidDisappear)
                            }
                        })
                        val toViewAnimator = ObjectAnimator.ofPropertyValuesHolder(
                            toView,
                            PropertyValuesHolder.ofFloat("scaleX", toView.scaleX, 1F),
                            PropertyValuesHolder.ofFloat("scaleY", toView.scaleY, 1F)
                        )
                        toViewAnimator.addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)
                                context.updateTransitionState(TransitioningContext.TransitionState.toViewDidAppear)
                                context.updateTransitionState(TransitioningContext.TransitionState.transitionFinish)
                            }
                        })
                        animatorSet.playTogether(fromViewAnimator)
                        animatorSet.playTogether(toViewAnimator)
                    } else {
                        val fromViewAnimator =
                            ObjectAnimator.ofFloat(fromView, propertyName, disX, 0f)
                        val toViewAnimator = ObjectAnimator.ofPropertyValuesHolder(
                            toView,
                            PropertyValuesHolder.ofFloat("scaleX", toView.scaleX, initialScale),
                            PropertyValuesHolder.ofFloat("scaleY", toView.scaleY, initialScale)
                        )
                        toViewAnimator.addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)
                                context.updateTransitionState(TransitioningContext.TransitionState.transitionCancel)
                            }
                        })
                        animatorSet.playTogether(fromViewAnimator)
                        animatorSet.playTogether(toViewAnimator)
                    }
                    animatorSet.start()
                    velocityTracker?.recycle()
                    velocityTracker = null
                }
            }
        }
    }
}