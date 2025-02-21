package com.dissonance.app.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.dissonance.app.R

class ProfileEditScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Profile screen editor onCreate log", "ProfileScreenEditor Activity onCreate() Called")

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile_editor)

        val backButton = findViewById<Button>(R.id.backButton)

        backButton.setOnClickListener {
            val intent = Intent(this, ProfileScreen::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Profile screen editor onDestory log", "ProfileScreenEditor Activity onDestroy() Called")
    }
}