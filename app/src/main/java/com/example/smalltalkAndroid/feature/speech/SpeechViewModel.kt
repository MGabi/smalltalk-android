package com.example.smalltalkAndroid.feature.speech

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel

class SpeechViewModel : ViewModel() {
    val demo = MediatorLiveData<String>()
}