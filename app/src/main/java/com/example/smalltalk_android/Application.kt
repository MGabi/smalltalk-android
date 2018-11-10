package com.example.smalltalk_android

import android.app.Application
import org.koin.standalone.StandAloneContext

@Suppress("unused")
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        StandAloneContext.startKoin(listOf(module))
    }
}