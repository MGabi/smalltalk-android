package com.example.smalltalkAndroid.model

import com.example.smalltalkAndroid.feature.speech.MessageOwner

data class Message(var content: String,
                   val owner: MessageOwner)