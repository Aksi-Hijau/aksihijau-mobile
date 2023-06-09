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

class ListDonasiAdapter(
    private val listDonasi : ArrayList<DataCampaign>,
    private val onItemClick : (DataCampaign) -> Unit
)  : RecyclerView.Adapter<ListDonasiAdapter.ListViewHolder>(){

    private val originalList: ArrayList<DataCampaign> = ArrayList(listDonasi)

    inner class ListViewHolder(private val binding: ListDonationBinding) : RecyclerView.ViewHolder(binding.root){
        val imageDonasi : ImageView = itemView.findViewById(R.id.imageDonasi)
        val tvNameCampaign : TextView = itemView.findViewById(R.id.tv_nameCampaign)
        val tvTarget : TextView = itemView.findViewById(R.id.tv_target)
        val tvSisaHari : TextView = itemView.findViewById(R.id.tv_sisa_hari)

        fun bind(dataCampaign: DataCampaign) {
            tvNameCampaign.text = dataCampaign.title
            tvTarget.text = dataCampaign.target.toString()
            tvSisaHari.text = dataCampaign.remainingDays.toString()

            Glide.with(binding.root)
                .load(dataCampaign.image)
                .error(R.drawable.ic_error_image_24) // Optional error image
                .into(imageDonasi)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListDonationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listDonasi.size

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

    fun getOriginalData(): List<DataCampaign> {
        return originalList
    }

}