package com.android_viewcontroller.core;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AnimatedTransitionContext extends BaseTransitioningContext {

    private ViewController fromController;
    private ViewController toController;
    private ContainerView containerView;
    private NavigationController.Operation operation;
    private View fromView;
    private View toView;

    public AnimatedTransitionContext(NavigationController.Operation operation, ViewController fromController, View fromView, ViewController toController, View toView, ContainerView containerView) {
        this.fromController = fromController;
        this.toController = toController;
        this.containerView = containerView;
        this.operation = operation;
        this.fromView = fromView;
        this.toView = toView;
    }

    @Nullable
    @Override
    public ViewController getController(ViewControllerKey key) {
        if (key == ViewControllerKey.FROM) {
            return fromController;
        } else if (key == ViewControllerKey.TO) {
            return toController;
        }
        return null;
    }

    @Nullable
    @Override
    public View getContentView(ViewKey key) {
        if (key == ViewKey.FROM) {
            return fromView;
        } else if (key == ViewKey.TO) {
            return toView;
        }
        return null;
    }

    @NonNull
    @Override
    public ContainerView getContainerView() {
        return containerView;
    }

    public NavigationController.Operation getOperation() {
        return operation;
    }
}
