package com.example.smalltalkAndroid.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("isVisible")
fun View.isVisible(state: Boolean) {
    visibility = when (state) {
        true -> View.VISIBLE
        false -> View.GONE
    }
}

@BindingAdapter("imageUrl")
fun ImageView.imageUrl(imageUrl: String?) {
    Glide.with(context).load(imageUrl).into(this)
}