package com.example.smalltalkAndroid.feature.speech

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smalltalkAndroid.Repository

class SpeechViewModel(repository: Repository) : ViewModel() {
    val twOutput = MutableLiveData<String>().apply { repository.getResponse("Ajutor sunt nesigur") { this.value = it.response } }
}