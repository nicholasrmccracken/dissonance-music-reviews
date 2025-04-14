package com.dissonance.app.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dissonance.app.R
import com.dissonance.app.adapter.SearchAdapter
import com.dissonance.app.screens.CreateReviewScreen
import com.dissonance.app.viewmodel.SharedDiscogsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
            val context = requireContext()
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_album_action, null)

            dialogView.findViewById<TextView>(R.id.dialogMessage).text =
                "What would you like to do?"

            val dialog = MaterialAlertDialogBuilder(context)
                .setView(dialogView)
                .create()

            dialogView.findViewById<Button>(R.id.viewDetailsButton).setOnClickListener {
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

                dialog.dismiss()
            }

            dialogView.findViewById<Button>(R.id.writeReviewButton).setOnClickListener {
                val (artistName, albumTitle) = selectedAlbum.title.split(" - ", limit = 2).let {
                    if (it.size == 2) it[0] to it[1] else "Unknown Artist" to selectedAlbum.title
                }

                val intent = Intent(context, CreateReviewScreen::class.java).apply {
                    putExtra("albumId", selectedAlbum.id.toString())
                    putExtra("albumTitle", albumTitle)
                    putExtra("albumCoverUrl", selectedAlbum.thumb ?: "")
                    putExtra("artistName", artistName)
                }
                startActivity(intent)
                dialog.dismiss()
            }

            dialogView.findViewById<Button>(R.id.cancelButton).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
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
