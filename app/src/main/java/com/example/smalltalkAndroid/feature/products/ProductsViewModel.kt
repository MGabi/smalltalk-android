package com.example.smalltalkAndroid.feature.products

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smalltalkAndroid.model.Product

class ProductsViewModel : ViewModel() {

    val products = MutableLiveData<List<Product>>()
}