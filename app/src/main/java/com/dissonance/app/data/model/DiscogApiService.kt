package com.dissonance.app.data.model
import android.provider.ContactsContract.Data
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

import com.dissonance.app.BuildConfig



interface DiscogApiService {
    // async func run and model populated by retrofit
    @GET ("database/search")
    suspend fun searchMusic(
        @Query("q") query: String,
        @Query("key") key: String = BuildConfig.DISCOGS_CONSUMER_KEY,
        @Query("secret") secret: String = BuildConfig.DISCOGS_CONSUMER_SECRET
    ): DiscogSearchModel


    @GET ("database/search")
    suspend fun searchAlbumn(
        @Query("q") query: String, // The main query term, e.g., album title, artist, etc.
        @Query("key") key: String = BuildConfig.DISCOGS_CONSUMER_KEY, // API Key
        @Query("secret") secret: String = BuildConfig.DISCOGS_CONSUMER_SECRET, // API Secret
        @Query("type") type: String = "release", // Limit results to 'release' type (album)
        @Query("title") title: String? = null,
        @Query("artist") artist: String,
        @Query("year") year: String? = null,
        @Query("genre") genre: String? = null,
        @Query("label") label: String? = null
    ): DiscogSearchModel
}