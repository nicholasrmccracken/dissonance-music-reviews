package com.dissonance.app.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class User(
    val email: String = "",
    val displayName: String = "",
    val password: String = "",
    val spotifyId: Int = 0,
    val userId: Int = 0,
    val username: String = ""
)