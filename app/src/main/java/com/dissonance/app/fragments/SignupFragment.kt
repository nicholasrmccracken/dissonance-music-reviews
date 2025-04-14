package com.dissonance.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dissonance.app.databinding.FragmentSignupBinding
import com.dissonance.app.screens.ProfileScreen
import com.dissonance.app.utils.NetworkMonitor
import com.dissonance.app.utils.NetworkUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val auth = FirebaseAuth.getInstance()

        NetworkMonitor.isConnected.observe(viewLifecycleOwner) { connected ->
            if (!connected) {
                Toast.makeText(requireContext(), "You're offline", Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerButton.setOnClickListener {
            if (!NetworkUtils.isInternetAvailable(requireContext())) {
                Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val displayName = binding.displayName.text.toString()
            val username = binding.username.text.toString()
            val email = binding.emailAddress.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()

            if (validateInputs(displayName, username, email, password, confirmPassword)) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.sendEmailVerification()
                                ?.addOnSuccessListener {
                                    Toast.makeText(requireContext(), "Verification email sent to $email", Toast.LENGTH_LONG).show()
                                }
                                ?.addOnFailureListener { error ->
                                    Toast.makeText(requireContext(), "Failed to send verification email: ${error.message}", Toast.LENGTH_LONG).show()
                                }

                            user?.uid?.let {
                                saveUserData(it, displayName, username, email)
                            }

                            auth.signOut()
                            Toast.makeText(requireContext(), "Please verify your email before logging in.", Toast.LENGTH_LONG).show()
                            parentFragmentManager.popBackStack()
                        } else {
                            Toast.makeText(requireContext(), "Registration Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        binding.backToLoginButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun validateInputs(firstName: String, lastName: String, email: String, password: String, confirmPassword: String): Boolean {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
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
            "aboutMe" to "..."
        )

        db.collection("users").document(uid).set(userData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "User Registered Successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error Saving Data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
