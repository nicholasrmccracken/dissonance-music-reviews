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

class ForgotPasswordFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        val emailInput = view.findViewById<EditText>(R.id.emailInput)
        val resetButton = view.findViewById<Button>(R.id.resetPasswordButton)

        resetButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            viewModel.sendPasswordReset(email)
        }

        val backButton = view.findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }


        viewModel.resetStatus.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
    }
}
