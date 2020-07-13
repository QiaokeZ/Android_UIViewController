package com.android_viewcontroller

import android.animation.*
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.RadioButton
import com.android_viewcontroller.core.*
import kotlinx.android.synthetic.main.view_a.view.*

class AViewController(context: Context) : ViewController(context) {

    override fun loadView(): View {
        return LayoutInflater.from(context).inflate(R.layout.view_a, null)
    }



    override fun viewDidLoad() {
        super.viewDidLoad()
        contentView.button.setOnClickListener {
            navigation?.push(BViewController(context))
        }
        contentView.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            if (i == R.id.radioButton) {
                EventManager.post("setTransitionType", 1)
            }
            if (i == R.id.radioButton2) {
                EventManager.post("setTransitionType", 2)
            }
        }
    }
}