package com.uiviewcontroller.core;


import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.uiviewcontroller.core.protocol.UIViewControllerAnimatedTransitioning;
import com.uiviewcontroller.core.protocol.UIViewControllerContextTransitioning;

import java.util.ArrayList;
import java.util.List;

public class UITabBarController extends UIViewController {

    private UITabBar tabBar;
    private int selectedIndex;
    private UIViewController selectedViewController;

    public UITabBarController(Context context) {
        super(context);
        this.tabBar = new UITabBar(context);
    }

    protected UIViewControllerAnimatedTransitioning tabBarController(UITabBarController tabBarController, UIViewController from, UIViewController to) {
        return new DefaultAnimatedTransitioning();
    }

    public UIView createView() {
        UIView view = getView();
        view.post(() -> {
            beginAppearanceTransition(true);
            if (!getChildViewControllers().isEmpty()) {
                selectedViewController = getChildViewControllers().get(0);
                getView().addView(selectedViewController.createView());
                layoutViewController(selectedViewController);
                selectedViewController.getView().post(() -> {
                    endAppearanceTransition();
                });
            } else {
                endAppearanceTransition();
            }
            layoutTabBar();
        });
        return view;
    }

    public void setSelectedIndex(int selectedIndex) {
        if (selectedIndex < getChildViewControllers().size() && this.selectedIndex != selectedIndex) {
            this.selectedIndex = selectedIndex;
            UIViewController selected = getChildViewControllers().get(selectedIndex);
            tabBar.setSelectedItem(selected.getTabBarItem());
            updateVisibleViewController();
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public UIViewController getSelectedViewController() {
        return selectedViewController;
    }

    private void layoutTabBar() {
        int y = getView().getHeight() - UIViewControllerPrivate.dip2px(getContext(), UITabBar.DEFAULT_HEIGHT);
        tabBar.setY(y);
        tabBar.setDelegate((tabBar, didSelectItem) -> {
            if (!_priv.isUpdating) {
                int index = tabBar.getItems().indexOf(didSelectItem);
                setSelectedIndex(index);
            }
        });
        getView().addView(tabBar);
        List items = new ArrayList();
        for (UIViewController viewController : getChildViewControllers()) {
            UITabBarItem item = viewController.getTabBarItem();
            items.add(item);
        }
        tabBar.setItems(items);
        if (selectedViewController != null) {
            tabBar.setSelectedItem(selectedViewController.getTabBarItem());
        }
    }

    private void layoutViewController(UIViewController viewController) {
        int height = getView().getHeight() - UIViewControllerPrivate.dip2px(getContext(), UITabBar.DEFAULT_HEIGHT);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        viewController.getView().setLayoutParams(params);
    }

    private void updateVisibleViewController() {
        _priv.isUpdating = true;
        UIViewController oldVisibleViewController = selectedViewController;
        UIViewController newVisibleViewController = getChildViewControllers().get(selectedIndex);
        newVisibleViewController.getView().setLayoutParams(oldVisibleViewController.getView().getLayoutParams());
        oldVisibleViewController.beginAppearanceTransition(false);
        newVisibleViewController.beginAppearanceTransition(true);
        UIViewControllerAnimatedTransitioning animator = tabBarController(this, oldVisibleViewController, newVisibleViewController);
        _UIViewControllerTransitionContext transitionContext = new _UIViewControllerTransitionContext(getView(), oldVisibleViewController, newVisibleViewController);
        animator.animateTransition(transitionContext);
        long duration = animator.transitionDuration(transitionContext);
        _priv.mainHandler.postDelayed(() -> {
            getView().removeView(oldVisibleViewController.getView());
            oldVisibleViewController.endAppearanceTransition();
            newVisibleViewController.endAppearanceTransition();
            newVisibleViewController.didMoveToParentViewController(this);
            selectedViewController = newVisibleViewController;
            _priv.isUpdating = false;
        }, duration);
    }

    private static class DefaultAnimatedTransitioning implements UIViewControllerAnimatedTransitioning {

        final long duration = 0;

        @Override
        public long transitionDuration(UIViewControllerContextTransitioning transitionContext) {
            return duration;
        }

        @Override
        public void animateTransition(UIViewControllerContextTransitioning transitionContext) {
            transitionContext.getContainerView().addView(transitionContext.getToView());
            transitionContext.completeTransition(!transitionContext.getTransitionWasCancelled());
        }
    }
}