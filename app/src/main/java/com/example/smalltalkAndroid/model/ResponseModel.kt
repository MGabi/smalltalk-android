package com.example.smalltalkAndroid.model

import com.squareup.moshi.Json

data class ResponseModel(@field:Json(name = "message") val response: String)