package com.dissonance.app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import coil3.size.Scale
import com.dissonance.app.R
import com.dissonance.app.viewmodel.ReviewViewModel
import com.dissonance.app.viewmodel.SharedDiscogsViewModel
import com.google.firebase.auth.FirebaseAuth

class ReviewFragment : Fragment() {

    private lateinit var reviewViewModel: ReviewViewModel
    private lateinit var discogViewModel: SharedDiscogsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reviewViewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)
        discogViewModel = ViewModelProvider(this).get(SharedDiscogsViewModel::class.java)

        val albumCover = view.findViewById<ImageView>(R.id.reviewImage)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val albumTitle = view.findViewById<TextView>(R.id.reviewTitle)
        val albumArtist = view.findViewById<TextView>(R.id.reviewArtist)
        val reviewContent = view.findViewById<TextView>(R.id.reviewContent)

        var recentReviewTitle = ""
        var recentReviewArtist = ""
        var recentReviewText = ""
        var rating = 0f

        // API Related temp storage var
        val user = FirebaseAuth.getInstance().currentUser

        reviewViewModel.reviewListObserve.observe(viewLifecycleOwner) { reviews ->
            val review = reviews.firstOrNull()
            if (review != null) {
                recentReviewTitle = review.reviewTitle
                recentReviewArtist = review.artistName
                recentReviewText = review.reviewText
                rating = review.rating.toFloat() / 2 // Assuming 1 - 10 thus we can get half stars in 5 stars
            } else {
                recentReviewTitle = "None"
                recentReviewArtist = "None"
                recentReviewText = "Make your first review!"
                rating = 0f
            }

            albumTitle.text = recentReviewTitle
            albumArtist.text = recentReviewArtist
            reviewContent.text = recentReviewText
            ratingBar.rating = rating

            // Call API search only after review data is available. Fixes concurrency issue.
            if (recentReviewTitle != "None" && recentReviewArtist != "None") {
                discogViewModel.searchAlbum(query = recentReviewTitle, artist = recentReviewArtist)
            }
        }

        refresh()

        discogViewModel.searchResults.observe(viewLifecycleOwner) { album ->
            albumCover.load(album.results[1].thumb){
                placeholder(android.R.drawable.ic_menu_report_image)
                crossfade(true)
                scale(Scale.FILL)
            }
        }
    }

    fun refresh() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            reviewViewModel.getRecentReviews(1, userId)
            Log.d("ReviewFragment", "Refreshing reviews for user: $userId")
        } else {
            Log.d("ReviewFragment", "ERROR: Unable to get current userId")
        }
    }
}