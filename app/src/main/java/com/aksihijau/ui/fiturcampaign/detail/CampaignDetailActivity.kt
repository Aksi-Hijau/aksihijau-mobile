package com.aksihijau.ui.fiturcampaign.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aksihijau.R
import com.aksihijau.api.campaignresponse.CampaignDetailsData
import com.aksihijau.api.campaignresponse.DataCampaign
import com.aksihijau.api.campaignresponse.Donation
import com.aksihijau.databinding.ActivityCampaignDetailBinding
import com.aksihijau.datastore.datadummy.donasi
import com.aksihijau.ui.fiturcampaign.donatur.DonaturListActivity
import com.aksihijau.ui.fiturcampaign.donatur.ListDonaturAdapter
import com.aksihijau.ui.fiturcampaign.newsletter.NewsListActivity
import com.aksihijau.ui.fiturcampaign.soil.SoilActivity
import com.bumptech.glide.Glide

class CampaignDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SLUG = "extra_slug"
    }

    private lateinit var binding: ActivityCampaignDetailBinding
    private lateinit var campaignDetailViewModel: CampaignDetailViewModel
    private lateinit var originalList: List<Donation>
    private var donaturAdapter : ListDonaturAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCampaignDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rvDonatur: RecyclerView = binding.rvDonaturDetail
        rvDonatur.layoutManager = LinearLayoutManager(this)
        rvDonatur.adapter = donaturAdapter

        val slug = intent.getStringExtra(EXTRA_SLUG)

        campaignDetailViewModel = ViewModelProvider(this, CampaignDetailFactory(this)).get(CampaignDetailViewModel::class.java)

        slug?.let {
            campaignDetailViewModel.getCampaignDetails(it)
            campaignDetailViewModel.getDonatur_CampaignDetail(it)
        }

        campaignDetailViewModel.isLoading.observe(this, { isLoading ->
            // Handle loading state here
        })

        campaignDetailViewModel.isSuccess.observe(this, { isSuccess ->
            // Handle success state here
        })

        campaignDetailViewModel.campaignDetails.observe(this, { campaignDetails ->
            updateUI(campaignDetails)
        })

        campaignDetailViewModel.donaturs.observe(this, { donaturs ->
            originalList = donaturs
            donaturAdapter?.setData(originalList)
            donaturAdapter?.setLimited(true)

        })



        clickItem()

    }

    private fun updateUI(campaignDetails: CampaignDetailsData?) {
        campaignDetails?.let { data ->
            Glide.with(binding.root)
                .load(data.image)
                .error(R.drawable.ic_error_image_24)
                .into(binding.ivHeaderDetail)
            Glide.with(binding.root)
                .load(data.fundraiser?.photo)
                .error(R.drawable.ic_error_image_24)
                .into(binding.imageCompany)
            binding.titleDonasiDetail.text = data.title
            binding.donasiCollect.text = data.collected.toString()
            binding.targetDonasi.text = data.target.toString()
            binding.tvDurationDonasi.text= data.remainingDays.toString()
            binding.desc.text = Html.fromHtml(data.description, Html.FROM_HTML_MODE_COMPACT)
            binding.companyName.text = data.fundraiser?.name
            binding.jenisTanah.text = data.soil?.type
        }
    }

    private fun clickItem(){
        binding.cvSoil.setOnClickListener {
            val soilId = campaignDetailViewModel.campaignDetails.value?.soil?.id

            soilId?.let {
                val intent = Intent(this, SoilActivity::class.java)
                intent.putExtra("soilId", it)
                startActivity(intent)
            }
        }

        binding.cvDonatur.setOnClickListener {
            val slug = campaignDetailViewModel.campaignDetails.value?.slug
            slug?.let {
                val intent = Intent(this, DonaturListActivity::class.java)
                intent.putExtra(EXTRA_SLUG, it)
                startActivity(intent)
            }
        }
        binding.cvNewsletter.setOnClickListener {
            val slug = campaignDetailViewModel.campaignDetails.value?.slug
            slug?.let {
                val intent = Intent(this, NewsListActivity::class.java)
                intent.putExtra(EXTRA_SLUG, it)
                startActivity(intent)
            }
        }
        binding.backPress.setOnClickListener {
            onBackPressed()
        }
    }
}