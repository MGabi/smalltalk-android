package com.example.smalltalkAndroid.feature.speech

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smalltalkAndroid.Repository
import com.example.smalltalkAndroid.model.ResponseModel

class SpeechViewModel(private val repo: Repository) : ViewModel() {
    val receivedMessageObservable = MutableLiveData<ResponseModel>()

    fun getResponse(message: String) {
        repo.getResponse(message, {
            receivedMessageObservable.value = it
        }, {
            receivedMessageObservable.value = ResponseModel("Sorry, I'm a bit dizzy and i I couldn't fulfill your desire \uD83D\uDE23")
        })
    }
}