package com.android_viewcontroller

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.android_viewcontroller.core.ViewController
import kotlinx.android.synthetic.main.view_b.view.*

class BViewController(context: Context) : ViewController(context) {

    override fun loadView(): View {
        return LayoutInflater.from(context).inflate(R.layout.view_b, null)
    }

    override fun viewDidLoad() {
        super.viewDidLoad()
        contentView.button.setOnClickListener {
            navigation?.push(CViewController(context))
        }
        contentView.toolbar.setNavigationOnClickListener {
            navigation?.pop()
        }
    }
}