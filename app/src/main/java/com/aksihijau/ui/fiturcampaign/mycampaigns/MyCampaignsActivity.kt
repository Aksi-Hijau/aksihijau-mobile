package com.aksihijau.ui.fiturcampaign.mycampaigns

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aksihijau.R
import com.aksihijau.databinding.ActivityMyCampaignsBinding
import com.aksihijau.datastore.TokenPreferences
import com.aksihijau.datastore.TokenViewModel
import com.aksihijau.datastore.TokenViewModelFactory
import com.aksihijau.ui.fiturcampaign.detail.CampaignDetailActivity
import com.aksihijau.ui.fiturcampaign.soil.SoilActivity
import com.aksihijau.ui.makecampaign.HomeMakeCampaignActivity
import com.aksihijau.ui.navigationmenu.campaign.ListDonasiAdapter

class MyCampaignsActivity : AppCompatActivity() {


    private lateinit var binding:ActivityMyCampaignsBinding
    private var campaignAdapter: ListDonasiAdapter? = null
    private lateinit var myCampaignsViewModel: MyCampaignsViewModel
    private lateinit var tokenViewModel: TokenViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCampaignsBinding.inflate(layoutInflater)

        myCampaignsViewModel = ViewModelProvider(this)[MyCampaignsViewModel::class.java]
        val pref = TokenPreferences.getInstance(this.dataStore)
        tokenViewModel = ViewModelProvider(this, TokenViewModelFactory(pref))[TokenViewModel::class.java]

        setContentView(binding.root)

        campaignAdapter = ListDonasiAdapter(arrayListOf()){ campaign ->
            val intent = Intent(this, CampaignDetailActivity::class.java)
            intent.putExtra(CampaignDetailActivity.EXTRA_SLUG, campaign.slug)
            startActivity(intent)
        }

        binding.rvMyCampaigns.layoutManager = LinearLayoutManager(this)
        binding.rvMyCampaigns.adapter = campaignAdapter

        tokenViewModel.getToken().observe(this){token->
            tokenViewModel.getRefreshToken().observe(this){rtoken->
                myCampaignsViewModel.getMyCampaigns(token, rtoken)
            }
        }

        myCampaignsViewModel.dataCampaigns.observe(this){
            if (it != null) {
                campaignAdapter!!.setData(it)
                campaignAdapter?.setLimited(false)
            }
        }

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this, HomeMakeCampaignActivity::class.java))
        }


    }
}