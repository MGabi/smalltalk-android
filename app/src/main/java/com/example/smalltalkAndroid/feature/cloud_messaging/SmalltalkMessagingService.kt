package com.example.smalltalkAndroid.feature.cloud_messaging

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.example.smalltalkAndroid.utils.sendNotification


class SmalltalkMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)
        message?.let { sendNotification(applicationContext, it) }
    }
}
