package com.dissonance.app.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.dissonance.app.R
import com.dissonance.app.screens.ProfileScreen
import com.dissonance.app.screens.CreateReviewScreen
import com.dissonance.app.screens.HomeScreen

class NavbarFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_navbar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("Lifecycle", "NavbarFragment: onViewCreated() called")

        super.onViewCreated(view, savedInstanceState)

        val bottomNavbar = view.findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottomNavbar.selectedItemId = getCurrentMenuItemId()

        bottomNavbar.setOnItemSelectedListener { item ->
            val currentActivity = requireActivity()::class.java.simpleName

            when (item.itemId) {
                R.id.nav_home -> {
                    if (currentActivity != "HomeScreen") {
                    val intent = Intent(requireActivity(), HomeScreen::class.java)
                    startActivity(intent)
                    }
                    true
                }
                R.id.nav_search -> {
//                    if (currentActivity != "SearchScreen") {
//                    val intent = Intent(requireActivity(), SearchScreen::class.java)
//                    startActivity(intent)
//                    }
                    true
                }
                R.id.nav_create_review -> {
                    if (currentActivity != "CreateReviewScreen") {
                    val intent = Intent(requireActivity(), CreateReviewScreen::class.java)
                    startActivity(intent)
                    }
                    true
                }
                R.id.nav_charts -> {
//                    if (currentActivity != "ChartsScreen") {
//                    val intent = Intent(requireActivity(), ChartsScreen::class.java)
//                    startActivity(intent)
//                    }
                    true
                }
                R.id.nav_profile -> {
                    if (currentActivity != "ProfileScreen") {
                        val intent = Intent(requireActivity(), ProfileScreen::class.java)
                        startActivity(intent)
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("Lifecycle", "NavbarFragment: onDestroyView() called")
    }

    private fun getCurrentMenuItemId(): Int {
        val currentActivity = requireActivity()::class.java.simpleName
        return when (currentActivity) {
            "HomeScreen" -> R.id.nav_home
            "SearchScreen" -> R.id.nav_search
            "CreateReviewScreen" -> R.id.nav_create_review
            "ChartsScreen" -> R.id.nav_charts
            "ProfileScreen" -> R.id.nav_profile
            else -> R.id.nav_home
        }
    }
}