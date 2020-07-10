package com.android_viewcontroller.core;

import android.view.MotionEvent;
import android.view.View;

public class TransitioningContext {

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
        transitionCancel,
        transitionFinish
    }

    public enum Operation {
        none, push, pop
    }

    public enum ViewControllerKey {
        from, to
    }

    public enum ViewKey {
        from, to
    }

    private ViewController fromController;
    private View fromView;

    private ViewController toController;
    private View toView;

    private Operation operation;
    private ContainerView containerView;
    private TransitionStateChangeListener transitionStateChangeListener;
    private MotionEvent event;

    public TransitioningContext(MotionEvent event, ViewController fromController, View fromView, ViewController toController, View toView, ContainerView containerView) {
        this(Operation.none, fromController, fromView, toController, toView, containerView);
        this.event = event;
    }

    public TransitioningContext(Operation operation, ViewController fromController, View fromView, ViewController toController, View toView, ContainerView containerView) {
        this.fromController = fromController;
        this.fromView = fromView;
        this.toController = toController;
        this.toView = toView;
        this.operation = operation;
        this.containerView = containerView;
    }

    public void setTransitionStateChangeListener(TransitionStateChangeListener transitionStateChangeListener) {
        this.transitionStateChangeListener = transitionStateChangeListener;
    }

    public void updateTransitionState(TransitionState state) {
        if (transitionStateChangeListener != null) {
            transitionStateChangeListener.onTransitionStateChanged(state);
        }
    }

    public ViewController getController(ViewControllerKey key) {
        if (key == ViewControllerKey.from) {
            return fromController;
        } else if (key == ViewControllerKey.to) {
            return toController;
        }
        return null;
    }

    public View getContentView(ViewKey key) {
        if (key == ViewKey.from) {
            return fromView;
        } else if (key == ViewKey.to) {
            return toView;
        }
        return null;
    }

    public ContainerView getContainerView() {
        return containerView;
    }

    public Operation getOperation() {
        return operation;
    }

    public MotionEvent getEvent() {
        return event;
    }

    public long getDefaultTransitionDuration() {
        return 300;
    }
}
