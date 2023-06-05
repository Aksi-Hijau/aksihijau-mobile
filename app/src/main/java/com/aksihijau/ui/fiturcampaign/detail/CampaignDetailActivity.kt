package com.aksihijau.ui.fiturcampaign.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.aksihijau.R
import com.aksihijau.api.campaignresponse.CampaignDetailsData
import com.aksihijau.databinding.ActivityCampaignDetailBinding
import com.bumptech.glide.Glide

class CampaignDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SLUG = "extra_slug"
    }

    private lateinit var binding: ActivityCampaignDetailBinding
    private lateinit var campaignDetailViewModel: CampaignDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCampaignDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val slug = intent.getStringExtra(EXTRA_SLUG)

        campaignDetailViewModel = ViewModelProvider(this, CampaignDetailFactory(this)).get(CampaignDetailViewModel::class.java)

        slug?.let { campaignDetailViewModel.getCampaignDetails(it) }

        campaignDetailViewModel.isLoading.observe(this, { isLoading ->
            // Handle loading state here
        })

        campaignDetailViewModel.isSuccess.observe(this, { isSuccess ->
            // Handle success state here
        })

        campaignDetailViewModel.campaignDetails.observe(this, { campaignDetails ->
            updateUI(campaignDetails)
        })
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
            binding.desc.text = data.description
            binding.companyName.text = data.fundraiser?.name
            binding.jenisTanah.text = data.soil?.type

        }
    }
}