package com.example.smalltalkAndroid

import android.app.Application
import com.github.ajalt.reprint.core.Reprint
import com.mcxiaoke.koi.KoiConfig
import org.koin.android.ext.android.startKoin

@Suppress("unused")
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        KoiConfig.logEnabled = true
        startKoin(this, listOf(module))
        Reprint.initialize(this)
    }
}