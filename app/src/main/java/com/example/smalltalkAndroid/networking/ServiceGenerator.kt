package com.example.smalltalkAndroid.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class ServiceGenerator {
    companion object {
        private const val URL = "http://192.168.166.147:8000"
    }

    val service: ApiNetworkInterface = Retrofit.Builder().baseUrl(URL).addConverterFactory(MoshiConverterFactory.create())
        .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()).build().create()
}