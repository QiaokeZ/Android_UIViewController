package com.android_viewcontroller.core.protocol;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android_viewcontroller.core.NavigationController;
import com.android_viewcontroller.core.ViewController;

public interface NavigationAnimatedTransitioning {

    @Nullable
    AnimatedTransitioning transitioning(@NonNull NavigationController navigationController, @NonNull NavigationController.Operation operation, @NonNull ViewController from, @NonNull ViewController to);

}
