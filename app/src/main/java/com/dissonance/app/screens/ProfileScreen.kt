package com.dissonance.app.screens

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import coil3.size.Scale
import com.dissonance.app.R
import com.dissonance.app.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.dissonance.app.viewmodel.SharedDiscogsViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.w3c.dom.Text
import java.util.Locale

class ProfileScreen : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private val discogViewModel: SharedDiscogsViewModel by viewModels()

    private lateinit var usernameTextView: TextView
    private lateinit var totalRatingsTextView: TextView
    private lateinit var totalReviewsTextView: TextView
    private lateinit var totalFollowersTextView: TextView
    private lateinit var topFourTextViews: List<TextView>
    private lateinit var topFourImageViews: List<ImageView>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationTextView: TextView
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001


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
        locationTextView = findViewById(R.id.locationText)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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

        // Request permission
        locationPermissionRequest.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)

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

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getUserCountry()
        } else {
            Log.d("Location", "Permission denied")
            locationTextView.text = "Location unavailable"
        }
    }

    private fun getUserCountry() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val country = addresses[0].countryName
                    locationTextView.text = country
                    Log.d("Location", "Country: $country")
                } else {
                    locationTextView.text = "Country not found"
                }
            } else {
                locationTextView.text = "Location not available"
            }
        }
    }
}