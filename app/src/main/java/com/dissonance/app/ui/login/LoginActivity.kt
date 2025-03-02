package com.dissonance.app.ui.login

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dissonance.app.R
import com.dissonance.app.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseApp

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

        FirebaseApp.initializeApp(this)

        // Check if Firebase initialized correctly
        if (FirebaseApp.getApps(this).isNotEmpty()) {
            Log.d("FirebaseCheck", "Firebase successfully initialized!")
        } else {
            Log.e("FirebaseCheck", "Firebase NOT initialized!")
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("Lifecycle", "LoginActivity: onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle", "LoginActivity: onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle", "LoginActivity: onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Lifecycle", "LoginActivity: onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Lifecycle", "LoginActivity: onDestroy() called")
    }
}
