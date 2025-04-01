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
        val thumbUrl = arguments?.getString("album_thumb")
        val year = arguments?.getString("album_year") ?: "N/A"
        val country = arguments?.getString("album_country") ?: "N/A"
        val format = arguments?.getString("album_format") ?: "N/A"
        val label = arguments?.getString("album_label") ?: "N/A"
        val genre = arguments?.getString("album_genre") ?: "N/A"

        val titleText = view.findViewById<TextView>(R.id.detailAlbumTitle)
        val thumbImage = view.findViewById<ImageView>(R.id.detailAlbumImage)
        val yearText = view.findViewById<TextView>(R.id.detailAlbumYear)
        val countryText = view.findViewById<TextView>(R.id.detailAlbumCountry)
        val formatText = view.findViewById<TextView>(R.id.detailAlbumFormat)
        val labelText = view.findViewById<TextView>(R.id.detailAlbumLabel)
        val genreText = view.findViewById<TextView>(R.id.detailAlbumGenre)
        val backButton = view.findViewById<Button>(R.id.backToResultsButton)

        titleText.text = albumTitle
        yearText.text = year.removePrefix("Year:").trim()
        countryText.text = country.removePrefix("Country:").trim()
        formatText.text = truncateListString(format, "Format")
        labelText.text = truncateListString(label, "Label")
        genreText.text = truncateListString(genre, "Genre")

        thumbImage.load(thumbUrl) {
            placeholder(android.R.drawable.ic_menu_report_image)
            scale(Scale.FILL)
        }

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun truncateListString(value: String, label: String): String {
        val cleaned = value.removePrefix("$label:").trim()
        val items = cleaned.split(",").map { it.trim() }

        return if (items.size > 5) {
            items.take(5).joinToString(", ") + ", etc."
        } else {
            cleaned
        }
    }

}
