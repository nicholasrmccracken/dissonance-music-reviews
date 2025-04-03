package com.dissonance.app.fragments

import android.os.Bundle
import android.text.TextUtils
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

class ReviewFragment : Fragment() {

    private lateinit var reviewViewModel: ReviewViewModel


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

        val albumCover = view.findViewById<ImageView>(R.id.reviewImage)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val albumReviewTitle = view.findViewById<TextView>(R.id.reviewTitle)
        albumReviewTitle.ellipsize = TextUtils.TruncateAt.END

        val albumTitle = view.findViewById<TextView>(R.id.reviewAlbumTitle)
        albumTitle.ellipsize = TextUtils.TruncateAt.END

        val reviewContent = view.findViewById<TextView>(R.id.reviewContent)
        reviewContent.ellipsize = TextUtils.TruncateAt.END

        val usernameTextView = view.findViewById<TextView>(R.id.usernameTextView) // Assume there's a TextView in your layout for this


        reviewViewModel.reviewListObserve.observe(viewLifecycleOwner) { reviews ->
            val review = reviews.firstOrNull()
            if (review != null) {
                albumTitle.text = review.albumTitle
                albumReviewTitle.text = review.reviewTitle
                reviewContent.text = review.reviewText
                ratingBar.rating = review.rating.toFloat() / 2 // Normalize rating
                usernameTextView.text = "@${review.username}"
                albumCover.load(review.albumCoverUrl) {
                    placeholder(android.R.drawable.ic_menu_report_image)
                    error(R.drawable.album_placeholder)
                    crossfade(true)
                    scale(Scale.FILL)
                }

            } else {
                albumTitle.text = "None"
                albumReviewTitle.text = "None"
                reviewContent.text = "Make your first review!"
                ratingBar.rating = 0f
            }
        }

        refresh()
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
