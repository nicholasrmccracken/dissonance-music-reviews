package com.dissonance.app.data.model
import android.provider.ContactsContract.Data
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query


interface DiscogApiService {
    // async func run and model populated by retrofit
    @GET ("database/search")
    suspend fun searchMusic(
        @Query("q") query: String,
        @Query("key") key: String = "YOUR_CONSUMER_KEY",
        @Query("secret") secret: String = "YOUR_CONSUMER_SECRET"
    ): DiscogSearchModel
}