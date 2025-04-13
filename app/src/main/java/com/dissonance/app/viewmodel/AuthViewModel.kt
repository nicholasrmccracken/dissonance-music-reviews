import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val _resetStatus = MutableLiveData<String>()
    val resetStatus: LiveData<String> = _resetStatus

    fun sendPasswordReset(email: String) {
        if (email.isBlank()) {
            _resetStatus.value = "Email is required."
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                _resetStatus.value = "Password reset email sent."
            }
            .addOnFailureListener {
                _resetStatus.value = "Error: ${it.message}"
            }
    }
}
