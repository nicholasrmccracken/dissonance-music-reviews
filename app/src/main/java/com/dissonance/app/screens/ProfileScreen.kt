package com.dissonance.app.screens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dissonance.app.R
import com.dissonance.app.fragments.EditProfileFragment

class ProfileScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Profile screen onCreate log", "ProfileScreen Activity onCreate() Called")
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile) // Link to XML layout

        val editProfileButton = findViewById<Button>(R.id.editProfileBtn)

        editProfileButton.setOnClickListener {
            val intent = Intent(this, ProfileEditScreen::class.java)
            startActivity(intent) // Navigate to ProfileScreen
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Profile screen onDestory log", "ProfileScreen Activity onDestroy() Called")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Lifecycle", "ProfileEditScreen: onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle", "ProfileEditScreen: onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle", "ProfileEditScreen: onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Lifecycle", "ProfileEditScreen: onStop() called")
    }
}