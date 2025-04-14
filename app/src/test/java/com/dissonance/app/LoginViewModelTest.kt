package com.dissonance.app.ui.login

import com.dissonance.app.data.LoginRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Assert.*
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.Rule

// This class mocks the Android Patterns class
class MockedPatterns {
    companion object {
        @JvmField
        val EMAIL_ADDRESS: java.util.regex.Pattern = java.util.regex.Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
    }
}

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    // Use InstantTaskExecutorRule to make LiveData work synchronously in tests
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var loginRepository: LoginRepository

    @Mock
    private lateinit var loginFormStateObserver: Observer<LoginFormState>

    @Mock
    private lateinit var loginResultObserver: Observer<LoginResult>

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        // Use PowerMockito to mock static classes if needed
        mockAndroidUtilPatterns()
        loginViewModel = LoginViewModel(loginRepository)

        // Observe LiveData
        loginViewModel.loginFormState.observeForever(loginFormStateObserver)
        loginViewModel.loginResult.observeForever(loginResultObserver)
    }

    private fun mockAndroidUtilPatterns() {
        // Setup a mocked environment for Patterns.EMAIL_ADDRESS
        try {
            val loginViewModelClass = LoginViewModel::class.java
            val field = loginViewModelClass.getDeclaredField("EMAIL_REGEX")
            field.isAccessible = true
            field.set(null, MockedPatterns.EMAIL_ADDRESS)
        } catch (e: Exception) {
            println("Could not mock Patterns.EMAIL_ADDRESS: ${e.message}")
        }
    }

    // Username validation tests
    @Test
    fun `isUserNameValid with non-email returns true`() {
        val result = loginViewModel.isUserNameValid("username")
        assertTrue("Non-empty username should return true", result)
    }

    @Test
    fun `isUserNameValid with complex non-email username returns true`() {
        val result = loginViewModel.isUserNameValid("user123_name")
        assertTrue("Complex username should return true", result)
    }

    @Test
    fun `isUserNameValid with empty string returns false`() {
        val result = loginViewModel.isUserNameValid("")
        assertFalse("Empty username should return false", result)
    }

    @Test
    fun `isUserNameValid with blank string returns false`() {
        val result = loginViewModel.isUserNameValid("   ")
        assertFalse("Blank username should return false", result)
    }

    @Test
    fun `isUserNameValid with special characters returns true if not blank`() {
        val result = loginViewModel.isUserNameValid("user#$%^&*")
        assertTrue("Username with special characters should return true if not blank", result)
    }

//    @Test
//    fun `isUserNameValid with email is true`() {
//        val result = loginViewModel.isUserNameValid("test@email.com")
//        assertTrue("Username with special characters should return true if not blank", result)
//    }

    // Password validation tests
    @Test
    fun `isPasswordValid with valid length returns true`() {
        val result = loginViewModel.isPasswordValid("password")
        assertTrue("Password with more than 5 characters should return true", result)
    }

    @Test
    fun `isPasswordValid with exact valid length returns true`() {
        val result = loginViewModel.isPasswordValid("123456")
        assertTrue("Password with exactly 6 characters should return true", result)
    }

    @Test
    fun `isPasswordValid with short password returns false`() {
        val result = loginViewModel.isPasswordValid("12345")
        assertFalse("Password with 5 or fewer characters should return false", result)
    }

    @Test
    fun `isPasswordValid with empty password returns false`() {
        val result = loginViewModel.isPasswordValid("")
        assertFalse("Empty password should return false", result)
    }

    @Test
    fun `isPasswordValid with only spaces returns false if too short`() {
        val result = loginViewModel.isPasswordValid("     ")
        assertFalse("Password with only spaces should return false if too short", result)
    }

    @Test
    fun `isPasswordValid with spaces but sufficient length returns true`() {
        val result = loginViewModel.isPasswordValid("      ")
        assertTrue("Password with spaces but sufficient length should return true", result)
    }
}