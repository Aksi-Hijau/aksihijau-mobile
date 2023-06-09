package com.aksihijau.ui.fiturcampaign.donatur

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aksihijau.R
import com.aksihijau.api.campaignresponse.DataCampaign
import com.aksihijau.api.campaignresponse.Donation
import com.aksihijau.databinding.ListDonationBinding
import com.aksihijau.databinding.ListDonaturBinding
import com.aksihijau.ui.navigationmenu.campaign.ListDonasiAdapter
import com.bumptech.glide.Glide

class ListDonaturAdapter(
    private val listDonatur : ArrayList<Donation>
) : RecyclerView.Adapter<ListDonaturAdapter.ListViewHolder>() {

    private val originalList: ArrayList<Donation> = ArrayList(listDonatur)
    private var isLimited = true


    inner class ListViewHolder(private val binding: ListDonaturBinding) : RecyclerView.ViewHolder(binding.root){
        val imageDonatur : ImageView = itemView.findViewById(R.id.circle_photo)
        val tvNameDonatur : TextView = itemView.findViewById(R.id.tv_name_donatur)
        val tvDonasi : TextView = itemView.findViewById(R.id.tv_donasi_donatur)
        val tvDonasiDate : TextView = itemView.findViewById(R.id.tv_date_donatur)

        fun bind(donation: Donation) {
            tvNameDonatur.text = donation.name
            tvDonasi.text = donation.amount.toString()
            tvDonasiDate.text = donation.paidAt

            Glide.with(binding.root)
                .load(donation.image)
                .error(R.drawable.ic_error_image_24) // Optional error image
                .into(imageDonatur)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListDonaturAdapter.ListViewHolder {
        val binding = ListDonaturBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListDonaturAdapter.ListViewHolder, position: Int) {
        holder.bind(listDonatur[position])
    }

    override fun getItemCount(): Int = if (isLimited) minOf(listDonatur.size, 3) else listDonatur.size

    fun setLimited(isLimited: Boolean) {
        this.isLimited = isLimited
        notifyDataSetChanged()
    }


    fun setData(donatur : List<Donation>){
        listDonatur.clear()
        listDonatur.addAll(donatur)
        originalList.clear()
        originalList.addAll(donatur)
        notifyDataSetChanged()
    }
}