package com.dissonance.app

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.dissonance.app.fragments.LoginFragment
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    @Test
    fun testLoginFragmentViewsDisplayed() {
        // Launch the fragment with no theme specified
        launchFragmentInContainer<LoginFragment>(themeResId = R.style.Theme_Dissonance)

        // Check if username input is displayed
        onView(withId(R.id.username))
            .check(matches(isDisplayed()))

        // Check if password input is displayed
        onView(withId(R.id.password))
            .check(matches(isDisplayed()))

        // Check if login button is displayed
        onView(withId(R.id.loginButton))
            .check(matches(isDisplayed()))

        // Check if signup button is displayed
        onView(withId(R.id.signupButton))
            .check(matches(isDisplayed()))
    }
}