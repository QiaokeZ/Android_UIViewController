package com.android_viewcontroller.core.protocol;

import androidx.annotation.NonNull;

import com.android_viewcontroller.core.PopGestureTransitionContext;

public interface PopGestureTransitioning {

    void transition(@NonNull PopGestureTransitionContext context);
}
