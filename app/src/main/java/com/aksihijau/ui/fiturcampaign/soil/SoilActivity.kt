package com.aksihijau.ui.fiturcampaign.soil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aksihijau.databinding.ActivitySoilBinding

class SoilActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySoilBinding
    private lateinit var soilViewModel: SoilViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val soilId = intent.getIntExtra("soilId", -1)

        soilViewModel = ViewModelProvider(this).get(SoilViewModel::class.java)
        soilViewModel.getSoilDetails(soilId)

        soilViewModel.soilDetails.observe(this, Observer { soil ->
            soil?.let {
                binding.jenisTanah.text = it.type
                binding.bodySoil.text = Html.fromHtml(it.body, Html.FROM_HTML_MODE_COMPACT)
            }

        })

    }
}