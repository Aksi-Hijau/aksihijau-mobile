package com.aksihijau.ui.navigationmenu.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aksihijau.databinding.FragmentProfileBinding
import com.aksihijau.ui.makecampaign.SoilAnalysis.SoilAnalysisActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textProfile
        profileViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = itx
        }

        binding.cvMakecampaign.setOnClickListener {
            openSoilAnalysisActivity()
        }
        return root
    }

    private fun openSoilAnalysisActivity() {
        val intent = Intent(requireContext(), SoilAnalysisActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}