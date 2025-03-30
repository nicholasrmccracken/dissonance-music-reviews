package com.dissonance.app.data.model

data class Review(
    val userId: String = "",
    val albumId: String = "",
    val rating: Int = 0,
    val reviewTitle: String = "",
    val reviewText: String = "",
    val reviewArtist: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
