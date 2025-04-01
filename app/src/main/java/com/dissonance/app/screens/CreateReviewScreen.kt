package com.dissonance.app.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dissonance.app.R
import com.dissonance.app.fragments.WriteReviewFragment

class CreateReviewScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_review)

        val albumId = intent.getStringExtra("albumId") ?: ""
        val albumCoverUrl = intent.getStringExtra("albumCoverUrl") ?: ""
        val albumTitle = intent.getStringExtra("albumTitle") ?: "Album Title"
        val artistName = intent.getStringExtra("artistName") ?: "Artist Name"

        val writeReviewFragment = WriteReviewFragment.newInstance(
            albumId = albumId,
            albumCoverUrl = albumCoverUrl,
            albumTitle = albumTitle,
            artistName = artistName
        )

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, writeReviewFragment)
            .commit()
    }
}