package com.dissonance.app.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import com.dissonance.app.R
import com.dissonance.app.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileEditScreen : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var submitEmailButton: Button
    private lateinit var submitUsernameButton: Button
    private lateinit var backButton: Button
    private lateinit var discogsDebugButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Profile screen editor onCreate log", "ProfileScreenEditor Activity onCreate() Called")

        val user = FirebaseAuth.getInstance().currentUser

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile_editor)

        editName = findViewById(R.id.editNameBox)
        editEmail = findViewById(R.id.editEmailBox)
        submitEmailButton = findViewById(R.id.changeEmailButton)
        submitUsernameButton = findViewById(R.id.changeNameButton)
        backButton = findViewById(R.id.backButton)

        // TODO Remove later as this is for debug for discogs
        discogsDebugButton = findViewById(R.id.deugButton)

        backButton.setOnClickListener {
            val intent = Intent(this, ProfileScreen::class.java)
            startActivity(intent)
            finish()
        }

        discogsDebugButton.setOnClickListener {
            val intent = Intent(this, discogsTest::class.java)
            startActivity(intent)
        }

        // Username update logic
        submitUsernameButton.setOnClickListener {
            val newName = editName.text.toString()
            if (newName.isNotEmpty()) {
                // userId = "testUser2"
                if (user != null) {
                    userViewModel.updateUserName(user.uid, newName)
                    Log.d("ProfileEditScreen", "Name updated to: $newName")
                } else {
                    Log.d("Fetch UID", "Fetch Current User UID Failure")
                }
            }
        }

        // Email update logic
        submitEmailButton.setOnClickListener {
            val newEmail = editEmail.text.toString()
            if (newEmail.isNotEmpty()) {
                // val userId = "testUser2"
                if (user != null) {
                    userViewModel.updateUserEmail(user.uid, newEmail)
                    Log.d("ProfileEditScreen", "Email updated to: $newEmail")
                } else {
                    Log.d("Fetch UID", "Fetch Current User UID Failure")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Profile screen editor onDestory log", "ProfileScreenEditor Activity onDestroy() Called")
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