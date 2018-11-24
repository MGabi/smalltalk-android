package com.example.smalltalkAndroid.model

import com.squareup.moshi.Json

data class LocationParams(
    @field:Json(name = "name") val name: String = "",
    @field:Json(name = "latitude") val latitude: Float = 0f,
    @field:Json(name = "longitude") val longitude: Float = 0f
)