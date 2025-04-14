package com.dissonance.app.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dissonance.app.databinding.FragmentLoginBinding
import com.dissonance.app.screens.ProfileScreen
import com.dissonance.app.ui.login.ForgotPasswordFragment
import com.dissonance.app.ui.login.LoginViewModel
import com.dissonance.app.ui.login.LoginViewModelFactory
import com.dissonance.app.ui.login.SignupFragment
import com.dissonance.app.utils.NetworkMonitor
import com.dissonance.app.utils.NetworkUtils
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NetworkMonitor.isConnected.observe(viewLifecycleOwner) { connected ->
            if (!connected) {
                Toast.makeText(requireContext(), "You're offline", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginButton.setOnClickListener {
            if (!NetworkUtils.isInternetAvailable(requireContext())) {
                Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter both username and password", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(username, password)
            }
        }

        binding.signupButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(com.dissonance.app.R.id.fragment_container, SignupFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.forgotPasswordButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(com.dissonance.app.R.id.fragment_container, ForgotPasswordFragment())
                .addToBackStack(null)
                .commit()
        }

        loginViewModel.loginResult.observe(viewLifecycleOwner) { loginResult ->
            loginResult?.let {
                if (it.error != null) {
                    Toast.makeText(requireContext(), getString(it.error), Toast.LENGTH_SHORT).show()
                }
                if (it.success != null) {
                    Toast.makeText(requireContext(), "Welcome ${it.success.displayName}", Toast.LENGTH_SHORT).show()
                    navigateToProfileScreen()
                }
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navigateToProfileScreen()
                } else {
                    Toast.makeText(requireContext(), "Authentication Failed", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun navigateToProfileScreen() {
        val intent = Intent(requireContext(), ProfileScreen::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
