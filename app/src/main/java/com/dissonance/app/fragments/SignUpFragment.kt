package com.dissonance.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dissonance.app.databinding.FragmentSignupBinding
import com.dissonance.app.screens.ProfileScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Lifecycle", "SignupFragment: onCreate()")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Lifecycle", "SignupFragment: onCreateView()")
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener {
            val displayName = binding.displayName.text.toString()
            val username = binding.username.text.toString()
            val email = binding.emailAddress.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()
            //val phoneNumber = binding.phoneNumber.text.toString()

            if (validateInputs(displayName, username, email, password, confirmPassword)) {
                // 🔥 Step 1: Create User with Firebase Authentication
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.let {
                                saveUserData(it.uid, displayName, username, email)
                            }
                            navigateToProfileScreen()
                        } else {
                            Toast.makeText(requireContext(), "Registration Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        // Handle Back to Login Button Click
        binding.backToLoginButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }


    // Validate User Inputs
    private fun validateInputs(
        firstName: String, lastName: String, email: String,
        password: String, confirmPassword: String
    ): Boolean {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
            password.isEmpty() || confirmPassword.isEmpty()
        ) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    // Navigate to ProfileScreen on Successful Sign Up
    private fun navigateToProfileScreen() {
        val intent = Intent(requireContext(), ProfileScreen::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("Lifecycle", "SignupFragment: onDestroyView()")
        _binding = null
    }

    private fun saveUserData(uid: String, displayName: String, username: String, email: String) {
        val db = FirebaseFirestore.getInstance()
        val userData = hashMapOf(
            "displayName" to displayName,
            "email" to email,
            "password" to "Fake_Pass",
            "spotifyId" to 0,
            "totalFollowers" to 0,
            "totalRatings" to 0,
            "totalReviews" to 0,
            "username" to username,
        )

        db.collection("users").document(uid).set(userData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "User Registered Successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error Saving Data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
