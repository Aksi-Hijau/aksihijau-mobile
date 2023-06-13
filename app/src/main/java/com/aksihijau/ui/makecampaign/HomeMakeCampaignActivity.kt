package com.aksihijau.ui.makecampaign

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import com.aksihijau.R
import com.aksihijau.databinding.ActivityHomeMakeCampaignBinding
import com.aksihijau.databinding.ActivitySoilAnalysisBinding
import com.aksihijau.ui.makecampaign.SoilAnalysis.SoilAnalysisActivity
import com.aksihijau.ui.webview.Makecampaign_webview_activity

class HomeMakeCampaignActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeMakeCampaignBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeMakeCampaignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar.root as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("Make Campaign")
        }
        binding.toolbar.root.setTitleTextColor(resources.getColor(R.color.white))

        onClick()


    }

    private fun onClick(){
        binding.btnCheck.setOnClickListener {
            val intent = Intent(this, SoilAnalysisActivity::class.java)
            startActivity(intent)
        }
        binding.btnCampaign.setOnClickListener {
            val intent = Intent(this, Makecampaign_webview_activity::class.java)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}