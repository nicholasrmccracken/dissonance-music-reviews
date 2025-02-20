package com.dissonance.app.screens

import android.app.Activity
import android.os.Bundle
import com.dissonance.app.R

class ProfileScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile) // Link to XML layout
    }
}