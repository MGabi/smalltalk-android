package com.example.smalltalkAndroid

import android.text.Editable
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData

@BindingAdapter("textAnimated")
fun TextView.textAnimated(text: LiveData<String>) {
    val anim = AlphaAnimation(1.0f, 0.3f)
    anim.duration = 100
    anim.repeatCount = 1
    anim.repeatMode = Animation.REVERSE

    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationEnd(animation: Animation?) {}
        override fun onAnimationStart(animation: Animation?) {}
        override fun onAnimationRepeat(animation: Animation?) {
            this@textAnimated.text = text.value
        }
    })

    this.startAnimation(anim)
}

fun View.disable() {
    isEnabled = false
    alpha = 0.3f
}

fun View.enable() {
    isEnabled = true
    alpha = 1f
}