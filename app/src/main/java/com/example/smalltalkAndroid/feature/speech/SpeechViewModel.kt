package com.example.smalltalkAndroid.feature.speech

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smalltalkAndroid.Repository
import com.example.smalltalkAndroid.model.Response

class SpeechViewModel(private val repo: Repository) : ViewModel() {

    val receivedMessageObservable = MutableLiveData<Response>()

    private val failResponses = listOf(
        "Sorry, I'm a bit dizzy and I couldn't fulfill your desire \uD83D\uDE23",
        "You don't know the wae \uD83D\uDE12"
    )

    fun getResponse(message: String) {
        repo.getResponse(
            message,
            onResponse = { receivedMessageObservable.value = it },
            onError = { receivedMessageObservable.value = Response(failResponses.shuffled().first()) })
    }
}