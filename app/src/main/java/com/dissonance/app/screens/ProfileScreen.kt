package com.dissonance.app.screens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dissonance.app.R
import com.dissonance.app.fragments.EditProfileFragment
import com.dissonance.app.viewmodel.ProfileViewModel
import com.dissonance.app.data.model.User
import com.dissonance.app.data.model.Profile

class ProfileScreen : AppCompatActivity() {

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Profile screen onCreate log", "ProfileScreen Activity onCreate() Called")
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile) // Link to XML layout

        profileViewModel.fetchUserAndProfile("user123")

        profileViewModel.userProfileObserve.observe(this, Observer { userProfile ->
            if (userProfile != null) {
                Log.d("ProfileScreen", "User Loaded: ${userProfile.totalRatings}, ${userProfile.totalReviews}, ${userProfile.totalFollowers}")
            } else {
                Log.d("ProfileScreen", "User profile is null")
            }
        })

        profileViewModel.userObjObserve.observe(this, Observer { userObj ->
            if (userObj != null) {
                Log.d("ProfileScreen", "User Loaded: ${userObj.username}, ${userObj.email}")
            } else {
                Log.d("ProfileScreen", "User profile is null")
            }
        })

        val editProfileButton = findViewById<Button>(R.id.editProfileBtn)

        editProfileButton.setOnClickListener {
            val intent = Intent(this, ProfileEditScreen::class.java)
            startActivity(intent) // Navigate to ProfileScreen
            finish() // TODO REMOVE THIS LATER
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Profile screen onDestory log", "ProfileScreen Activity onDestroy() Called")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Lifecycle", "ProfileScreen: onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle", "ProfileScreen: onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle", "ProfileScreen: onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Lifecycle", "ProfileScreen: onStop() called")
    }
}