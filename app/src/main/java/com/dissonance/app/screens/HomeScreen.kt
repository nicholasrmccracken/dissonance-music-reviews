package com.dissonance.app.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dissonance.app.R
import com.dissonance.app.adapter.ReviewAdapter
import com.dissonance.app.utils.NetworkUtils.isInternetAvailable
import com.dissonance.app.viewmodel.ReviewViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = null

        if (!isInternetAvailable(this)) {
            Toast.makeText(this, "Offline mode: showing cached data.", Toast.LENGTH_SHORT).show()
        }

        //            reviewViewModel.reviewListObserve.observe(this) { reviews ->
        //                reviewAdapter.submitList(reviews)
        //            }
        //            reviewViewModel.getRecentReviews(1000)

        lifecycleScope.launch {
            reviewViewModel.reviewFlow.collectLatest { pagingData ->
                reviewAdapter.submitData(pagingData)
            }
        }
    }
}