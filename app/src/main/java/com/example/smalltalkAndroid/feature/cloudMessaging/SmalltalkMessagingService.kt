package com.example.smalltalkAndroid.feature.cloudMessaging

import com.example.smalltalkAndroid.utils.sendNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class SmalltalkMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)
        message?.let { sendNotification(applicationContext, it) }
    }
}
