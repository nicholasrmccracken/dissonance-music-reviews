package com.dissonance.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dissonance.app.R
import com.dissonance.app.viewmodel.SharedDiscogsViewModel

class SearchFragment : Fragment() {

    private lateinit var viewModel: SharedDiscogsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SharedDiscogsViewModel::class.java]

        val artistInput = view.findViewById<EditText>(R.id.artistInput)
        val titleInput = view.findViewById<EditText>(R.id.titleInput)
        val genreInput = view.findViewById<EditText>(R.id.genreInput)
        val yearInput = view.findViewById<EditText>(R.id.yearInput)
        val searchButton = view.findViewById<Button>(R.id.searchButton)

        searchButton.setOnClickListener {
            val artist = artistInput.text.toString()
            val title = titleInput.text.toString()
            val genre = genreInput.text.toString()
            val year = yearInput.text.toString()

            if (artist.isBlank()) {
                Toast.makeText(requireContext(), "Artist is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.searchAlbum(
                query = "$artist $title",
                artist = artist,
                title = title.takeIf { it.isNotBlank() },
                year = year.takeIf { it.isNotBlank() },
                genre = genre.takeIf { it.isNotBlank() }
            )

            val resultsFragment = SearchResultsFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, resultsFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}