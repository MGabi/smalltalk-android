package com.example.smalltalkAndroid.networking

import com.example.smalltalkAndroid.model.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiNetworkInterface {
    @GET("/")
    fun getResponse(@Query("message") response: String): Call<Response>
}