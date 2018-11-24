package com.example.smalltalkAndroid.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun ImageView.imageUrl(imageUrl: String?) {
    Glide.with(context).load(imageUrl).into(this)
}