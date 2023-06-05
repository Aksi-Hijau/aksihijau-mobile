package com.aksihijau.ui.navigationmenu.campaign

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aksihijau.databinding.FragmentCampaignBinding
import com.aksihijau.ui.fiturcampaign.detail.CampaignDetailActivity

class CampaignFragment : Fragment() {

    private var _binding: FragmentCampaignBinding? = null
    private val binding get() = _binding!!

    private lateinit var campaignViewModel: CampaignViewModel
    private lateinit var campaignAdapter: ListDonasiAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCampaignBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val rvDonation: RecyclerView = binding.rvListDonasi
        rvDonation.layoutManager = LinearLayoutManager(requireContext())
        campaignAdapter = ListDonasiAdapter(arrayListOf()) { campaign ->
            val intent = Intent(requireContext(), CampaignDetailActivity::class.java)
            intent.putExtra(CampaignDetailActivity.EXTRA_SLUG, campaign.slug)
            startActivity(intent)
        }
        rvDonation.adapter = campaignAdapter

        campaignViewModel = ViewModelProvider(this).get(CampaignViewModel::class.java)
        campaignViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            // Handle loading state here
        })
        campaignViewModel.isSuccess.observe(viewLifecycleOwner, { isSuccess ->
            // Handle success state here
        })
        campaignViewModel.campaigns.observe(viewLifecycleOwner, { campaigns ->
            campaignAdapter.setData(campaigns)
        })

        campaignViewModel.getCampaigns()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}