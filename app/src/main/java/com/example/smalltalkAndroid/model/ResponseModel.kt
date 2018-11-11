package com.example.smalltalkAndroid.model

import com.squareup.moshi.Json

data class ResponseModel(
    @field:Json(name = "message") val response: String = "",
    @field:Json(name = "validate") val requireValidation: Boolean = false,
    @field:Json(name = "call") val requireCall: Boolean = false,
    @field:Json(name = "location") val locationData: LocationParams = LocationParams()
)

data class LocationParams(
    @field:Json(name = "latitude") val latitude: Float = 0f,
    @field:Json(name = "longitude") val longitude: Float = 0f
)