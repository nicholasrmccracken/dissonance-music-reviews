package com.dissonance.app.screens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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


//package com.dissonance.app.screens
//
//import android.os.Bundle
//import android.util.Log
//import android.widget.TextView
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.Observer
//import com.dissonance.app.R
//import com.dissonance.app.viewmodel.ProfileViewModel
//
//class ProfileScreen : AppCompatActivity() {
//    private val profileViewModel: ProfileViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        supportActionBar?.hide()
//        setContentView(R.layout.activity_profile)
//
//        // Reference to UI elements
//        val userNameTextView = findViewById<TextView>(R.id.userNameTextView)
//        val userEmailTextView = findViewById<TextView>(R.id.userEmailTextView)
//
//        // Replace with actual user ID (e.g., from logged-in session)
//        val userId = "user_123"
//
//        // Fetch user profile
//        profileViewModel.fetchUserProfile(userId)
//
//        // Observe the LiveData
//        profileViewModel.userProfileObserve.observe(this, Observer { userProfile ->
//            if (userProfile != null) {
//                userNameTextView.text = userProfile.name
//                userEmailTextView.text = userProfile.email
//                Log.d("ProfileScreen", "User Loaded: ${userProfile.name}, ${userProfile.email}")
//            } else {
//                userNameTextView.text = "User not found"
//                userEmailTextView.text = ""
//                Log.d("ProfileScreen", "User profile is null")
//            }
//        })
//    }
//}