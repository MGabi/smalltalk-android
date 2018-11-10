package com.example.smalltalkAndroid

import com.example.smalltalkAndroid.model.ResponseModel
import com.example.smalltalkAndroid.networking.ApiNetworkInterface
import com.example.smalltalkAndroid.networking.ServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

typealias onSuccess <T> = (T) -> Unit

class Repository(private val serviceGenerator: ServiceGenerator) {
    fun getResponse(intent: String, onSuccess: onSuccess<ResponseModel>) {
        serviceGenerator.createService(ApiNetworkInterface::class.java).getResponse(intent).enqueue(callback { throwable, response ->
            response?.body()?.let(onSuccess)
        })
    }

    private fun <T> callback(fn: (Throwable?, Response<T>?) -> Unit) = object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) = fn(null, response)
        override fun onFailure(call: Call<T>, t: Throwable) = fn(t, null)
    }
}