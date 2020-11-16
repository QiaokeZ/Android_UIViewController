package com.uiviewcontroller.core;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class UIViewController {

    private Context context;
    private UITabBarItem tabBarItem;
    private List<UIViewController> childViewControllers;
    private UIViewController parentViewController;
    protected UIView view;
    protected UIViewControllerPrivate _priv;

    public UIViewController(Context context) {
        this.context = context;
        this.tabBarItem = new UITabBarItem();
        this.childViewControllers = new ArrayList();
        this._priv = new UIViewControllerPrivate();
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

    protected void loadView() {

    }

    protected boolean shouldAutomaticallyForwardAppearanceMethods() {
        return true;
    }

    protected void beginAppearanceTransition(boolean isAppearing) {
        if (_priv.appearanceTransitionStack == 0 || (_priv.appearanceTransitionStack > 0 && _priv.viewIsAppearing != isAppearing)) {
            _priv.appearanceTransitionStack = 1;
            _priv.viewIsAppearing = isAppearing;
            if (shouldAutomaticallyForwardAppearanceMethods()) {
                for (UIViewController child : childViewControllers) {
                    if (child.isViewLoaded() && child.getView().isDescendantOfView(getView())) {
                        child.beginAppearanceTransition(isAppearing);
                    }
                }
            }
            if (_priv.viewIsAppearing) {
                viewWillAppear();
            } else {
                viewWillDisappear();
            }
        } else {
            _priv.appearanceTransitionStack++;
        }
    }

    protected void endAppearanceTransition() {
        if (_priv.appearanceTransitionStack > 0) {
            _priv.appearanceTransitionStack--;
            if (_priv.appearanceTransitionStack == 0) {
                if (shouldAutomaticallyForwardAppearanceMethods()) {
                    for (UIViewController child : childViewControllers) {
                        child.endAppearanceTransition();
                    }
                }
                if (_priv.viewIsAppearing) {
                    viewDidAppear();
                } else {
                    viewDidDisappear();
                }
            }
        }
    }

    protected void willMoveToParentViewController(UIViewController parent) {
        if (parent != null) {
            _priv.parentageTransition = UIViewControllerPrivate.UIViewControllerParentageTransition.TOPARENT;
        } else {
            _priv.parentageTransition = UIViewControllerPrivate.UIViewControllerParentageTransition.FROMPARENT;
        }
    }

    protected void didMoveToParentViewController(UIViewController parent) {
        _priv.parentageTransition = UIViewControllerPrivate.UIViewControllerParentageTransition.NONE;
    }

    protected void removeFromParentViewController() {
        if (parentViewController != null) {
            parentViewController.childViewControllers.remove(this);
            parentViewController = null;
        }
        didMoveToParentViewController(null);
    }

    public void addChildViewController(UIViewController viewController) {
        assert (viewController != null);
        viewController.willMoveToParentViewController(this);
        childViewControllers.add(viewController);
        viewController.parentViewController = this;
    }

    public UIView createView() {
        UIView view = getView();
        view.post(() -> {
            beginAppearanceTransition(true);
            endAppearanceTransition();
        });
        return view;
    }

    public UIView getView() {
        if (!isViewLoaded()) {
            loadView();
            if (view == null) {
                view = new UIView(context);
            }
            viewDidLoad();
        }
        return view;
    }

    public UITabBarItem getTabBarItem() {
        return tabBarItem;
    }

    public void setTabBarItem(UITabBarItem tabBarItem) {
        this.tabBarItem = tabBarItem;
    }

    public boolean isViewLoaded() {
        return view != null;
    }

    public Context getContext() {
        return context;
    }

    public UIViewController getParentViewController() {
        return parentViewController;
    }

    public List<UIViewController> getChildViewControllers() {
        return childViewControllers;
    }

    public UINavigationController getNavigationController() {
        UIViewController viewController = getParentViewController();
        while (viewController != null) {
            if (viewController instanceof UINavigationController) {
                return (UINavigationController) viewController;
            }
            viewController = viewController.getParentViewController();
        }
        return null;
    }
}
