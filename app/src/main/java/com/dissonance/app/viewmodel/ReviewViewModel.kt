package com.dissonance.app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.dissonance.app.data.model.Review
import com.dissonance.app.data.ReviewPagingSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ReviewViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
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

    val reviewFlow = Pager(PagingConfig(pageSize = 25)) {
        ReviewPagingSource(db)
    }.flow.cachedIn(viewModelScope)
}