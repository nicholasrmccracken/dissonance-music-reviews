package com.dissonance.app.data.model

data class Review(
    val userId: String = "",
    val username: String = "",
    val albumId: String = "",
    val albumCoverUrl: String = "",
    val albumTitle: String = "",
    val artistName: String = "",
    val rating: Int = 0,
    val reviewTitle: String = "",
    val reviewText: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
