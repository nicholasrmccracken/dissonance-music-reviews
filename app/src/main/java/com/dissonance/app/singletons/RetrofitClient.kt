package com.dissonance.app.singletons
import com.dissonance.app.data.model.DiscogApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// Acts as a singleton obj that can be used to call query to discogAPIs
// Initializes as soon as app starts and persists
// Can use lazy keyword to init it only when first used
object RetrofitClient {
    private const val BASE_URL = "https://api.discogs.com/"

    // .create(Interface) tells retrofit to implement those functions needed for api calls
    val DiscogsApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DiscogApiService::class.java)
}