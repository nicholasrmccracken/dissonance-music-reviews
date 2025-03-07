package com.dissonance.app.data

import android.util.Log
import com.dissonance.app.data.model.User
import com.dissonance.app.data.model.Profile

import com.google.firebase.firestore.FirebaseFirestore

class ProfileRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getUserProfile(userId: String, callback: (Profile?) -> Unit) {
        db.collection("test_users").document(userId).collection("Profile")
            .document("ProfileID")
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
        db.collection("test_users").document(userId).get()
            .addOnSuccessListener { userDoc ->
                if (userDoc.exists()) {
                    val user = userDoc.toObject(User::class.java)
                    Log.d("Profile Fire Store", "DEBUG!!!")

                    db.collection("test_users").document(userId)
                        .collection("Profile").document("ProfileID")
                        .get()
                        .addOnSuccessListener { profileDoc ->
                            val userProfile = if (profileDoc.exists()) profileDoc.toObject(Profile::class.java) else null
                            Log.d("Profile Fire Store", "DEBUG!!!")
                            callback(user, userProfile) // Pass both user and profile
                        }
                        .addOnFailureListener {
                            Log.d("Profile Fire Store", "FAIL DEBUG!")
                            callback(user, null) // Failed to get profile
                        }
                } else {
                    Log.d("Profile Fire Store", "FAIL DEBUG!!")
                    callback(null, null) // User not found
                }
            }
            .addOnFailureListener {
                Log.d("Profile Fire Store", "FAIL DEBUG!!!123")
                callback(null, null) // Failed to get user
            }
    }
}