package com.android_viewcontroller.core.protocol;

import androidx.annotation.NonNull;

import com.android_viewcontroller.core.ViewController;

public interface ViewControllerLifecycleCallbacks {

    void loadView(@NonNull ViewController viewController);

    void viewDidLoad(@NonNull ViewController viewController);

    void viewWillAppear(@NonNull ViewController viewController);

    void viewDidAppear(@NonNull ViewController viewController);

    void viewWillDisappear(@NonNull ViewController viewController);

    void viewDidDisappear(@NonNull ViewController viewController);

}
