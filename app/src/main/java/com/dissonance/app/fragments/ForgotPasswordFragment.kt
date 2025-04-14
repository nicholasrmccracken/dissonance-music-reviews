package com.dissonance.app.ui.login

import AuthViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dissonance.app.R
import com.dissonance.app.utils.NetworkMonitor
import com.dissonance.app.utils.NetworkUtils

class ForgotPasswordFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        val emailInput = view.findViewById<EditText>(R.id.emailInput)
        val resetButton = view.findViewById<Button>(R.id.resetPasswordButton)
        val backButton = view.findViewById<Button>(R.id.backButton)

        NetworkMonitor.isConnected.observe(viewLifecycleOwner) { connected ->
            if (!connected) {
                Toast.makeText(requireContext(), "You're offline", Toast.LENGTH_SHORT).show()
            }
        }

        resetButton.setOnClickListener {
            if (!NetworkUtils.isInternetAvailable(requireContext())) {
                Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val email = emailInput.text.toString().trim()
            viewModel.sendPasswordReset(email)
        }

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewModel.resetStatus.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
    }
}
