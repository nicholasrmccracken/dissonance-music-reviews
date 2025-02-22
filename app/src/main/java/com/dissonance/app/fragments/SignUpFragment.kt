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

        // Handle Register Button Click
        binding.registerButton.setOnClickListener {
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val email = binding.emailAddress.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()
            val phoneNumber = binding.phoneNumber.text.toString()

            if (validateInputs(firstName, lastName, email, password, confirmPassword, phoneNumber)) {
                navigateToProfileScreen()
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
        password: String, confirmPassword: String, phoneNumber: String
    ): Boolean {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
            password.isEmpty() || confirmPassword.isEmpty() || phoneNumber.isEmpty()
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
}
