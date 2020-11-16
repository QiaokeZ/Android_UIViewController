package com.uiviewcontroller.core;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.FrameLayout;

public class UIView extends FrameLayout {

    private TouchEventListener touchEventListener;

    public interface TouchEventListener {
        void onTouchEvent(MotionEvent event);
    }

    public UIView(final Context context) {
        this(context, null);
    }

    public UIView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UIView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.WHITE);
    }

    public void setTouchEventListener(TouchEventListener touchEventListener) {
        this.touchEventListener = touchEventListener;
    }

    public boolean isDescendantOfView(ViewParent view) {
        if (view != null) {
            ViewParent find = this;
            while (find != null) {
                if (find == view) {
                    return true;
                } else {
                    find = find.getParent();
                }
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (touchEventListener != null) {
            touchEventListener.onTouchEvent(event);
            return true;
        }
        return super.onTouchEvent(event);
    }
}
