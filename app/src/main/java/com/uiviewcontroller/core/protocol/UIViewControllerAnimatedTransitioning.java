package com.uiviewcontroller.core.protocol;

public interface UIViewControllerAnimatedTransitioning {

    long transitionDuration(UIViewControllerContextTransitioning transitionContext);

    void animateTransition(UIViewControllerContextTransitioning transitionContext);
}
