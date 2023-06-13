package com.aksihijau.ui.fiturcampaign.newsletter

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aksihijau.R
import com.aksihijau.api.campaignresponse.Report
import com.aksihijau.databinding.ListNewsBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class ListNewsAdapter(
    private val listNewsletter : ArrayList<Report>
) : RecyclerView.Adapter<ListNewsAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding : ListNewsBinding) : RecyclerView.ViewHolder(binding.root){
        val imageCreator : ImageView = itemView.findViewById(R.id.circle_photo_creator)
        val tvNameCreator : TextView = itemView.findViewById(R.id.tv_name_creator)
        val tvtitlenews : TextView = itemView.findViewById(R.id.tv_title_news)
        val body_news : TextView = itemView.findViewById(R.id.body_news)
        val tvdatenews : TextView = itemView.findViewById(R.id.tv_date_news)

        fun bind(report: Report){
            Glide.with(binding.root)
                .load(report.cratorImage)
                .error(R.drawable.ic_error_image_24)
                .into(imageCreator)
            tvNameCreator.text = report.cratorName
            tvtitlenews.text = report.title
            body_news.text = Html.fromHtml(report.body, Html.FROM_HTML_MODE_COMPACT)

            val originalDateString = report.createdAt
            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = originalFormat.parse(originalDateString)
            val targetFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.getDefault())
            val formattedDateString = targetFormat.format(date)
            tvdatenews.text = formattedDateString

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