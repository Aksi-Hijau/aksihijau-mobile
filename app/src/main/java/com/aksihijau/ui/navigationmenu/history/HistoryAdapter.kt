package com.aksihijau.ui.navigationmenu.history

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aksihijau.api.DataItemDonationHistories
import com.aksihijau.databinding.HistoryItemBinding
import com.bumptech.glide.Glide

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
        Glide.with(holder.photo).load(photo).into(holder.photo)
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