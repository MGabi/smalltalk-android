package com.example.smalltalkAndroid


import com.example.smalltalkAndroid.feature.speech.SpeechViewModel
import com.example.smalltalkAndroid.networking.ServiceGenerator
import org.koin.androidx.viewmodel.experimental.builder.viewModel
import org.koin.dsl.module.module
import org.koin.experimental.builder.single

val module = module {
    viewModel<MainViewModel>()
    viewModel<SpeechViewModel>()
    single<Repository>()
    single<ServiceGenerator>()
}