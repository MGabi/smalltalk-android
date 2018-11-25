package com.example.smalltalkAndroid.networking

import com.example.smalltalkAndroid.model.ResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiNetworkInterface {
    // tiganeala maxima pentru ca nu stim cum sa parsam semnu intrebarii pe backend pentru regular expressions
    @GET("/message={message}")
    fun getResponse(@Path("message") response: String): Call<ResponseModel>
}