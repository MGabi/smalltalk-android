package com.example.smalltalkAndroid

import com.example.smalltalkAndroid.networking.ServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

typealias OnResponse <T> = (T) -> Unit

class Repository(private val serviceGenerator: ServiceGenerator) {
    fun getResponse(intent: String, onResponse: OnResponse<com.example.smalltalkAndroid.model.Response>, onError: OnResponse<Throwable>) {
        serviceGenerator.service.getResponse(intent.toLowerCase()).enqueue(callback { throwable, response ->
            response?.body()?.let(onResponse)
            throwable?.let(onError)
            })
    }

    private fun <T> callback(fn: (Throwable?, Response<T>?) -> Unit) = object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) = fn(null, response)
        override fun onFailure(call: Call<T>, t: Throwable) = fn(t, null)
    }
}