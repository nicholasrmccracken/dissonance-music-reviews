package com.dissonance.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dissonance.app.R
import com.dissonance.app.databinding.ActivityLoginBinding
import com.dissonance.app.screens.ProfileScreen
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Lifecycle", "LoginActivity: onCreate()")

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load LoginFragment by default
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d("Lifecycle", "LoginActivity: onStart()")

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val intent = Intent(this, ProfileScreen::class.java)
            startActivity(intent)
            finish()
        }
    }



    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle", "LoginActivity: onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle", "LoginActivity: onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Lifecycle", "LoginActivity: onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Lifecycle", "LoginActivity: onDestroy()")
    }
}
