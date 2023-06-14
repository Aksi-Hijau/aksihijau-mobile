package com.aksihijau.ui.fiturcampaign.newsletter

import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
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
import com.bumptech.glide.request.target.SimpleTarget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class ListNewsAdapter(
    private val listNewsletter: ArrayList<Report>
) : RecyclerView.Adapter<ListNewsAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding: ListNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageCreator: ImageView = itemView.findViewById(R.id.circle_photo_creator)
        private val tvNameCreator: TextView = itemView.findViewById(R.id.tv_name_creator)
        private val tvtitlenews: TextView = itemView.findViewById(R.id.tv_title_news)
        private val body_news: TextView = itemView.findViewById(R.id.body_news)
        private val tvdatenews: TextView = itemView.findViewById(R.id.tv_date_news)

        private val mainHandler = Handler(Looper.getMainLooper())

        private inner class ImageGetterImpl(private val html: String) : Html.ImageGetter {
            override fun getDrawable(source: String): Drawable {
                val futureTarget = Glide.with(itemView.context.applicationContext)
                    .asDrawable()
                    .load(source)
                    .submit()

                val width = body_news.width - body_news.paddingLeft - body_news.paddingRight
                val height = width * futureTarget.get().intrinsicHeight / futureTarget.get().intrinsicWidth

                val drawable = futureTarget.get()
                drawable.setBounds(0, 0, width, height)

                mainHandler.post {
                    body_news.invalidate()
                    val spannable = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, this@ImageGetterImpl, null)
                    body_news.text = spannable
                }

                return drawable
            }
        }

        fun bind(report: Report) {
            Glide.with(binding.root)
                .load(report.cratorImage)
                .error(R.drawable.ic_error_image_24)
                .into(imageCreator)
            tvNameCreator.text = report.cratorName
            tvtitlenews.text = report.title
            val html = report.body

            val imageGetter = html?.let { ImageGetterImpl(it) }

            CoroutineScope(Dispatchers.IO).launch {
                val spannable = withContext(Dispatchers.IO) {
                    Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, imageGetter, null)
                }
                withContext(Dispatchers.Main) {
                    body_news.text = spannable
                }
            }

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

    fun setData(report: List<Report>) {
        listNewsletter.clear()
        listNewsletter.addAll(report)
        notifyDataSetChanged()
    }
}