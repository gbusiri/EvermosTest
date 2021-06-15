package com.example.awesomeapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.awesomeapp.R
import com.example.awesomeapp.data.model.PhotoModel
import kotlinx.android.synthetic.main.layout_curated_grid.view.*
import kotlinx.android.synthetic.main.layout_curated_list.view.*

class CuratedAdapter(
    val photos: List<PhotoModel>,
    val isGridView: Boolean,
    val onClickListener: ((View) -> Unit)?
) : RecyclerView.Adapter<CuratedAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            if (isGridView)
                R.layout.layout_curated_grid
            else
                R.layout.layout_curated_list,
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindComponent(photos[position], isGridView, onClickListener)
    }

    override fun getItemCount(): Int = photos.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindComponent(photoModel: PhotoModel, isGridView: Boolean, onClickListener: ((View) -> Unit)?) {
            val imgThumbnail = if (isGridView) itemView.imgThumbnailGrid else itemView.imgThumbnail
            val photographer = if (isGridView) itemView.tvPhotographerGrid else itemView.tvPhotographer
            val photographerUrl = if (isGridView) itemView.tvPhotographerUrlGrid else itemView.tvPhotographerUrl

            Glide.with(itemView)
                .load(photoModel.images?.imageTiny)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(imgThumbnail)
            photographer.text = photoModel.photographer
            photographerUrl.text = photoModel.photographerUrl
            itemView.setOnClickListener(onClickListener)
        }
    }
}