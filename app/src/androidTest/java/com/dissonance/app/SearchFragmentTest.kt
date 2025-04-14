package com.dissonance.app

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dissonance.app.fragments.SearchFragment
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.core.app.ActivityScenario
import com.dissonance.app.screens.SearchScreen


@RunWith(AndroidJUnit4::class)
class SearchFragmentTest {

    @Test
    fun searchFragment_inputFieldsAndSearchButton_navigatesToResults() {
        ActivityScenario.launch(SearchScreen::class.java)

        // Type text into artist and title fields
        onView(withId(R.id.artistInput))
            .perform(typeText("Radiohead"), closeSoftKeyboard())

        onView(withId(R.id.titleInput))
            .perform(typeText("OK Computer"), closeSoftKeyboard())

        // Click the search button
        onView(withId(R.id.searchButton))
            .perform(click())

        // Check if the results fragment container is displayed
        onView(withId(R.id.fragment_container))
            .check(matches(isDisplayed()))
    }
}
