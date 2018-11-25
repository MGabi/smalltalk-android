package com.example.smalltalkAndroid.feature.products

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smalltalkAndroid.Repository
import com.example.smalltalkAndroid.model.Product

class ProductsViewModel(val repository: Repository) : ViewModel() {

    val products = MutableLiveData<List<Product>>()
}