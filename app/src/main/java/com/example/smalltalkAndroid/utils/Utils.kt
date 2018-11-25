package com.example.smalltalkAndroid.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.smalltalkAndroid.MainActivity
import com.example.smalltalkAndroid.R
import com.google.firebase.messaging.RemoteMessage

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hideAlpha(duration: Long = 300L) {
    this.animate()
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                this@hideAlpha.alpha = 0f
                this@hideAlpha.hide()
            }
        })
        .setDuration(duration)
        .alpha(0f)
        .start()
}

fun View.showAlpha(duration: Long = 300) {
    this@showAlpha.alpha = 0f
    this.animate()
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                this@showAlpha.alpha = 0f
                this@showAlpha.show()
            }
        })
        .setDuration(duration)
        .alpha(1f)
        .start()
}

fun View.shuffleAnimate() {
    ObjectAnimator
        .ofFloat(this, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
        .setDuration(500)
        .start()
}

fun sendNotification(context: Context, remoteMessage: RemoteMessage) {
    val notificationId = 100
    val chanelId = "chanelid"
    val intent = Intent(context, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // you must create a notification channel for API 26 and Above
        val name = "Smalltalk Channel"
        val description = "Smalltalk channel description "
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(chanelId, name, importance)
        channel.description = description
        val notificationManager = getSystemService(context, NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }

    val mBuilder = NotificationCompat.Builder(context, chanelId)
        .setSmallIcon(R.drawable.ic_announcement)
        .setContentTitle(remoteMessage.data["title"])
        .setContentText(remoteMessage.data["content"])
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true) // cancel the notification when clicked

    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(notificationId, mBuilder.build())
}