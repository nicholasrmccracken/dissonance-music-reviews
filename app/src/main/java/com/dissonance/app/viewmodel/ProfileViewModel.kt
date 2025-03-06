package com.dissonance.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dissonance.app.data.ProfileRepository
import com.dissonance.app.data.model.User
import com.dissonance.app.data.model.Profile


class ProfileViewModel : ViewModel() {
    // Notes:
    // LiveData is observable data holder that auto upd when change
    // MutableLiveData is writable version of LiveData can be changed, view model writes
    private val repository = ProfileRepository()

    // The profile obj
    private val userProfile = MutableLiveData<Profile?>()
    val userProfileObserve: LiveData<Profile?> get() = userProfile

    // The user obj
    private val userObj = MutableLiveData<User?>()
    val userObjObserve: LiveData<User?> get() = userObj

    fun fetchUserAndProfile(userId: String){
        repository.getUserAndProfile(userId) { user, profile ->
            userObj.value = user
            userProfile.value = profile
        }
    }
}