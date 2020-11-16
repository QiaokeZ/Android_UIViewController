package com.uiviewcontroller.core;

import android.graphics.drawable.Drawable;
import android.text.SpannableString;

public class UITabBarItem {

    private SpannableString title;
    private SpannableString selectedTitle;
    private Drawable drawable;
    private Drawable selectedDrawable;
    private String badgeValue;
    private int badgeColor;

    public UITabBarItem() {
        this(new Builder());
    }

    public UITabBarItem(Builder builder) {
        this.title = builder.title;
        this.selectedTitle = builder.selectedTitle;
        this.drawable = builder.drawable;
        this.selectedDrawable = builder.selectedDrawable;
        this.badgeValue = builder.badgeValue;
        this.badgeColor = builder.badgeColor;
    }

    public SpannableString getTitle() {
        return title;
    }

    public SpannableString getSelectedTitle() {
        return selectedTitle;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public Drawable getSelectedDrawable() {
        return selectedDrawable;
    }

    public String getBadgeValue() {
        return badgeValue;
    }

    public int getBadgeColor() {
        return badgeColor;
    }

    public static class Builder {
        private SpannableString title;
        private SpannableString selectedTitle;
        private Drawable drawable;
        private Drawable selectedDrawable;
        private String badgeValue;
        private int badgeColor;

        public Builder setTitle(SpannableString title) {
            this.title = title;
            return this;
        }

        public Builder setSelectedTitle(SpannableString selectedTitle) {
            this.selectedTitle = selectedTitle;
            return this;
        }

        public Builder setDrawable(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        public Builder setSelectedDrawable(Drawable selectedDrawable) {
            this.selectedDrawable = selectedDrawable;
            return this;
        }

        public Builder setBadgeValue(String badgeValue) {
            this.badgeValue = badgeValue;
            return this;
        }

        public Builder setBadgeColor(int badgeColor) {
            this.badgeColor = badgeColor;
            return this;
        }

        public UITabBarItem build() {
            return new UITabBarItem(this);
        }
    }
}
