package com.aksihijau.ui.navigationmenu.campaign

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
    private lateinit var searchCampaignEditText: EditText

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
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
        campaignViewModel.isSuccess.observe(viewLifecycleOwner, { isSuccess ->
            // Handle success state here
        })
        campaignViewModel.campaigns.observe(viewLifecycleOwner, { campaigns ->
            campaignAdapter.setLimited(false)
            campaignAdapter.setData(campaigns)
        })

        campaignViewModel.getCampaigns()
        searchCampaignEditText = binding.searchCampaign
        setupSearch()
        return root
    }

    private fun setupSearch() {
        searchCampaignEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                filterCampaign(s.toString())
            }

        })
    }

    private fun filterCampaign(query: String) {
        campaignAdapter.filterListByTitle(query)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}