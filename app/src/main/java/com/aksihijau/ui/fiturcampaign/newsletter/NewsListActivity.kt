package com.aksihijau.ui.fiturcampaign.newsletter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aksihijau.R
import com.aksihijau.databinding.ActivityNewsListBinding
import com.aksihijau.ui.fiturcampaign.detail.CampaignDetailActivity
import com.aksihijau.ui.fiturcampaign.donatur.ListDonaturAdapter

class NewsListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNewsListBinding
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: ListNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar.root as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("Newsletter")
        }
        binding.toolbar.root.setTitleTextColor(resources.getColor(R.color.white))

        val rvNewsletter : RecyclerView = binding.rvListNews
        rvNewsletter.layoutManager = LinearLayoutManager(this)
        newsAdapter = ListNewsAdapter(ArrayList())
        rvNewsletter.adapter = newsAdapter

        newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        val slug = intent.getStringExtra(CampaignDetailActivity.EXTRA_SLUG)
        slug?.let {
            newsViewModel.getNewsletter(it)
        }

        newsViewModel.isLoading.observe(this, { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = android.view.View.VISIBLE
            } else {
                binding.progressBar.visibility = android.view.View.GONE
            }
        })

        newsViewModel.isSuccess.observe(this, { isSuccess ->
            // Handle success state here
        })

        newsViewModel.reports.observe(this, { reports ->
            reports?.let {
                newsAdapter.setData(it)
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}