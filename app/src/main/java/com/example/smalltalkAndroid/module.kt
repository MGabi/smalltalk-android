package com.example.smalltalkAndroid


import com.example.smalltalkAndroid.feature.speech.SpeechViewModel
import com.example.smalltalkAndroid.networking.ServiceGenerator
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val module = module {
    viewModel { MainViewModel() }
    viewModel { SpeechViewModel(get()) }
    single { Repository(get()) }
    single { ServiceGenerator() }
}