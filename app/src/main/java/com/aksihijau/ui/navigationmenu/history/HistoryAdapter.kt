package com.aksihijau.ui.navigationmenu.history

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aksihijau.R
import com.aksihijau.api.DataItemDonationHistories
import com.aksihijau.databinding.HistoryItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions

class HistoryAdapter(private val history : List<DataItemDonationHistories>) : RecyclerView.Adapter<HistoryAdapter.ListViewHolder>(){

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvDesc: TextView = binding.tvItemDesc
        val tvName: TextView = binding.tvItemName
        val photo: ImageView = binding.imgItemPhoto
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = history.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val name= history[position].campaignTitle
        val amount = history[position].amount
        val photo = history[position].campaignImage
        holder.tvName.text = name
        holder.tvDesc.text = "Rp " + amount

        val imageUrl = photo.replace("storage.cloud.google.com", "storage.googleapis.com")

        Glide.with(holder.photo)
            .load(imageUrl)
            .apply(RequestOptions().format(DecodeFormat.PREFER_RGB_565))
            .error(R.drawable.ic_error_image_24) // Optional error image
            .into(holder.photo)
        holder.itemView.setOnClickListener{
            if (name != null) {
                onItemClickCallback.onItemClicked(history[position])
            }
        }

    }
    interface OnItemClickCallback {
        fun onItemClicked(history: DataItemDonationHistories)
    }

}