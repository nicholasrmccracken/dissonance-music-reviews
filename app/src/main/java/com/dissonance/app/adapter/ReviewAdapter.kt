package com.dissonance.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import coil3.size.Scale
import com.dissonance.app.R
import com.dissonance.app.data.model.Review
import com.google.android.material.imageview.ShapeableImageView

class ReviewAdapter : ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val albumCoverImage = itemView.findViewById<ShapeableImageView>(R.id.album_cover)
        private val albumTitle = itemView.findViewById<TextView>(R.id.album_title)
        private val albumArtist = itemView.findViewById<TextView>(R.id.album_artist)
        private val rating = itemView.findViewById<TextView>(R.id.review_rating)
        private val reviewTitle = itemView.findViewById<TextView>(R.id.review_title)
        private val reviewContent = itemView.findViewById<TextView>(R.id.review_content)
        private val username = itemView.findViewById<TextView>(R.id.review_username_label)

        fun bind(item: Review) {
            albumCoverImage.load(item.albumCoverUrl.ifBlank { null }) {
                placeholder(android.R.drawable.ic_menu_report_image)
                error(R.drawable.album_placeholder)
                crossfade(true)
                scale(Scale.FILL)
            }
            albumTitle.text = item.albumTitle
            albumArtist.text = item.artistName
            rating.text = itemView.context.getString(R.string.review_rating_format, item.rating)
            reviewTitle.text = item.reviewTitle
            reviewContent.text = item.reviewText
            username.text = itemView.context.getString(R.string.review_username_label_format, item.username)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.userId == newItem.userId && oldItem.albumId == newItem.albumId
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }
}
