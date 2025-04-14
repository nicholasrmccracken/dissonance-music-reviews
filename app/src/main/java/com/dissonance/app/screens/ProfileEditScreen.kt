package com.dissonance.app.screens

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.viewModels
import com.dissonance.app.R
import com.dissonance.app.utils.NetworkUtils.isInternetAvailable
import com.dissonance.app.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileEditScreen : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var submitEmailButton: Button
    private lateinit var submitUsernameButton: Button
    private lateinit var backButton: Button
    private lateinit var toggleLocationSwitch: Switch


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Profile screen editor onCreate log", "ProfileScreenEditor Activity onCreate() Called")

        val user = FirebaseAuth.getInstance().currentUser

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile_edit)

        editName = findViewById(R.id.editNameBox)
        editEmail = findViewById(R.id.editEmailBox)
        submitEmailButton = findViewById(R.id.changeEmailButton)
        submitUsernameButton = findViewById(R.id.changeNameButton)
        backButton = findViewById(R.id.backButton)
        toggleLocationSwitch = findViewById(R.id.locationSwitch)
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE) // get shared pref
        val isLocationEnabled = sharedPreferences.getBoolean("SHOW_LOCATION", true) // Get SHOW_LOCATION from shared pref, true is incase it doesnt exist
        toggleLocationSwitch.isChecked = isLocationEnabled


        backButton.setOnClickListener {
            val intent = Intent(this, ProfileScreen::class.java)
            startActivity(intent)
            finish()
        }

        // Username update logic
        submitUsernameButton.setOnClickListener {
            val newName = editName.text.toString()
            if (newName.isNotEmpty()) {
                if (user != null) {
                    if (isInternetAvailable(this)) {
                        userViewModel.updateUserName(user.uid, newName)
                        Log.d("ProfileEditScreen", "Name updated to: $newName")
                        Toast.makeText(this, "Username updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "No internet connection available. Please try again when connected.", Toast.LENGTH_LONG).show()
                        Log.d("ProfileEditScreen", "Network connectivity issue - couldn't update username")
                    }
                } else {
                    Log.d("Fetch UID", "Fetch Current User UID Failure")
                    Toast.makeText(this, "Authentication error. Please log in again.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // Email update logic
        submitEmailButton.setOnClickListener {
            val newEmail = editEmail.text.toString()
            if (newEmail.isNotEmpty()) {
                if (user != null) {
                    if (isInternetAvailable(this)) {
                        userViewModel.updateUserEmail(user.uid, newEmail)
                        Log.d("ProfileEditScreen", "Email updated to: $newEmail")
                        Toast.makeText(this, "Email updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "No internet connection available. Please try again when connected.", Toast.LENGTH_LONG).show()
                        Log.d("ProfileEditScreen", "Network connectivity issue - couldn't update email")
                    }
                } else {
                    Log.d("Fetch UID", "Fetch Current User UID Failure")
                    Toast.makeText(this, "Authentication error. Please log in again.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // Save setting when switch is toggled
        toggleLocationSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("SHOW_LOCATION", isChecked).apply()
        }
    }
}