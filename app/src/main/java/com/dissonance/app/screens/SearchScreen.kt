package com.dissonance.app.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dissonance.app.R
import com.dissonance.app.fragments.SearchFragment

class SearchScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SearchFragment())
            .commit()
    }
}