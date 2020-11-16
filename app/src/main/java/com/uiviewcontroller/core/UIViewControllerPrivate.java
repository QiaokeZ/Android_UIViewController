package com.uiviewcontroller.core;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class UIViewControllerPrivate {

    enum UIViewControllerParentageTransition {
        NONE,
        TOPARENT,
        FROMPARENT
    }

    Handler mainHandler = new Handler(Looper.getMainLooper());
    UIViewControllerParentageTransition parentageTransition;
    int appearanceTransitionStack;
    boolean viewIsAppearing;
    boolean isUpdating;

    static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
