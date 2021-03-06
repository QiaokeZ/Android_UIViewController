package com.uiviewcontroller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.uiviewcontroller.core.UIViewController;

public class FindViewContoller extends UIViewController {


    public FindViewContoller(Context context) {
        super(context);
    }

    @Override
    protected void viewDidLoad() {
        super.viewDidLoad();
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.viewcontroller_find, null);
        getView().addView(rootView);

        rootView.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNavigationController().pushViewController(new TestViewController(getContext()));
            }
        });

    }
}
