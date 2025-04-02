package com.dissonance.app.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dissonance.app.R
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

        backButton = findViewById<Button>(R.id.backButton2)
        submitButton = findViewById<Button>(R.id.submitButton)
        editAboutMe = findViewById<EditText>(R.id.editAboutMeText)

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
}