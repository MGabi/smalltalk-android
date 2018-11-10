package com.example.smalltalkAndroid.feature.speech

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smalltalkAndroid.OnSuccess
import com.example.smalltalkAndroid.Repository
import com.example.smalltalkAndroid.model.Message
import com.example.smalltalkAndroid.model.ResponseModel

class SpeechViewModel(private val repo: Repository) : ViewModel() {
    val receivedMessageObservable = MutableLiveData<Message>()

    fun getResponse(message: String) {
        repo.getResponse(message, object : OnSuccess<ResponseModel> {
            override fun invoke(p1: ResponseModel) {
                receivedMessageObservable.value = Message(p1.response, MessageOwner.SERVER)
            }
        })
    }
}