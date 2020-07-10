package com.android_viewcontroller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.android_viewcontroller.core.ViewController
import kotlinx.android.synthetic.main.view_c.view.button
import kotlinx.android.synthetic.main.view_c.view.toolbar

class CViewController(context: Context) : ViewController(context) {

    override fun loadView(): View {
        return  LayoutInflater.from(context).inflate(R.layout.view_c, null)
    }

    override fun viewDidLoad() {
        super.viewDidLoad()
        contentView.button.setOnClickListener {
            navigation?.push(DViewController(context))
        }
        contentView.toolbar.setNavigationOnClickListener {
            navigation?.pop()
        }
    }

    override fun viewWillAppear() {
        super.viewWillAppear()
    }

    override fun viewDidAppear() {
        super.viewDidAppear()
    }

    override fun viewWillDisappear() {
        super.viewWillDisappear()
    }

    override fun viewDidDisappear() {
        super.viewDidDisappear()
    }
}