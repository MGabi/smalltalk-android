package com.example.smalltalkAndroid.feature.speech

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smalltalkAndroid.Repository
import com.example.smalltalkAndroid.model.ResponseModel
import java.lang.Exception
import kotlin.random.Random

class SpeechViewModel(private val repo: Repository) : ViewModel() {
    val receivedMessageObservable = MutableLiveData<ResponseModel>()

    private val failResponses = listOf(
        "Sorry, I'm a bit dizzy and I couldn't fulfill your desire \uD83D\uDE23",
        "You don't know the wae \uD83D\uDE12"
    )

    fun getResponse(message: String) {
        repo.getResponse(message, {
            receivedMessageObservable.value = it
        }, {
            val msg = try {
                failResponses.shuffled().first()
            }catch (ex: Exception) {
                ex.printStackTrace()
                "Sorry, something went wrong in my mind \uD83D\uDE2F"
            }
            receivedMessageObservable.value = ResponseModel(msg)
        })
    }
}