package com.dissonance.app.screens

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dissonance.app.singletons.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class discogsTest: AppCompatActivity() {
    private val testScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        testScope.launch {
            try{
                val results = RetrofitClient.DiscogsApi.searchAlbumn("The Bends", artist = "Radiohead")
                Log.d("debug", results.results[0].title)
                Log.d("debug", results.results[0].uri)
                Log.d("debug", results.results[0].type)
                results.results[0].year?.let { Log.d("debug", it) }
                results.results[0].thumb?.let { Log.d("debug", it) }
                results.results[0].catalogNumber?.let { Log.d("debug", it) }
                Log.d("debug", results.results[0].uri)
                Log.d("debug", results.results[0].genre.toString())

                Log.d("DEBUG", "Discogs retreived something")
                println(results)
            } catch (e: Exception) {
                Log.d("Discog api", "Error occured when trying to search discogs api")
            }
        }
    }
}