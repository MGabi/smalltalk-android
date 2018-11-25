package com.example.smalltalkAndroid.feature.products

import com.example.smalltalkAndroid.model.Product

data class ProductItemViewModel(private val product: Product) {
    val id: Int = product.id
    val name: String = product.name
    val imageUrl: String = product.imageUrl
    var isBuy: Boolean = product.isBuy
    var isWishlist: Boolean = product.isWishlist
}