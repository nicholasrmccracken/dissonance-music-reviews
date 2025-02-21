package com.dissonance.app
import com.dissonance.app.screens.ProfileScreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity onCreate log", "MainActivity onCreate() Called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Linking XML layout
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity onDestroy log", "MainActivity onDestroy() Called")
    }

}