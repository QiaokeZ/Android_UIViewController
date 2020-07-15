package com.android_viewcontroller.core;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class BaseTransitioningContext {

    public interface TransitionStateChangeListener {
        void onTransitionStateChanged(TransitionState state);
    }

    public enum TransitionState {
        fromViewWillAppear,
        fromViewDidAppear,
        fromViewWillDisappear,
        fromViewDidDisappear,
        toViewWillAppear,
        toViewDidAppear,
        toViewWillDisappear,
        toViewDidDisappear,
        cancel,
        finish
    }

    public enum ViewControllerKey {
        FROM, TO
    }

    public enum ViewKey {
        FROM, TO
    }

    private TransitionStateChangeListener transitionStateChangeListener;

    public void setTransitionStateChangeListener(TransitionStateChangeListener transitionStateChangeListener) {
        this.transitionStateChangeListener = transitionStateChangeListener;
    }

    public void updateTransitionState(TransitionState state) {
        if (transitionStateChangeListener != null) {
            transitionStateChangeListener.onTransitionStateChanged(state);
        }
    }

    @Nullable
    public abstract ViewController getController(ViewControllerKey key);

    @Nullable
    public abstract View getContentView(ViewKey key);

    @NonNull
    public abstract ContainerView getContainerView();

    public long getDefaultTransitionDuration() {
        return 300;
    }
}


