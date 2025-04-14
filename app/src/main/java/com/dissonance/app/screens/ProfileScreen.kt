package com.dissonance.app.screens

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.dissonance.app.R
import com.dissonance.app.fragments.ReviewFragment
import com.dissonance.app.ui.login.LoginActivity
import com.dissonance.app.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.dissonance.app.viewmodel.SharedDiscogsViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale
import android.location.LocationManager
import com.dissonance.app.utils.NetworkUtils.isInternetAvailable

class ProfileScreen : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    private lateinit var editProfileButton: Button
    private lateinit var editAboutMeButton: Button
    private lateinit var aboutMeTextView: TextView
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

        aboutMeTextView = findViewById<TextView>(R.id.aboutMe)
        aboutMeTextView.ellipsize = TextUtils.TruncateAt.END
        aboutMeTextView.maxLines = 6
        editProfileButton = findViewById<Button>(R.id.editProfileBtn)
        editAboutMeButton = findViewById<Button>(R.id.editAboutMe)
        usernameTextView = findViewById<TextView>(R.id.username)
        totalRatingsTextView = findViewById<TextView>(R.id.numberOfRatings)
        totalReviewsTextView = findViewById<TextView>(R.id.numberOfReviews)
        totalFollowersTextView = findViewById<TextView>(R.id.numberOfFollowers)
        locationTextView = findViewById(R.id.locationText)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val locationFlag = sharedPreferences.getBoolean("SHOW_LOCATION", false)

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
                aboutMeTextView.text = "${userObj.aboutMe}"

            } else {
                Log.d("User", "User is null")
                usernameTextView.text = "Error"
                totalRatingsTextView.text = "Error"
                totalReviewsTextView.text = "Error"
                totalFollowersTextView.text = "Error"
            }
        })

        // If location flag from sharedpref set then fetch location (country) else make that dissapear
        // If location flag from sharedpref set then fetch location (country) else make that disappear
        if (locationFlag) {
            // First check if we have network connectivity before trying to get location
            if (isInternetAvailable(this)) {
                locationPermissionRequest.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                locationTextView.text = "Location unavailable - no network"
                Log.d("Location", "Cannot get location: No network connection")
            }
        } else {
            locationTextView.visibility = TextView.GONE
        }


        editProfileButton.setOnClickListener {
            val intent = Intent(this, ProfileEditScreen::class.java)
            startActivity(intent) // Navigate to ProfileScreen
        }

        editAboutMeButton.setOnClickListener {
            val intent = Intent(this, AboutMeEditScreen::class.java)
            startActivity(intent) // Navigate to edit about me
        }

        // Embedded review fragment stuff
        val reviewFragment = ReviewFragment().apply {
            arguments = Bundle().apply {
                user?.let {
                    putString("userId", user.uid)
                    putString("username", user.displayName)
                }
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView2, reviewFragment)
            .commit()

        val logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Log out the user
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Close ProfileScreen
        }

    }

    override fun onResume() {
        super.onResume()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            userViewModel.fetchUser(user.uid) // Refresh user data
        }

        val reviewFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2)
        if (reviewFragment is ReviewFragment) {
            reviewFragment.refresh()  // You'll define this function in ReviewFragment
        }
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getUserCountry()
        } else {
            Log.d("Location", "Permission denied")
            locationTextView.text = "Location Denied"
        }
    }

    // Function that gets user country
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
        // Check if location is enabled
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
            !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Location services are disabled
            locationTextView.text = "Location services disabled"
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