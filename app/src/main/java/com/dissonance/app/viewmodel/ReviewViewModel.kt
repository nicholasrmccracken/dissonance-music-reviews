package com.dissonance.app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dissonance.app.data.UserRepository
import com.dissonance.app.data.model.Review
import com.dissonance.app.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ReviewViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val reviewObj = MutableLiveData<Review?>()
    val reviewObjObserve: LiveData<Review?> get() = reviewObj

    // Function to fetch the review with the lowest timestamp for the given user
    fun getRecentReview(userId: String) {
        db.collection("reviews")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                val review = result.documents.firstOrNull()?.toObject(Review::class.java)
                reviewObj.value = review
                Log.d("getRecentReview", "Success")
            }
            .addOnFailureListener {
                // If the query fails, update LiveData with null
                reviewObj.value = null
                Log.d("getRecentReview", "Error")

            }
    }
}