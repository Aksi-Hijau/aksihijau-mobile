package com.aksihijau.ui.payment.pilihnominalmetode

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aksihijau.databinding.PaymentItemBinding
import com.bumptech.glide.Glide

class PaymentAdapter(private val payment : ArrayList<Payment>) : RecyclerView.Adapter<PaymentAdapter.ListViewHolder>(){

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(binding: PaymentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvName: TextView = binding.tvItemName
        val photo: ImageView = binding.imgItemPhoto
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = PaymentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = payment.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val name= payment[position].name
        val logo = payment[position].logo
        holder.tvName.text = name
        Glide.with(holder.itemView).load(logo).into(holder.photo)
        holder.itemView.setOnClickListener{
            if (name != null) {
                onItemClickCallback.onItemClicked(payment[position])
            }
        }

    }
    interface OnItemClickCallback {
        fun onItemClicked(Payment: Payment)
    }

}