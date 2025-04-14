package com.dissonance.app

import android.app.Application
import com.dissonance.app.utils.NetworkMonitor

class DissonanceApp : Application() {
    override fun onCreate() {
        super.onCreate()

        NetworkMonitor.register(this)
    }
}