package com.android_viewcontroller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.android_viewcontroller.core.ViewController
import kotlinx.android.synthetic.main.view_d.view.*
import kotlinx.android.synthetic.main.view_d.view.button
import kotlinx.android.synthetic.main.view_d.view.toolbar

class DViewController(context: Context) : ViewController(context) {

    override fun loadView(): View {
        return LayoutInflater.from(context).inflate(R.layout.view_d, null)
    }

    override fun viewDidLoad() {
        super.viewDidLoad()
        contentView.toolbar.setNavigationOnClickListener {
            navigation?.pop()
        }
        contentView.button.setOnClickListener {
            val b = navigation?.children?.filter { it is BViewController }
            navigation?.pop(b?.first())
        }
        contentView.button1.setOnClickListener {
            navigation?.popToRoot()
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