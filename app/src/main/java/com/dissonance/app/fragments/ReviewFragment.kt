package com.dissonance.app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import coil3.request.error
import coil3.size.Scale
import com.dissonance.app.R
import com.dissonance.app.viewmodel.ReviewViewModel
import com.dissonance.app.viewmodel.SharedDiscogsViewModel
import com.dissonance.app.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class ReviewFragment : Fragment() {

    private lateinit var reviewViewModel: ReviewViewModel
    private lateinit var discogViewModel: SharedDiscogsViewModel


    private var userId: String? = null
    private var username: String? = null  // Added username field

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString("userId")
        }
    }

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
        val usernameTextView = view.findViewById<TextView>(R.id.usernameTextView) // Assume there's a TextView in your layout for this


        reviewViewModel.reviewListObserve.observe(viewLifecycleOwner) { reviews ->
            val review = reviews.firstOrNull()
            if (review != null) {
                albumTitle.text = review.reviewTitle
                albumArtist.text = review.artistName
                reviewContent.text = review.reviewText
                ratingBar.rating = review.rating.toFloat() / 2 // Normalize rating
                usernameTextView.text = review.username

            } else {
                albumTitle.text = "None"
                albumArtist.text = "None"
                reviewContent.text = "Make your first review!"
                ratingBar.rating = 0f
            }

            // Call API search only after review data is available
            if (albumTitle.text != "None" && albumArtist.text != "None") {
                discogViewModel.searchAlbum(query = albumTitle.text.toString(), artist = albumArtist.text.toString())
            }
        }

        refresh()

        discogViewModel.searchResults.observe(viewLifecycleOwner) { album ->
            albumCover.load(album.results[0].thumb) {
                placeholder(android.R.drawable.ic_menu_report_image)
                error(R.drawable.album_placeholder)
                crossfade(true)
                scale(Scale.FILL)
            }
        }
    }

    fun refresh() {
        if (userId != null) {
            reviewViewModel.getRecentReviews(1, userId!!)
            Log.d("ReviewFragment", "Refreshing reviews for user: $userId with username: $username")
        } else {
            Log.d("ReviewFragment", "ERROR: Unable to get current userId")
        }
    }
}
