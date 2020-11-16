package com.uiviewcontroller.core;


import android.view.View;

import com.uiviewcontroller.core.protocol.UIViewControllerContextTransitioning;


public class _UIViewControllerTransitionContext implements UIViewControllerContextTransitioning {

    private final UIViewController fromController;
    private final UIViewController toController;
    private final UIView containerView;

    public _UIViewControllerTransitionContext(UIView containerView, UIViewController fromController, UIViewController toController) {
        this.containerView = containerView;
        this.fromController = fromController;
        this.toController = toController;
    }

    @Override
    public UIView getContainerView() {
        return containerView;
    }

    @Override
    public UIViewController getFromViewController() {
        return fromController;
    }

    @Override
    public UIViewController getToViewController() {
        return toController;
    }

    @Override
    public View getFromView() {
        return fromController.getView();
    }

    @Override
    public View getToView() {
        return toController.getView();
    }

    @Override
    public boolean getTransitionWasCancelled() {
        return false;
    }

    @Override
    public void completeTransition(boolean didComplete) {

    }
}
