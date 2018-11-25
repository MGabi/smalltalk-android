package com.example.smalltalkAndroid.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "image") val imageUrl: String,
    @field:Json(name = "is_wishlist") val isWishlist: Int,
    @field:Json(name = "is_cart") val isBuy: Int
) : Parcelable