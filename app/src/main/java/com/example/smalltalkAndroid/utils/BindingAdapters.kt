package com.example.smalltalkAndroid.utils

import android.widget.ImageButton
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.smalltalkAndroid.R

@BindingAdapter("imageUrl")
fun ImageView.imageUrl(imageUrl: String?) {
    Glide.with(context).load(imageUrl).into(this)
}

@BindingAdapter("isBuy")
fun ImageButton.isBuy(value: Boolean) {
    setImageDrawable(
        context.getDrawable(
            if (value) {
                R.drawable.ic_shopping_cart_black_48dp
            } else {
                R.drawable.ic_add_shopping_cart_black_48dp
            }
        )
    )
}

@BindingAdapter("isWishlist")
fun ImageButton.isWishlist(value: Boolean) {
    setImageDrawable(
        context.getDrawable(
            if (value) {
                R.drawable.ic_favorite_black_48dp
            } else {
                R.drawable.ic_favorite_border_black_48dp
            }
        )
    )
}