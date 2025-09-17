package com.example.laba4

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MainListAdapter(
    private var list: List<BreedPreview>,
    private var onItemClick: (BreedPreview) -> Unit
    )
    : RecyclerView.Adapter<MainListAdapter.MainListHolder>() {

    inner class MainListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val breedName: TextView = itemView.findViewById(R.id.main_breed_list_text)
        val thumbnail: ImageView = itemView.findViewById(R.id.main_breed_list_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_list_item_layout, parent, false)
        return MainListHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MainListHolder, position: Int) {
        val item = list[position]
        holder.breedName.text = item.breedName
        Glide.with(holder.itemView.context)
            .load(item.thumbnail)
            .into(holder.thumbnail)
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<BreedPreview>) {
        this.list = newItems
        notifyDataSetChanged()
    }

}