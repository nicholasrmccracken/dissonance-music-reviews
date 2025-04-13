//package com.dissonance.app.viewmodel
//
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.dissonance.app.data.UserRepository
//import com.dissonance.app.data.model.Review
//import com.dissonance.app.data.model.User
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.Query
//
//class ReviewViewModel: ViewModel() {
//
//    private val db = FirebaseFirestore.getInstance()
//    private val reviewList = MutableLiveData<List<Review>>()
//    val reviewListObserve: LiveData<List<Review>> get() = reviewList
//
//    // Function to fetch the review with the lowest timestamp for the given user
//    fun getRecentReviews(limit: Int, userId: String? = null) {
//        var query = db.collection("reviews")
//            .orderBy("timestamp", Query.Direction.DESCENDING)
//            .limit(limit.toLong())
//
//        if (!userId.isNullOrBlank()) {
//            query = query.whereEqualTo("userId", userId)
//        }
//
//        query.get()
//            .addOnSuccessListener { result ->
//                val reviews = result.documents.mapNotNull { it.toObject(Review::class.java) }
//                reviewList.value = reviews
//                Log.d("getRecentReview", "Fetched ${reviews.size} reviews")
//            }
//            .addOnFailureListener {
//                reviewList.value = emptyList()
//                Log.d("getRecentReview", "Error fetching reviews")
//            }
//    }
//}

package com.dissonance.app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dissonance.app.data.model.Review
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.Date

class ReviewViewModel(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val reviewList = MutableLiveData<List<Review>>()
    val reviewListObserve: LiveData<List<Review>> get() = reviewList

    // Function to fetch the review with the lowest timestamp for the given user
    fun getRecentReviews(limit: Int, userId: String? = null) {
        var query = db.collection("reviews")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(limit.toLong())

        if (!userId.isNullOrBlank()) {
            query = query.whereEqualTo("userId", userId)
        }

        query.get()
            .addOnSuccessListener { result ->
                val reviews = result.documents.mapNotNull { it.toObject(Review::class.java) }
                reviewList.value = reviews
                Log.d("getRecentReview", "Fetched ${reviews.size} reviews")
            }
            .addOnFailureListener {
                reviewList.value = emptyList()
                Log.d("getRecentReview", "Error fetching reviews")
            }
    }

    fun validateReview(review: Review): Boolean {
        // Check if required fields are present
        if (review.userId.isNullOrBlank() || review.username.isNullOrBlank()) {
            return false
        }

        // Validate content length max 9000
        if (review.reviewText.length > 9000) {
            return false
        }

        // Validate rating is between 1-5
        if (review.rating < 1 || review.rating > 10) {
            return false
        }

        return true
    }

    fun calculateAverageRating(reviews: List<Review>): Float {
        if (reviews.isEmpty()) {
            return 0.0f
        }

        val sum = reviews.sumOf { it.rating.toDouble() }
        return (sum / reviews.size).toFloat()
    }
}