package com.dissonance.app

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dissonance.app.screens.AboutMeEditScreen
import com.dissonance.app.screens.ProfileEditScreen
import com.dissonance.app.screens.ProfileScreen
import com.dissonance.app.ui.login.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    // This rule ensures the activity is launched before tests run and cleaned up after
    @get:Rule
    val activityRule = ActivityScenarioRule(ProfileScreen::class.java)

    @Before
    fun setup() {
        // Initialize Intents before each test
        Intents.init()
    }

    @After
    fun cleanup() {
        // Release Intents after each test
        Intents.release()
    }

    @Test
    fun testUIElementsDisplayed() {
        // Check that all important UI elements are displayed
        onView(withId(R.id.username)).check(matches(isDisplayed()))
        onView(withId(R.id.numberOfRatings)).check(matches(isDisplayed()))
        onView(withId(R.id.numberOfReviews)).check(matches(isDisplayed()))
        onView(withId(R.id.numberOfFollowers)).check(matches(isDisplayed()))
        onView(withId(R.id.aboutMe)).check(matches(isDisplayed()))
        onView(withId(R.id.editProfileBtn)).check(matches(isDisplayed()))
        onView(withId(R.id.editAboutMe)).check(matches(isDisplayed()))
        onView(withId(R.id.logoutButton)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavigateToProfileEdit() {
        // Click edit profile button and verify navigation
        onView(withId(R.id.editProfileBtn)).perform(click())
        Intents.intended(hasComponent(ProfileEditScreen::class.java.name))
    }

    @Test
    fun testNavigateToAboutMeEdit() {
        // Click edit about me button and verify navigation
        onView(withId(R.id.editAboutMe)).perform(click())
        Intents.intended(hasComponent(AboutMeEditScreen::class.java.name))
    }

    @Test
    fun testLogout() {
        // Click logout button and verify navigation to login screen
        onView(withId(R.id.logoutButton)).perform(click())
        Intents.intended(hasComponent(LoginActivity::class.java.name))
    }

    @Test
    fun testStaticTextDisplayed() {
        // Check that static text labels are displayed correctly
        onView(withText("Ratings")).check(matches(isDisplayed()))
        onView(withText("Reviews")).check(matches(isDisplayed()))
        onView(withText("Followers")).check(matches(isDisplayed()))
        onView(withText("About Me")).check(matches(isDisplayed()))
        onView(withText("Recent Activity")).check(matches(isDisplayed()))
    }

    @Test
    fun testButtonTextDisplayed() {
        // Verify button text
        onView(withId(R.id.editProfileBtn)).check(matches(withText("Settings")))
        onView(withId(R.id.editAboutMe)).check(matches(withText("Edit About Me")))
        onView(withId(R.id.logoutButton)).check(matches(withText("Log Out")))
    }
}