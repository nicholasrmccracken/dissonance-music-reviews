package com.dissonance.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import coil3.size.Scale
import com.dissonance.app.R
import com.dissonance.app.data.model.ReleaseResult

class SearchAdapter(
    private val onItemClick: (ReleaseResult) -> Unit
) : ListAdapter<ReleaseResult, SearchAdapter.SearchViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val album = getItem(position)
        holder.bind(album)
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.albumTitle)
        private val thumbImage: ImageView = itemView.findViewById(R.id.albumThumb)

        fun bind(item: ReleaseResult) {
            titleText.text = item.title
            thumbImage.load(item.thumb) {
                placeholder(android.R.drawable.ic_menu_report_image)
                scale(Scale.FILL)
            }

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ReleaseResult>() {
        override fun areItemsTheSame(oldItem: ReleaseResult, newItem: ReleaseResult): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ReleaseResult, newItem: ReleaseResult): Boolean {
            return oldItem == newItem
        }
    }
}
