package com.aksihijau.ui.fiturcampaign.newsletter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aksihijau.R
import com.aksihijau.api.campaignresponse.Report
import com.aksihijau.databinding.ListNewsBinding
import com.bumptech.glide.Glide

class ListNewsAdapter(
    private val listNewsletter : ArrayList<Report>
) : RecyclerView.Adapter<ListNewsAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding : ListNewsBinding) : RecyclerView.ViewHolder(binding.root){
        val imageCreator : ImageView = itemView.findViewById(R.id.circle_photo_creator)
        val tvNameCreator : TextView = itemView.findViewById(R.id.tv_name_creator)
        val tvtitlenews : TextView = itemView.findViewById(R.id.tv_title_news)
        val body_news : TextView = itemView.findViewById(R.id.body_news)

        fun bind(report: Report){
            Glide.with(binding.root)
                .load(report.cratorImage)
                .error(R.drawable.ic_error_image_24)
                .into(imageCreator)
            tvNameCreator.text = report.cratorName
            tvtitlenews.text = report.title
            body_news.text = report.body

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listNewsletter.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listNewsletter[position])
    }

    fun setData(report : List<Report>){
        listNewsletter.clear()
        listNewsletter.addAll(report)
        notifyDataSetChanged()
    }
}