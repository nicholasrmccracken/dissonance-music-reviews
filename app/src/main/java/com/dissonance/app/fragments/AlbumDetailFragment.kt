package com.dissonance.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dissonance.app.R
import coil3.load
import coil3.request.placeholder
import coil3.size.Scale

class AlbumDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_album_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val albumTitle = arguments?.getString("album_title") ?: "Unknown Title"
        val albumUri = arguments?.getString("album_uri")

        val titleText = view.findViewById<TextView>(R.id.detailAlbumTitle)
        val thumbImage = view.findViewById<ImageView>(R.id.detailAlbumImage)
        val backButton = view.findViewById<Button>(R.id.backToResultsButton)

        titleText.text = albumTitle

        // If you have a thumbnail URL, load it using Coil
        thumbImage.load("https://api.discogs.com$albumUri") {
            placeholder(android.R.drawable.ic_menu_report_image)
            scale(Scale.FILL)
        }

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}