package com.android_viewcontroller.core;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class ViewController {

    protected final Context mContext;
    protected final List<ViewController> children = new ArrayList();
    protected ViewControllerLifecycleCallbacks callbacks;
    protected ContainerView containerView;
    protected View contentView;
    protected ViewController parent;
    protected ViewController visible;
    private boolean isViewLoaded;
//    private boolean popGestureEnabled = true;

    public ViewController(Context context) {
        mContext = context;
        initialize();
    }

    private void initialize() {
        contentView = new FrameLayout(mContext);
    }

    protected View loadView() {
        return contentView;
    }

    protected void viewDidLoad() {

    }

    protected void viewWillAppear() {

    }

    protected void viewDidAppear() {

    }

    protected void viewWillDisappear() {

    }

    protected void viewDidDisappear() {

    }

    public void setLifecycleCallbacks(ViewControllerLifecycleCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public boolean isViewLoaded() {
        return isViewLoaded;
    }

    public List<ViewController> getChildren() {
        return children;
    }

    public Context getContext() {
        return mContext;
    }

    public ContainerView makeContainerView() {
        if (containerView == null) {
            containerView = new ContainerView(mContext);
            containerView.setBackgroundColor(Color.BLACK);
            if (children.size() > 0) {
                containerView.addView(_notifyLoadView());
                _notifyViewDidLoad();
                _notifyViewWillAppear();
                ViewController controller = children.get(0);
                controller.parent = this;
                controller.contentView = controller._notifyLoadView();
                containerView.addView(controller.contentView);
                controller._notifyViewDidLoad();
                controller._notifyViewWillAppear();
                _notifyViewDidAppear();
                controller._notifyViewDidAppear();
                visible = controller;
            }
            return containerView;
        } else {
            return containerView;
        }
    }

    public NavigationController getNavigation() {
        ViewController result = parent;
        while (result != null) {
            if (result instanceof NavigationController) {
                return (NavigationController) result;
            }
            result = result.parent;
        }
        return null;
    }

    View _notifyLoadView() {
        if (parent != null && parent.callbacks != null) {
            parent.callbacks.loadView(this);
        }
        View view = loadView();
        isViewLoaded = true;
        return view;
    }

    void _notifyViewDidLoad() {
        if (parent != null && parent.callbacks != null) {
            parent.callbacks.viewDidLoad(this);
        }
        viewDidLoad();
    }

    void _notifyViewWillAppear() {
        if (parent != null && parent.callbacks != null) {
            parent.callbacks.viewWillAppear(this);
        }
        viewWillAppear();
    }

    void _notifyViewDidAppear() {
        if (parent != null && parent.callbacks != null) {
            parent.callbacks.viewDidAppear(this);
        }
        viewDidAppear();
    }

    void _notifyViewWillDisappear() {
        if (parent != null && parent.callbacks != null) {
            parent.callbacks.viewWillDisappear(this);
        }
        viewWillDisappear();
    }

    void _notifyViewDidDisappear() {
        if (parent != null && parent.callbacks != null) {
            parent.callbacks.viewDidDisappear(this);
        }
        viewDidDisappear();
    }
}
