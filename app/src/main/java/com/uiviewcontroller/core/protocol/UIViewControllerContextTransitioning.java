package com.uiviewcontroller.core.protocol;


import android.view.View;

import com.uiviewcontroller.core.UIView;
import com.uiviewcontroller.core.UIViewController;

public interface UIViewControllerContextTransitioning {

    UIView getContainerView();

    UIViewController getFromViewController();

    UIViewController getToViewController();

    View getFromView();

    View getToView();

    boolean getTransitionWasCancelled();

    void completeTransition(boolean didComplete);
}
