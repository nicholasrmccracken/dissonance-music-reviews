package com.dissonance.app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import coil3.size.Scale
import com.dissonance.app.R
import com.dissonance.app.data.model.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class WriteReviewFragment : Fragment() {
    private lateinit var albumCoverImage: ImageView
    private lateinit var albumTitleText: TextView
    private lateinit var artistNameText: TextView
    private lateinit var ratingInput: EditText
    private lateinit var reviewTitleInput: EditText
    private lateinit var reviewTextInput: EditText
    private lateinit var publishButton: Button

    private lateinit var albumId: String
    private lateinit var albumCoverUrl: String
    private lateinit var albumTitle: String
    private lateinit var artistName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_write_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        albumId = requireArguments().getString("albumId") ?: ""
        albumCoverUrl = requireArguments().getString("albumCoverUrl") ?: ""
        albumTitle = requireArguments().getString("albumTitle") ?: "Album Title"
        artistName = requireArguments().getString("artistName") ?: "Artist Name"

        albumCoverImage = view.findViewById(R.id.album_cover)
        albumTitleText = view.findViewById(R.id.album_title)
        artistNameText = view.findViewById(R.id.artist_name)
        ratingInput = view.findViewById(R.id.rating_input)
        reviewTitleInput = view.findViewById(R.id.review_title_input)
        reviewTextInput = view.findViewById(R.id.review_text_input)
        publishButton = view.findViewById(R.id.publish_review_button)

        albumTitleText.text = albumTitle
        artistNameText.text = artistName
        albumCoverImage.load(albumCoverUrl){
            placeholder(R.drawable.album_placeholder)
            crossfade(true)
            scale(Scale.FILL)
        }

        publishButton.setOnClickListener {
            publishReview()
        }
    }

    private fun publishReview() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val rating = ratingInput.text.toString().toIntOrNull()
        val reviewTitle = reviewTitleInput.text.toString().trim()
        val reviewText = reviewTextInput.text.toString().trim()

        if (rating == null || rating !in 1..10) {
            Toast.makeText(requireContext(), "Rating must be between 1 and 10.", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(requireContext(), "Review published!", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
                val userRef = db.collection("users").document(userId)
                userRef.update("totalReviews", com.google.firebase.firestore.FieldValue.increment(1))
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to publish review: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    companion object {
        fun newInstance(albumId: String, albumCoverUrl: String, albumTitle: String, artistName: String): WriteReviewFragment {
            val fragment = WriteReviewFragment()
            fragment.arguments = Bundle().apply {
                putString("albumId", albumId)
                putString("albumCoverUrl", albumCoverUrl)
                putString("albumTitle", albumTitle)
                putString("artistName", artistName)
            }
            return fragment
        }
    }
}