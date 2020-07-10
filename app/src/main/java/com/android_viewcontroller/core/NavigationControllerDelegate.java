package com.android_viewcontroller.core;

import androidx.annotation.Nullable;

public interface NavigationControllerDelegate {

    @Nullable
    AnimatedTransitioning transitioning(NavigationController navigationController, TransitioningContext.Operation operation, ViewController from, ViewController to);

    boolean shouldPopGestureTransition(NavigationController navigationController, ViewController from, ViewController to);

    @Nullable
    AnimatedTransitioning popGestureTransitioning(NavigationController navigationController, ViewController from, ViewController to);
}

