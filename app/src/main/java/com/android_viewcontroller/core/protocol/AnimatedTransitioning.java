package com.android_viewcontroller.core.protocol;

import androidx.annotation.NonNull;

import com.android_viewcontroller.core.AnimatedTransitionContext;

public interface AnimatedTransitioning {

    void animateTransition(@NonNull AnimatedTransitionContext context);
}
