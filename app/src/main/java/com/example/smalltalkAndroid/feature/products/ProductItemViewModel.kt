package com.example.smalltalkAndroid.feature.products

import com.example.smalltalkAndroid.model.Product

data class ProductItemViewModel(private val product: Product) {
    val id: Int = product.id
    val name: String = product.name
    val imageUrl: String = product.imageUrl
    val price: String = product.price.toString() + " lei"
    var isBuy: Boolean = product.isBuy == 1
    var isWishlist: Boolean = product.isWishlist == 1
}