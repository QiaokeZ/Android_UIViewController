package com.android_viewcontroller.core;

public interface ViewControllerLifecycleCallbacks {

    void loadView(ViewController viewController);

    void viewDidLoad(ViewController viewController);

    void viewWillAppear(ViewController viewController);

    void viewDidAppear(ViewController viewController);

    void viewWillDisappear(ViewController viewController);

    void viewDidDisappear(ViewController viewController);

}
