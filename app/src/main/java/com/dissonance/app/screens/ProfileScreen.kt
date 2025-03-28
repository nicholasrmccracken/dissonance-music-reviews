package com.dissonance.app.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import coil3.size.Scale
import com.dissonance.app.R
import com.dissonance.app.data.model.DiscogSearchModel
import com.dissonance.app.data.model.ReleaseResult
import com.dissonance.app.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.dissonance.app.singletons.RetrofitClient
import com.dissonance.app.viewmodel.SharedDiscogsViewModel
import org.w3c.dom.Text

class ProfileScreen : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private val discogViewModel: SharedDiscogsViewModel by viewModels()

    private lateinit var usernameTextView: TextView
    private lateinit var totalRatingsTextView: TextView
    private lateinit var totalReviewsTextView: TextView
    private lateinit var totalFollowersTextView: TextView
//    private lateinit var topFourTextView1: TextView
//    private lateinit var topFourTextView2: TextView
//    private lateinit var topFourTextView3: TextView
//    private lateinit var topFourTextView4: TextView
    private lateinit var topFourTextViews: List<TextView>
    private lateinit var topFourImageViews: List<ImageView>




    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Profile screen onCreate log", "ProfileScreen Activity onCreate() Called")
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile) // Link to XML layout

        val editProfileButton = findViewById<Button>(R.id.editProfileBtn)
        usernameTextView = findViewById<TextView>(R.id.username)
        totalRatingsTextView = findViewById<TextView>(R.id.numberOfRatings)
        totalReviewsTextView = findViewById<TextView>(R.id.numberOfReviews)
        totalFollowersTextView = findViewById<TextView>(R.id.numberOfFollowers)

        topFourTextViews = listOf(
            findViewById<TextView>(R.id.topFourText1),
            findViewById<TextView>(R.id.topFourText2),
            findViewById<TextView>(R.id.topFourText3),
            findViewById<TextView>(R.id.topFourText4)
        )

        topFourImageViews = listOf(
            findViewById<ImageView>(R.id.topFourImage1),
            findViewById<ImageView>(R.id.topFourImage2),
            findViewById<ImageView>(R.id.topFourImage3),
            findViewById<ImageView>(R.id.topFourImage4)
        )

        // TODO REMOVE LATER WITH PULLED DATA FROM FIRESTORE
        val albumsToSearch = listOf(
            "good kid, m.A.A.d city" to "Kendrick Lamar",
            "The Anthology" to "A Tribe Called Quest",
            "Illmatic" to "Nas",
            "Starz" to "Yung Lean"
        )

        // API Related temp storage var
        val user = FirebaseAuth.getInstance().currentUser

        // View Model Interactions
        if (user != null) {
            userViewModel.fetchUser(user.uid)
        } else {
            Log.d("Fetch UID", "Fetch Current User UID Failure")
        }
        userViewModel.userObjObserve.observe(this, Observer { userObj ->
            if (userObj != null) {
                Log.d("User", "User Loaded: ${userObj.username}, ${userObj.email}")

                usernameTextView.text = userObj.username
                totalRatingsTextView.text = "${userObj.totalRatings}"
                totalReviewsTextView.text = "${userObj.totalReviews}"
                totalFollowersTextView.text = "${userObj.totalFollowers}"
            } else {
                Log.d("User", "User is null")
                usernameTextView.text = "Error"
                totalRatingsTextView.text = "Error"
                totalReviewsTextView.text = "Error"
                totalFollowersTextView.text = "Error"
            }
        })

        // Observer for discog viewmodel
        // TODO possibly make some changes to the ? or req or no req for such field
        discogViewModel.batchSearchResults.observe(this) { results ->
            albumsToSearch.forEachIndexed { index, (title, artist) ->
                // Find the search result for the current album
                val searchResult = results[Pair(title, artist)]

                topFourTextViews[index].text = searchResult
                    ?.results
                    ?.firstOrNull()
                    ?.title
                    ?: "Not Found"

                topFourImageViews[index].load(searchResult?.results?.firstOrNull()?.thumb) {
                    placeholder(android.R.drawable.ic_menu_report_image)
                    crossfade(true)
                    scale(Scale.FILL)
                }
            }
        }

        discogViewModel.searchAlbums(albumsToSearch)

        editProfileButton.setOnClickListener {
            val intent = Intent(this, ProfileEditScreen::class.java)
            startActivity(intent) // Navigate to ProfileScreen
            finish() // TODO REMOVE THIS LATER
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Profile screen onDestory log", "ProfileScreen Activity onDestroy() Called")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Lifecycle", "ProfileScreen: onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle", "ProfileScreen: onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle", "ProfileScreen: onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Lifecycle", "ProfileScreen: onStop() called")
    }
}