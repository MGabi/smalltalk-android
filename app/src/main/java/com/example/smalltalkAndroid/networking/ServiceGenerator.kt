package com.example.smalltalkAndroid.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ServiceGenerator {
    companion object {
        private const val URL = "http://192.168.1.25:8080"
    }

    val service = createService(ApiNetworkInterface::class.java)

    private fun <S> createService(serviceClass: Class<S>): S =
        Retrofit.Builder().baseUrl(URL).addConverterFactory(MoshiConverterFactory.create()).client(
            OkHttpClient.Builder().addInterceptor(
                HttpLoggingInterceptor().setLevel(
                    HttpLoggingInterceptor.Level.BODY
                )
            ).build()
        ).build().create(serviceClass)
}