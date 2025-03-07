package com.dissonance.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dissonance.app.data.UserRepository
import com.dissonance.app.data.model.User


class UserViewModel : ViewModel() {
    // Notes:
    // LiveData is observable data holder that auto upd when change
    // MutableLiveData is writable version of LiveData can be changed, view model writes
    private val repository = UserRepository()


    // The user obj
    private val userObj = MutableLiveData<User?>()
    val userObjObserve: LiveData<User?> get() = userObj

    fun fetchUser(userId: String){
        repository.getUser(userId) { user ->
            userObj.value = user
        }
    }

    fun updateUserName(userId: String, newName: String) {
        repository.updateUserName(userId, newName) { success ->
            if (success) {
                userObj.value?.let { user ->
                    userObj.value = user.copy(username = newName) // Update LiveData with new name
                }
            }
        }
    }

    fun updateUserEmail(userId: String, newEmail: String) {
        repository.updateUserEmail(userId, newEmail) { success ->
            if (success) {
                userObj.value?.let { user ->
                    userObj.value = user.copy(email = newEmail) // Update LiveData with new email
                }
            }
        }
    }
}