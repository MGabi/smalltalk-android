package com.example.smalltalkAndroid.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
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

var ImageView.imageAnimated: Drawable
    get() = this.background
    set(value) {
        val anim = AlphaAnimation(1f, 0.5f)
        anim.duration = 300
        anim.repeatCount = 1
        anim.repeatMode = Animation.REVERSE

        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {}
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {
                this@imageAnimated.background = value
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

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hideAlpha(duration: Long = 300L) {
    this.animate()
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                this@hideAlpha.alpha = 0f
                this@hideAlpha.hide()
            }
        })
        .setDuration(duration)
        .alpha(0f)
        .start()
}

fun View.showAlpha(duration: Long = 300) {
    this@showAlpha.alpha = 0f
    this.animate()
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                this@showAlpha.alpha = 0f
                this@showAlpha.show()
            }
        })
        .setDuration(duration)
        .alpha(1f)
        .start()
}

fun View.toggleView(boxHeight: Int, collapse: Boolean = true, duration: Long = 300L) {
    val animator =
        if (collapse)
            ValueAnimator.ofInt(boxHeight, 0)
        else
            ValueAnimator.ofInt(0, boxHeight)
    animator.setEvaluator(IntEvaluator())
    animator.duration = duration
    animator.interpolator = AccelerateDecelerateInterpolator()
    animator.addUpdateListener {
        this@toggleView.layoutParams.height = it.animatedValue as? Int ?: return@addUpdateListener
        this@toggleView.requestLayout()
    }
    animator.start()
}