package com.android_viewcontroller.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class ContainerView extends FrameLayout {

    private TouchEventListener touchEventListener;

    public interface TouchEventListener {
        void onTouchEvent(MotionEvent event);
    }

    public ContainerView(final Context context) {
        this(context, null);
    }

    public ContainerView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContainerView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTouchEventListener(TouchEventListener touchEventListener) {
        this.touchEventListener = touchEventListener;
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
