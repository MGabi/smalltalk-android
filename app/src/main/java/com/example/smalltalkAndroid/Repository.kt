package com.example.smalltalkAndroid

import com.example.smalltalkAndroid.model.ResponseModel
import com.example.smalltalkAndroid.networking.ApiNetworkInterface
import com.example.smalltalkAndroid.networking.ServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

typealias OnSuccess <T> = (T) -> Unit

class Repository(serviceGenerator: ServiceGenerator) {
    private val api = serviceGenerator.createService(ApiNetworkInterface::class.java)
    fun getResponse(intent: String, onSuccessCallback: OnSuccess<ResponseModel>) {
        api.getResponse(intent)
            .enqueue(callback { throwable, response ->
                response?.body()?.let(onSuccessCallback)
            })
    }

    private fun <T> callback(fn: (Throwable?, Response<T>?) -> Unit) = object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) = fn(null, response)
        override fun onFailure(call: Call<T>, t: Throwable) = fn(t, null)
    }
}