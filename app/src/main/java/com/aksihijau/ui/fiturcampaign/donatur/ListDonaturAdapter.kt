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
import java.text.NumberFormat
import java.util.Locale

class ListDonaturAdapter(
    private val listDonatur : ArrayList<Donation>
) : RecyclerView.Adapter<ListDonaturAdapter.ListViewHolder>() {

    private val originalList: ArrayList<Donation> = ArrayList(listDonatur)
    private var isLimited = true
    val localeID = Locale("id", "ID")
    val currencyFormat = NumberFormat.getCurrencyInstance(localeID)


    inner class ListViewHolder(private val binding: ListDonaturBinding) : RecyclerView.ViewHolder(binding.root){
        val imageDonatur : ImageView = itemView.findViewById(R.id.circle_photo)
        val tvNameDonatur : TextView = itemView.findViewById(R.id.tv_name_donatur)
        val tvDonasi : TextView = itemView.findViewById(R.id.tv_donasi_donatur)
        val tvDonasiDate : TextView = itemView.findViewById(R.id.tv_date_donatur)

        fun bind(donation: Donation) {
            tvNameDonatur.text = donation.name
            val DonationAmount = donation.amount.toString()
            val Donation = DonationAmount.toDouble()
            tvDonasi.text = currencyFormat.format(Donation)
            tvDonasiDate.text = donation.paidAt

            val imageUrl = donation.image?.replace("storage.cloud.google.com", "storage.googleapis.com")

            Glide.with(binding.root)
                .load(imageUrl)
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