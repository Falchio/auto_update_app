package ru.piteravto.appautoinstall

import android.app.Application
import android.content.Context

class App : Application() {

    companion object {
        private lateinit var INSTANCE: App
        val context: Context get() = INSTANCE
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}