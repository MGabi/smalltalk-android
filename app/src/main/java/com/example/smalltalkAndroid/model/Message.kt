package com.example.smalltalkAndroid.model

import com.example.smalltalkAndroid.feature.speech.MessageOwner

data class Message(var text: String,
                   val owner: MessageOwner)