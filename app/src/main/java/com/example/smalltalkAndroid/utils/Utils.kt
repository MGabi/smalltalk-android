package com.example.smalltalkAndroid.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View

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

fun View.shuffleAnimate() {
    ObjectAnimator
        .ofFloat(this, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
        .setDuration(500)
        .start()
}