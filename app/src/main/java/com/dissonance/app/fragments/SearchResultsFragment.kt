package com.dissonance.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dissonance.app.R
import com.dissonance.app.adapter.SearchAdapter
import com.dissonance.app.viewmodel.SharedDiscogsViewModel

class SearchResultsFragment : Fragment() {

    private lateinit var viewModel: SharedDiscogsViewModel
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SharedDiscogsViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.searchResultsRecyclerView)
        val backButton = view.findViewById<Button>(R.id.backToSearchButton)

        searchAdapter = SearchAdapter { selectedAlbum ->
            val bundle = Bundle().apply {
                putInt("album_id", selectedAlbum.id)
                putString("album_title", selectedAlbum.title)
                putString("album_uri", selectedAlbum.uri)
                putString("album_thumb", selectedAlbum.thumb)
                putString("album_year", selectedAlbum.year)
                putString("album_country", selectedAlbum.country)
                putString("album_format", selectedAlbum.format?.joinToString(", "))
                putString("album_label", selectedAlbum.label?.joinToString(", "))
                putString("album_genre", selectedAlbum.genre?.joinToString(", "))
            }

            val fragment = AlbumDetailFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
        }

        viewModel.searchResults.observe(viewLifecycleOwner) { results ->
            val uniqueResults = results.results
                .distinctBy { it.title.trim().lowercase() }

            searchAdapter.submitList(uniqueResults)
        }


        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}
