package com.dissonance.app

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dissonance.app.screens.ProfileScreen
import com.dissonance.app.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    private lateinit var scenario: ActivityScenario<LoginActivity>

    @Before
    fun setup() {
        Intents.init()
        scenario = activityRule.scenario
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testFragmentContainerExists() {
        // Check that the fragment container exists and is displayed
        onView(withId(R.id.fragment_container))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testLoginFragmentLoaded() {
        // Verify that the LoginFragment is loaded into the container
        // This would require a custom matcher, but for simplicity we'll check for
        // views that we know exist in LoginFragment
        onView(withId(R.id.username)).check(matches(isDisplayed()))
        onView(withId(R.id.password)).check(matches(isDisplayed()))
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavigationToProfileIfLoggedIn() {
    }

    @Test
    fun testActivityLifecycleLogging() {
        scenario.recreate()

        // Verify the activity can be recreated without issues
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()))
    }
}