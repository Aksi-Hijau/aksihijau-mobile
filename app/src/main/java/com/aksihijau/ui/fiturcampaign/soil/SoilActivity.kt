package com.aksihijau.ui.fiturcampaign.soil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.lifecycle.ViewModelProvider
import com.aksihijau.R
import com.aksihijau.databinding.ActivitySoilBinding

class SoilActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySoilBinding
    private lateinit var soilViewModel: SoilViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("Soil Detail")
        }
        binding.toolbar.root.setTitleTextColor(resources.getColor(R.color.white))



        val soilId = intent.getIntExtra("soilId", -1)

        soilViewModel = ViewModelProvider(this).get(SoilViewModel::class.java)
        soilViewModel.getSoilDetails(soilId)

        soilViewModel.soilDetails.observe(this, { soil ->
            soil?.let {
                binding.jenisTanah.text = it.type
                binding.bodySoil.text = Html.fromHtml(it.body, Html.FROM_HTML_MODE_COMPACT)
            }

        })

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}