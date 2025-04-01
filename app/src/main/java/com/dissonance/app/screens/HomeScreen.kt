package com.dissonance.app.screens

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dissonance.app.R
import com.dissonance.app.adapter.ReviewAdapter
import com.dissonance.app.viewmodel.ReviewViewModel

class HomeScreen : AppCompatActivity() {

    private lateinit var reviewAdapter: ReviewAdapter
    private val reviewViewModel: ReviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        reviewAdapter = ReviewAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.review_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = reviewAdapter

        reviewViewModel.reviewListObserve.observe(this) { reviews ->
            reviewAdapter.submitList(reviews)
        }

        reviewViewModel.getRecentReviews(20)
    }
}