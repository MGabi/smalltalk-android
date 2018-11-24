package com.example.smalltalkAndroid

import android.app.Application
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.smalltalkAndroid.feature.cloud_messaging.SmalltalkMessagingService
import com.example.smalltalkAndroid.utils.sendNotification
import com.github.ajalt.reprint.core.Reprint
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.mcxiaoke.koi.KoiConfig
import com.mcxiaoke.koi.log.logd
import org.koin.android.ext.android.startKoin

@Suppress("unused")
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        KoiConfig.logEnabled = true
        startKoin(this, listOf(module))
        Reprint.initialize(this)
        FirebaseAnalytics.getInstance(this)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            logd("FBMessaging", "token: $it")
        }
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
    }
}