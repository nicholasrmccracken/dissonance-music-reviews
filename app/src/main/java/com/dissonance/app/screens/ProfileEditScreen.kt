package com.dissonance.app.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dissonance.app.R

class ProfileEditScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile_editor)
    }
}