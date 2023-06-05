package com.aksihijau.ui.navigationmenu.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aksihijau.databinding.FragmentHomeBinding
import com.aksihijau.ui.fiturcampaign.detail.CampaignDetailActivity
import com.aksihijau.ui.navigationmenu.campaign.CampaignViewModel
import com.aksihijau.ui.navigationmenu.campaign.ListDonasiAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var campaignAdapter: ListDonasiAdapter? = null
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val rvDonation: RecyclerView = binding.rvDonasi

        campaignAdapter = ListDonasiAdapter(arrayListOf()){ campaign ->
            val intent = Intent(requireContext(), CampaignDetailActivity::class.java)
            intent.putExtra(CampaignDetailActivity.EXTRA_SLUG, campaign.slug)
            startActivity(intent)
        }

        rvDonation.layoutManager = LinearLayoutManager(requireContext())
        rvDonation.adapter = campaignAdapter

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            // Handle loading state here
        })
        homeViewModel.isSuccess.observe(viewLifecycleOwner, { isSuccess ->
            // Handle success state here
        })
        homeViewModel.campaigns.observe(viewLifecycleOwner, { campaigns ->
            campaignAdapter?.setData(campaigns)
        })

        homeViewModel.getCampaigns_home()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}