package com.dissonance.app.viewmodel

import android.app.appsearch.SearchResult
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dissonance.app.data.model.DiscogSearchModel
import com.dissonance.app.singletons.RetrofitClient
import kotlinx.coroutines.launch

class SharedDiscogsViewModel() : ViewModel() {
    // Specifically for discog database search results
    private val _searchResults = MutableLiveData<DiscogSearchModel>()
    val searchResults: LiveData<DiscogSearchModel> = _searchResults

    fun searchMusic(query: String){
        viewModelScope.launch {
            try {
                val results = RetrofitClient.DiscogsApi.searchMusic(query)
                _searchResults.value = results
            } catch (e: Exception) {
                Log.d("SharedDiscogVM", "Error occured when invoking search music")
            }
        }
    }

    // Function to search album with more detailed parameters
    fun searchAlbum(
        query: String,
        artist: String,
        title: String? = null,
        year: String? = null,
        genre: String? = null,
        label: String? = null
    ) {
        viewModelScope.launch {
            try {
                val results = RetrofitClient.DiscogsApi.searchAlbumn(
                    query = query,
                    artist = artist,
                    title = title,
                    year = year,
                    genre = genre,
                    label = label
                )
                _searchResults.value = results
            } catch (e: Exception) {
                Log.d("SharedDiscogVM", "Error occured when invoking search albumn")
            }
        }
    }
}