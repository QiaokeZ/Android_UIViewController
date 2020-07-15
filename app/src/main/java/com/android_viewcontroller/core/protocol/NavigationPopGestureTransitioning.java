package com.android_viewcontroller.core.protocol;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android_viewcontroller.core.NavigationController;
import com.android_viewcontroller.core.ViewController;

public interface NavigationPopGestureTransitioning {

    @Nullable
    PopGestureTransitioning transitioning(@NonNull NavigationController navigationController, @NonNull ViewController from, @NonNull ViewController to);
}
