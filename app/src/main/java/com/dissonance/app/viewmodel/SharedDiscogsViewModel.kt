package com.dissonance.app.viewmodel

import android.app.appsearch.SearchResult
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dissonance.app.data.model.DiscogSearchModel
import com.dissonance.app.singletons.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class SharedDiscogsViewModel() : ViewModel() {
    // Specifically for discog database search results
    private val _searchResults = MutableLiveData<DiscogSearchModel>()
    val searchResults: LiveData<DiscogSearchModel> = _searchResults

    // Live data for batch search results
    private val _batchSearchResults = MutableLiveData<Map<Pair<String, String>, DiscogSearchModel>>()
    val batchSearchResults: LiveData<Map<Pair<String,String>, DiscogSearchModel>> = _batchSearchResults

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
    // Returns only 1 albumn object
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

    // Batch search method search based on a list of queries
    // Input list of queries as list of pairs [title, artist]
    // Returns a list of objects corresponding to pair [title, artist]
    // Note order of returned obj is NOT guaranteed
    fun searchAlbums(albums: List<Pair<String, String>>) {
        // Clear previous results
        val resultsMap = mutableMapOf<Pair<String, String>, DiscogSearchModel>()

        viewModelScope.launch {
            val searchJobs = albums.map { (title, artist) ->
                async {
                    try {
                        val result = RetrofitClient.DiscogsApi.searchAlbumn(
                            query = title,
                            artist = artist,
                            title = title)
                        resultsMap[Pair(title, artist)] = result
                    } catch (e: Exception) {
                        Log.e("DiscogViewModel", "Search error for $title", e)
                    }
                }
            }

            // Wait for all searches to complete
            searchJobs.awaitAll()

            _batchSearchResults.value = resultsMap
        }
    }

    // Validate search parameters
    fun validateSearchParams(query: String, artist: String): Boolean {
        return query.isNotBlank() && artist.isNotBlank()
    }

    // Filter empty or invalid album pairs
    fun filterValidAlbumPairs(albums: List<Pair<String, String>>): List<Pair<String, String>> {
        return albums.filter { (title, artist) ->
            title.isNotBlank() && artist.isNotBlank()
        }
    }

    // Get most common genre from batch search results
    fun getMostCommonGenre(searchResults: Map<Pair<String, String>, DiscogSearchModel>): String? {
        val genreCounts = mutableMapOf<String, Int>()

        searchResults.forEach { (_, result) ->
            result.results?.forEach { item ->
                item.genre?.forEach { genre ->
                    genreCounts[genre] = (genreCounts[genre] ?: 0) + 1
                }
            }
        }

        return genreCounts.entries.maxByOrNull { it.value }?.key
    }
}