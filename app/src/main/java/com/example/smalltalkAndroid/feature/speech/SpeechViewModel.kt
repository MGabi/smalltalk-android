package com.example.smalltalkAndroid.feature.speech

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smalltalkAndroid.Repository
import com.example.smalltalkAndroid.model.Message

class SpeechViewModel(private val repo: Repository) : ViewModel() {
    val receivedMessageObservable = MutableLiveData<Message>()

    fun getResponse(message: String) {
        repo.getResponse(message) {
            receivedMessageObservable.value = Message(it.response, MessageOwner.SERVER)
        }
    }
}