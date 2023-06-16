package com.aksihijau.ui.navigationmenu.campaign

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aksihijau.R
import com.aksihijau.api.campaignresponse.DataCampaign
import com.aksihijau.databinding.ListDonationBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import java.text.NumberFormat
import java.util.Locale

class ListDonasiAdapter(
    private val listDonasi : ArrayList<DataCampaign>,
    private val onItemClick : (DataCampaign) -> Unit
)  : RecyclerView.Adapter<ListDonasiAdapter.ListViewHolder>(){

    private val originalList: ArrayList<DataCampaign> = ArrayList(listDonasi)
    private var isLimited = true
    val localeID = Locale("id", "ID")
    val currencyFormat = NumberFormat.getCurrencyInstance(localeID)

    inner class ListViewHolder(private val binding: ListDonationBinding) : RecyclerView.ViewHolder(binding.root){
        val imageDonasi : ImageView = itemView.findViewById(R.id.imageDonasi)
        val tvNameCampaign : TextView = itemView.findViewById(R.id.tv_nameCampaign)
        val tvTarget : TextView = itemView.findViewById(R.id.tv_target)
        val tvSisaHari : TextView = itemView.findViewById(R.id.tv_sisa_hari)

        fun bind(dataCampaign: DataCampaign) {
            tvNameCampaign.text = dataCampaign.title
            val donationCollectAmount = dataCampaign.target.toString()
            val Collectamount = donationCollectAmount.toDouble()
            tvTarget.text = currencyFormat.format(Collectamount)
            tvSisaHari.text = dataCampaign.remainingDays.toString()

            val imageUrl = dataCampaign.image?.replace("storage.cloud.google.com", "storage.googleapis.com")

            Glide.with(binding.root)
                .load(imageUrl)
                .apply(RequestOptions().format(DecodeFormat.PREFER_RGB_565))
                .error(R.drawable.ic_error_image_24) // Optional error image
                .into(imageDonasi)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListDonationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = if (isLimited) minOf(listDonasi.size, 2) else listDonasi.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listDonasi[position])
        holder.itemView.setOnClickListener {
            onItemClick(listDonasi[position])
        }

    }

    fun setData(campaigns: List<DataCampaign>) {
        listDonasi.clear()
        listDonasi.addAll(campaigns)
        originalList.clear()
        originalList.addAll(campaigns)
        notifyDataSetChanged()
    }

    fun setLimited(isLimited: Boolean) {
        this.isLimited = isLimited
        notifyDataSetChanged()
    }

    fun filterListByTitle(query: String) {
        val filteredList = ArrayList<DataCampaign>()

        for (campaign in originalList) {
            if (campaign.title!!.contains(query, true)) {
                filteredList.add(campaign)
            }
        }

        listDonasi.clear()
        listDonasi.addAll(filteredList)
        notifyDataSetChanged()
    }

}