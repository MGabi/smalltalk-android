package com.example.smalltalkAndroid

import com.example.smalltalkAndroid.model.ResponseModel
import com.example.smalltalkAndroid.networking.ServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

typealias OnResponse <T> = (T) -> Unit

class Repository(private val serviceGenerator: ServiceGenerator) {
    fun getResponse(intent: String, onResponse: OnResponse<ResponseModel>, onError: OnResponse<Throwable>) {
        serviceGenerator.service.getResponse(intent.toLowerCase()).enqueue(callback { throwable, response ->
            response?.body()?.let(onResponse)
            throwable?.let(onError)
        })
    }

    fun setCart(id: Int, value: Boolean) {
        serviceGenerator.service.updateCartItem(id, if (value) 1 else 0).enqueue(callback { _, _ -> })
    }

    fun setWishlist(id: Int, value: Boolean) {
        serviceGenerator.service.updateWishlistItem(id, if (value) 1 else 0).enqueue(callback { _, _ -> })
    }

    fun confirmFinger() {
        serviceGenerator.service.confirmFinger().enqueue(callback { _, _ -> })
    }

    private fun <T> callback(fn: (Throwable?, Response<T>?) -> Unit) = object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) = fn(null, response)
        override fun onFailure(call: Call<T>, t: Throwable) = fn(t, null)
    }
}