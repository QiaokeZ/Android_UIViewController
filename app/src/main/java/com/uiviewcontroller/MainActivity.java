package com.uiviewcontroller;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.uiviewcontroller.core.UINavigationController;
import com.uiviewcontroller.core.UITabBarController;
import com.uiviewcontroller.core.UITabBarItem;
import com.uiviewcontroller.core.UIViewController;

public class MainActivity extends AppCompatActivity {

    private UINavigationController navigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareView();
    }

    private void prepareView() {

        UITabBarController tabBarController = new UITabBarController(this);

        //微信
        ChatViewContoller chatViewContoller = new ChatViewContoller(this);
        configViewController(chatViewContoller, "微信", R.drawable.ic_chat_24, R.drawable.ic_chat_selected_24);
        tabBarController.addChildViewController(chatViewContoller);

        //通讯录
        AddressViewContoller addressViewContoller = new AddressViewContoller(this);
        configViewController(addressViewContoller, "通讯录", R.drawable.ic_contact_mail_24, R.drawable.ic_contact_mail_selected_24);
        tabBarController.addChildViewController(addressViewContoller);

        //发现
        FindViewContoller findViewContoller = new FindViewContoller(this);
        configViewController(findViewContoller, "发现", R.drawable.ic_search_24, R.drawable.ic_search_selected_24);
        tabBarController.addChildViewController(findViewContoller);

        //我的
        MineViewController mineViewController = new MineViewController(this);
        configViewController(mineViewController, "我的", R.drawable.ic_person_24, R.drawable.ic_person_selected_24);
        tabBarController.addChildViewController(mineViewController);

        navigationController = new UINavigationController(this, tabBarController);
        setContentView(navigationController.createView());
    }

    private void configViewController(UIViewController viewController,
                                      String title,
                                      int drawableId,
                                      int selectedDrawableId) {
        SpannableString normalTitle = SpannableString.valueOf(title);
        normalTitle.setSpan(new RelativeSizeSpan(0.7f), 0, normalTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString selectedTitle = SpannableString.valueOf(title);
        selectedTitle.setSpan(new RelativeSizeSpan(0.7f), 0, selectedTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        selectedTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#04BE02")), 0, selectedTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        Drawable normalImage = ContextCompat.getDrawable(this, drawableId);
        normalImage.setBounds(0, 0, normalImage.getMinimumWidth(), normalImage.getMinimumHeight());

        Drawable selectedImage1 = ContextCompat.getDrawable(this, selectedDrawableId);
        selectedImage1.setBounds(0, 0, selectedImage1.getMinimumWidth(), selectedImage1.getMinimumHeight());

        UITabBarItem item = new UITabBarItem.Builder()
                .setTitle(normalTitle)
                .setSelectedTitle(selectedTitle)
                .setDrawable(normalImage)
                .setSelectedDrawable(selectedImage1)
                .build();
        viewController.setTabBarItem(item);
    }

    @Override
    public void onBackPressed() {
        if (navigationController.getViewControllers().size() > 1) {
            navigationController.popViewController();
        } else {
            super.onBackPressed();
        }
    }
}