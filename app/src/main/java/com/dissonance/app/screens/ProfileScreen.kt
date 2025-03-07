package com.dissonance.app.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dissonance.app.R
import com.dissonance.app.viewmodel.UserViewModel

class ProfileScreen : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    private lateinit var usernameTextView: TextView
    private lateinit var totalRatingsTextView: TextView
    private lateinit var totalReviewsTextView: TextView
    private lateinit var totalFollowersTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Profile screen onCreate log", "ProfileScreen Activity onCreate() Called")
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile) // Link to XML layout

        val editProfileButton = findViewById<Button>(R.id.editProfileBtn)
        usernameTextView = findViewById<TextView>(R.id.username)
        totalRatingsTextView = findViewById<TextView>(R.id.numberOfRatings)
        totalReviewsTextView = findViewById<TextView>(R.id.numberOfReviews)
        totalFollowersTextView = findViewById<TextView>(R.id.numberOfFollowers)

        // View Model Interactions
        userViewModel.fetchUser("testUser")
        userViewModel.userObjObserve.observe(this, Observer { userObj ->
            if (userObj != null) {
                Log.d("User", "User Loaded: ${userObj.username}, ${userObj.email}")

                usernameTextView.text = userObj.username
                totalRatingsTextView.text = "${userObj.totalRatings}"
                totalReviewsTextView.text = "${userObj.totalReviews}"
                totalFollowersTextView.text = "${userObj.totalFollowers}"
            } else {
                Log.d("User", "User is null")
                usernameTextView.text = "Error"
                totalRatingsTextView.text = "Error"
                totalReviewsTextView.text = "Error"
                totalFollowersTextView.text = "Error"
            }
        })

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