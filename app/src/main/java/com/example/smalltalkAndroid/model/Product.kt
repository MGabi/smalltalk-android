package com.example.smalltalkAndroid.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(@field:Json(name = "id") val id: Int, @field:Json(name = "name") val name: String, @field:Json(name = "image_url") val imageUrl: String) : Parcelable