package com.dissonance.app.screens

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dissonance.app.R
import com.dissonance.app.utils.NetworkUtils.isInternetAvailable
import com.dissonance.app.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class AboutMeEditScreen : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()

    private lateinit var backButton: Button
    private lateinit var submitButton: Button
    private lateinit var editAboutMe: EditText

    override fun onCreate(savedInstanceState: Bundle?) {

        val user = FirebaseAuth.getInstance().currentUser

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_about_me_edit)

        backButton = findViewById(R.id.backButton2)
        submitButton = findViewById(R.id.submitButton)
        editAboutMe = findViewById(R.id.editAboutMeText)

        backButton.setOnClickListener {
            val intent = Intent(this, ProfileScreen::class.java)
            startActivity(intent)
            finish()
        }

        submitButton.setOnClickListener {
            val text = editAboutMe.text.toString()
            if(text.isNotEmpty()){
                if(user != null){
                    if (isInternetAvailable(this)) {
                        userViewModel.updateUserAboutMe(user.uid, text)
                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        // Navigate back to profile screen after successful update
                        val intent = Intent(this, ProfileScreen::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "No internet connection available. Please try again when connected.", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "About Me cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}