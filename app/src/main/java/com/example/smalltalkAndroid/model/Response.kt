package com.example.smalltalkAndroid.model

import com.squareup.moshi.Json

data class Response(
    @field:Json(name = "message") val response: String = "",
    @field:Json(name = "validate") val requireValidation: Boolean = false,
    @field:Json(name = "call") val requireCall: Boolean = false,
    @field:Json(name = "location") val locationData: LocationParams = LocationParams(),
    @field:Json(name = "url") val url: String = "",
    @field:Json(name = "products_intent") val productsIntent: Boolean = false, // if the intent is to show a list of products
    @field:Json(name = "products") val products: List<Product> = emptyList()
)