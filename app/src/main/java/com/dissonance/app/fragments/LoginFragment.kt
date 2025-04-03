package com.dissonance.app.ui.login


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dissonance.app.R
import com.dissonance.app.databinding.FragmentLoginBinding
import com.dissonance.app.fragments.SearchFragment
import com.dissonance.app.screens.ProfileScreen
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {


    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Lifecycle", "LoginFragment: onCreate()")


        auth = FirebaseAuth.getInstance()


        // Initialize ViewModel
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Lifecycle", "LoginFragment: onCreateView()")
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Handle Log In Button Click
        binding.loginButton.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()


            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter both username and password", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(username, password)
            }
        }


        // Handle Sign Up Button Click
        binding.signupButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SignupFragment())
                .addToBackStack(null)
                .commit()
        }


        // Observe Login Result
        loginViewModel.loginResult.observe(viewLifecycleOwner) { loginResult ->
            if (loginResult == null) return@observe


            // Show Error if Login Failed
            if (loginResult.error != null) {
                Toast.makeText(requireContext(), getString(loginResult.error), Toast.LENGTH_SHORT).show()
            }


            // Navigate to ProfileScreen if Login Successful
            if (loginResult.success != null) {
                Toast.makeText(requireContext(), "Welcome ${loginResult.success.displayName}", Toast.LENGTH_SHORT).show()
                navigateToProfileScreen()
            }
        }
    }


    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                    //navigateToSearchFragment()
                    navigateToProfileScreen()
                } else {
                    Toast.makeText(requireContext(), "Authentication Failed", Toast.LENGTH_LONG).show()
                }
            }
    }


    // Navigate to ProfileScreen on Successful Login
    private fun navigateToProfileScreen() {
        val intent = Intent(requireContext(), ProfileScreen::class.java)
        startActivity(intent)
        requireActivity().finish()
    }


    private fun navigateToSearchFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SearchFragment())
            .addToBackStack(null) // Optional if you want to allow back navigation
            .commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

