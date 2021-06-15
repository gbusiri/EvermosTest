package com.example.awesomeapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.awesomeapp.R
import com.example.awesomeapp.data.model.PhotoModel
import kotlinx.android.synthetic.main.layout_curated_horizontal.view.*

class CuratedAdapter(
    val photos: List<PhotoModel>,
    val onClickListener: ((View) -> Unit)?
) : RecyclerView.Adapter<CuratedAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_curated_horizontal, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindComponent(photos[position], onClickListener)
    }

    override fun getItemCount(): Int = photos.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindComponent(photoModel: PhotoModel, onClickListener: ((View) -> Unit)?) {
            Glide.with(itemView)
                .load(photoModel.images?.imageTiny)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(itemView.imgThumbnail)
            itemView.tvPhotographer.text =  photoModel.photographer
            itemView.tvPhotographerUrl.text = photoModel.photographerUrl
            itemView.setOnClickListener(onClickListener)
        }
    }
}