
### 模仿iOS(UIKit)中的 UIViewController UINavigationController UITabBarController
### 源码参考：hhttps://github.com/BigZaphod/Chameleon/blob/master/UIKit/Classes/UIViewController.m

##### 效果演示1
<img src="https://s3.ax1x.com/2020/11/16/Dkmde0.gif" width="30%" height="50%"/>

##### 如何使用
```kotlin
class MainActivity : AppCompatActivity() {

    lateinit var navigationController: NavigationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigationController = NavigationController(this, AViewController(this))
        navigationController.setLifecycleCallbacks(MyViewControllerLifecycleCallbacks())
        val view = navigationController.makeContainerView()
        setContentView(view)
    }

    override fun onBackPressed() {
        if (navigationController.children.size == 1) {
            super.onBackPressed()
        } else {
            navigationController.pop()
        }
    }
}


```