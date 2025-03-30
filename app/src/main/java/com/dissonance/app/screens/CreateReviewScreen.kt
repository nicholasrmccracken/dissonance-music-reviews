package com.dissonance.app.screens

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dissonance.app.R
import com.dissonance.app.data.model.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateReviewScreen : AppCompatActivity() {

    private lateinit var albumCoverImage: ImageView
    private lateinit var albumTitleText: TextView
    private lateinit var artistNameText: TextView
    private lateinit var ratingInput: EditText
    private lateinit var reviewTitleInput: EditText
    private lateinit var reviewTextInput: EditText
    private lateinit var publishButton: Button

    private lateinit var albumId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_review)

        albumCoverImage = findViewById(R.id.album_cover)
        albumTitleText = findViewById(R.id.album_title)
        artistNameText = findViewById(R.id.artist_name)
        ratingInput = findViewById(R.id.rating_input)
        reviewTitleInput = findViewById(R.id.review_title_input)
        reviewTextInput = findViewById(R.id.review_text_input)
        publishButton = findViewById(R.id.publish_review_button)

        albumId = intent.getStringExtra("albumId") ?: ""
        val albumCoverUrl = intent.getStringExtra("albumImageUrl") ?: ""
        val albumTitle = intent.getStringExtra("albumTitle") ?: "Album Title"
        val artistName = intent.getStringExtra("artistName") ?: "Artist Name"

        albumTitleText.text = albumTitle
        artistNameText.text = artistName
        // TODO: Show album cover image using GLIDE

        publishButton.setOnClickListener {
            publishReview()
        }
    }

    private fun publishReview() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "You must be signed in to post a review.", Toast.LENGTH_SHORT).show()
            return
        }

        val rating = ratingInput.text.toString().toIntOrNull()
        val reviewTitle = reviewTitleInput.text.toString().trim()
        val reviewText = reviewTextInput.text.toString().trim()

        // TODO Make it 1 to 5
        if (rating == null || rating < 1 || rating > 10) {
            Toast.makeText(this, "Rating must be between 1 and 10.", Toast.LENGTH_SHORT).show()
            return
        }

        val artistName = artistNameText.text.toString()

        val review = Review(
            userId = userId,
            albumId = albumId,
            rating = rating,
            reviewTitle = reviewTitle,
            reviewText = reviewText,
            reviewArtist = artistName
        )

        val db = FirebaseFirestore.getInstance()

        db.collection("reviews")
            .add(review)
            .addOnSuccessListener {
                Toast.makeText(this, "Review published!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                val errorMessage = "Failed to publish review: ${it.message}"
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                android.util.Log.e("CreateReviewScreen", errorMessage, it)
            }
    }
}