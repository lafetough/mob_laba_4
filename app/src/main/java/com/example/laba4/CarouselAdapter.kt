package com.example.laba4

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CarouselAdapter(private var photos: List<String>)
    : RecyclerView.Adapter<CarouselAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val imageView: ImageView = itemView.findViewById(R.id.carousel_breed_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carousel_item_layout, parent, false)
        return PhotoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = photos[position]
        Glide.with(holder.itemView.context)
            .load(item)
            .centerCrop()
            .into(holder.imageView)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<String>) {
        this.photos = newItems
        notifyDataSetChanged()
    }
}