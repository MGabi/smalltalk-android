package com.example.smalltalkAndroid


import com.example.smalltalkAndroid.feature.MainViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val module = module {
    viewModel { MainViewModel() }
}