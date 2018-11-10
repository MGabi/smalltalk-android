package com.example.smalltalkAndroid.model

import com.example.smalltalkAndroid.feature.speech.MessageOwner

data class Message(val text: String,
                   val owner: MessageOwner)