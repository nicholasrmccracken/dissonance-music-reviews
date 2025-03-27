package com.dissonance.app.data

import android.util.Log
import com.dissonance.app.data.model.User

import com.google.firebase.firestore.FirebaseFirestore

// TODO you will need to have live data in repository and have viewmodel have observer observing the repository
class UserRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getUser(userId: String, callback: (User?) -> Unit) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userObj = document.toObject(User::class.java)
                    callback(userObj)
                } else {
                    Log.d("User Fire Store", "USER NOT EXISTS")
                    callback(null) // User not found
                }
            }
            .addOnFailureListener {
                Log.d("User Fire Store", "USER NOT EXISTS")
                callback(null)
            }
    }

    fun updateUserName(userId: String, newName: String, callback: (Boolean) -> Unit) {
        db.collection("users").document(userId)
            .update("username", newName)
            .addOnSuccessListener {
                Log.d("User Fire Store", "Username updated successfully")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.e("User Fire Store", "Error updating username", e)
                callback(false)
            }
    }

    fun updateUserEmail(userId: String, newEmail: String, callback: (Boolean) -> Unit) {
        db.collection("users").document(userId)
            .update("email", newEmail)
            .addOnSuccessListener {
                Log.d("User Fire Store", "Email updated successfully")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.e("User Fire Store", "Error updating email", e)
                callback(false)
            }
    }


}