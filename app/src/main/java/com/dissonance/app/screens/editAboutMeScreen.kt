package com.dissonance.app.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.dissonance.app.R
import com.dissonance.app.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class editAboutMeScreen : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()

    lateinit var backButton: Button
    lateinit var submitButton: Button
    lateinit var editAboutMe: EditText



    override fun onCreate(savedInstanceState: Bundle?) {

        val user = FirebaseAuth.getInstance().currentUser

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_aboutme)

        backButton = findViewById<Button>(R.id.backButton2)
        submitButton = findViewById<Button>(R.id.submitButton)
        editAboutMe = findViewById<EditText>(R.id.editAboutMe)

        backButton.setOnClickListener {
            val intent = Intent(this, ProfileScreen::class.java)
            startActivity(intent)
            finish()
        }

        submitButton.setOnClickListener {
            val text = editAboutMe.text.toString()
            if(text.isNotEmpty()){
                if(user != null){
                    userViewModel.updateUserAboutMe(user.uid, text)
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