package com.aksihijau.ui.fiturcampaign.donatur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aksihijau.R
import com.aksihijau.databinding.ActivityDonationListBinding
import com.aksihijau.ui.fiturcampaign.detail.CampaignDetailActivity

class DonaturListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDonationListBinding
    private lateinit var donaturViewModel: DonaturViewModel
    private lateinit var donaturAdapter : ListDonaturAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar.root as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("Donatur List")
        }
        binding.toolbar.root.setTitleTextColor(resources.getColor(R.color.white))

        val rvDonatur : RecyclerView = binding.rvListDonatur
        rvDonatur.layoutManager = LinearLayoutManager(this)
        donaturAdapter = ListDonaturAdapter(ArrayList())
        rvDonatur.adapter = donaturAdapter

        donaturViewModel = ViewModelProvider(this).get(DonaturViewModel::class.java)

        val slug = intent.getStringExtra(CampaignDetailActivity.EXTRA_SLUG)
        slug?.let {
            donaturViewModel.getDonatur(it)
        }

        donaturViewModel.isLoading.observe(this, { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        donaturViewModel.isSuccess.observe(this, { isSuccess ->
            // Handle success state here
        })

        donaturViewModel.donaturs.observe(this, { donaturs ->
            donaturs?.let {
                donaturAdapter.setLimited(false)
                donaturAdapter.setData(it)

            }

        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}