package com.uiviewcontroller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.uiviewcontroller.core.UINavigationController;
import com.uiviewcontroller.core.UIViewController;

public class TestViewController extends UIViewController {

    public TestViewController(Context context) {
        super(context);
    }

    @Override
    protected void viewDidLoad() {
        super.viewDidLoad();
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.viewcontroller_test, null);
        getView().addView(rootView);
//
        rootView.findViewById(R.id.btn_back).setOnClickListener(v -> {
            UINavigationController navigationController = getNavigationController();
            if (navigationController != null) {
                navigationController.popViewController();
            }
        });
    }
}
