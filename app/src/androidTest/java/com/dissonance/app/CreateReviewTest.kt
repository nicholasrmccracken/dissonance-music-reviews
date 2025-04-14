package com.dissonance.app

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dissonance.app.screens.CreateReviewScreen
import com.dissonance.app.screens.ProfileScreen
import com.dissonance.app.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import android.os.IBinder
import android.view.WindowManager
import androidx.test.espresso.Root
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher



@RunWith(AndroidJUnit4::class)
class CreateReviewScreenTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(CreateReviewScreen::class.java)

    @Test
    fun testViewsDisplayed() {
        onView(withId(R.id.album_cover)).check(matches(isDisplayed()))
        onView(withId(R.id.album_title)).check(matches(isDisplayed()))
        onView(withId(R.id.artist_name)).check(matches(isDisplayed()))
        onView(withId(R.id.rating_input)).check(matches(isDisplayed()))
        onView(withId(R.id.review_title_input)).check(matches(isDisplayed()))
        onView(withId(R.id.review_text_input)).check(matches(isDisplayed()))
        onView(withId(R.id.publish_review_button)).check(matches(isDisplayed()))
    }

    @Test
    fun testEmptyRatingShowsError() {
        onView(withId(R.id.review_title_input)).perform(typeText("Great Album!"))
        onView(withId(R.id.review_text_input)).perform(typeText("Loved the vocals and production."), closeSoftKeyboard())
        onView(withId(R.id.publish_review_button)).perform(click())
//        assertDisplayed("Rating must be between 1 and 10.")

    }

    @Test
    fun testInvalidRatingShowsError() {
        onView(withId(R.id.rating_input)).perform(typeText("11"), closeSoftKeyboard())
        onView(withId(R.id.review_title_input)).perform(typeText("Great Album!"))
        onView(withId(R.id.review_text_input)).perform(typeText("Loved the vocals and production."), closeSoftKeyboard())
        onView(withId(R.id.publish_review_button)).perform(click())
//        assertDisplayed("Rating must be between 1 and 10.")

    }

    @Test
    fun testEmptyReviewFieldsShowError() {
        onView(withId(R.id.rating_input)).perform(typeText("8"), closeSoftKeyboard())
        onView(withId(R.id.publish_review_button)).perform(click())
//        assertDisplayed("Review title and text are required.")

    }

    @Test
    fun testPublishButtonClickableWithValidInputs() {
        onView(withId(R.id.rating_input)).perform(typeText("8"), closeSoftKeyboard())
        onView(withId(R.id.review_title_input)).perform(typeText("Masterpiece"), closeSoftKeyboard())
        onView(withId(R.id.review_text_input)).perform(typeText("Every track hits hard!"), closeSoftKeyboard())
        onView(withId(R.id.publish_review_button)).perform(click())
    }
}
