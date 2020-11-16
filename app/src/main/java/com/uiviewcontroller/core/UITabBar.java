package com.uiviewcontroller.core;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UITabBar extends ViewGroup {

    public static final int DEFAULT_HEIGHT = 50;
    private UITabBarDelegate delegate;
    private UITabBarItem selectedItem;
    private List<UITabBarItem> items;
    private List<UITabBarButton> buttons;

    public interface UITabBarDelegate {
        void tabBar(UITabBar tabBar, UITabBarItem didSelectItem);
    }

    public UITabBar(Context context) {
        super(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, UIViewControllerPrivate.dip2px(context, UITabBar.DEFAULT_HEIGHT));
        setLayoutParams(params);
    }

    public void setSelectedItem(UITabBarItem selectedItem) {
        this.selectedItem = selectedItem;
        if (buttons != null) {
            for (UITabBarButton button : buttons) {
                button.updateContent();
            }
        }
    }

    public UITabBarItem getSelectedItem() {
        return selectedItem;
    }

    public void setItems(List<UITabBarItem> items) {
        this.items = items;
        addButtons();
        requestLayout();
    }

    public List<UITabBarItem> getItems() {
        return items;
    }

    public void setDelegate(UITabBarDelegate delegate) {
        this.delegate = delegate;
    }

    public UITabBarDelegate getDelegate() {
        return delegate;
    }

    private void addButtons() {
        if (buttons != null) {
            removeAllViews();
            buttons.clear();
        } else {
            buttons = new ArrayList();
        }
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                UITabBarItem item = items.get(i);
                UITabBarButton button = new UITabBarButton(getContext(), item, delegate);
                addView(button);
                buttons.add(button);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (getChildCount() > 0 && changed) {
            int childViewWidth = (right - left) / getChildCount();
            for (int i = 0; i < getChildCount(); i++) {
                View childView = getChildAt(i);
                int childViewLeft = i * childViewWidth;
                int childViewRight = childViewWidth + childViewLeft;
                childView.layout(childViewLeft, 0, childViewRight, bottom);
            }
        }
    }

    private static class UITabBarButton extends ViewGroup {

        UITabBarItem item;
        UITabBarDelegate delegate;
        TextView textView;

        public UITabBarButton(Context context, UITabBarItem item, UITabBarDelegate delegate) {
            super(context);
            this.item = item;
            this.delegate = delegate;
            prepareView();
        }

        private void prepareView() {
            textView = new TextView(getContext());
            textView.setAllCaps(false);
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0, 20, 0, 0);
            textView.setOnClickListener((v) -> {
                UITabBar tabBar = (UITabBar) getParent();
//                tabBar.setSelectedItem(item);
                if (delegate != null) {
                    delegate.tabBar(tabBar, item);
                }
            });
            addView(textView);
        }

        public void updateContent() {
            UITabBar tabBar = (UITabBar) getParent();
            UITabBarItem selectedItem = tabBar.selectedItem;
            if (item == selectedItem) {
                textView.setText(selectedItem.getSelectedTitle());
                textView.setCompoundDrawables(null, selectedItem.getSelectedDrawable(), null, null);
            } else {
                textView.setText(item.getTitle());
                textView.setCompoundDrawables(null, item.getDrawable(), null, null);
            }
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            if (changed) {
                textView.layout(0, 0, right - left, bottom - top);
            }
        }
    }
}
