package com.dissonance.app.data

import com.dissonance.app.data.model.User
import com.dissonance.app.data.model.Profile

import com.google.firebase.firestore.FirebaseFirestore

class ProfileRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getUserProfile(userId: String, callback: (Profile?) -> Unit) {
        db.collection("users").document(userId).collection("profile")
            .document("profileData")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userProfile = document.toObject(Profile::class.java)
                    callback(userProfile)
                } else {
                    callback(null) // Profile not found
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun getUserAndProfile(userId: String, callback: (User?, Profile?) -> Unit) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { userDoc ->
                if (userDoc.exists()) {
                    val user = userDoc.toObject(User::class.java)

                    db.collection("users").document(userId)
                        .collection("profile").document("profileData")
                        .get()
                        .addOnSuccessListener { profileDoc ->
                            val userProfile = if (profileDoc.exists()) profileDoc.toObject(Profile::class.java) else null
                            callback(user, userProfile) // Pass both user and profile
                        }
                        .addOnFailureListener {
                            callback(user, null) // Failed to get profile
                        }
                } else {
                    callback(null, null) // User not found
                }
            }
            .addOnFailureListener {
                callback(null, null) // Failed to get user
            }
    }
}