package com.aksihijau.ui.navigationmenu.history

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aksihijau.R
import com.aksihijau.api.DataItemDonationHistories
import com.aksihijau.databinding.FragmentHistoryBinding
import com.aksihijau.datastore.TokenPreferences
import com.aksihijau.datastore.TokenViewModel
import com.aksihijau.datastore.TokenViewModelFactory
import com.aksihijau.ui.navigationmenu.BottomNavigationActivity
import com.aksihijau.ui.user.login.LoginActivity
import com.aksihijau.ui.view.SplashScreen

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var tokenViewModel: TokenViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        historyViewModel =
            ViewModelProvider(this).get(HistoryViewModel::class.java)
        val pref = TokenPreferences.getInstance(requireContext().dataStore)
        tokenViewModel = ViewModelProvider(this, TokenViewModelFactory(pref))[TokenViewModel::class.java]

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.startDate.visibility = View.GONE
        binding.endDate.visibility = View.GONE

        tokenViewModel.getLoginSettings().observe(viewLifecycleOwner){
            if(!it){
                binding.btLogin.visibility = View.VISIBLE
                binding.textView4.visibility = View.VISIBLE

                binding.btLogin.setOnClickListener {
                    val intent = Intent( requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                }

                binding.rvHistory.visibility = View.INVISIBLE
                binding.relativeLayout.visibility = View.INVISIBLE
            }else {
                binding.btLogin.visibility = View.INVISIBLE
                binding.textView4.visibility = View.INVISIBLE

                binding.rvHistory.visibility = View.VISIBLE
                binding.relativeLayout.visibility = View.VISIBLE
                tokenViewModel.getToken().observe(viewLifecycleOwner){ token->
                    tokenViewModel.getRefreshToken().observe(viewLifecycleOwner){refreshToken->
                        historyViewModel.getDonationHistories(token, refreshToken)
                    }
                }
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupView()
        setupAction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView(){
        historyViewModel.histories.observe(viewLifecycleOwner){
            binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
            val historyAdapter = HistoryAdapter(it)
            historyAdapter.setOnItemClickCallback(object : HistoryAdapter.OnItemClickCallback{
                override fun onItemClicked(history: DataItemDonationHistories) {
                    val intent = Intent(requireContext(), HistoryDetailActivity::class.java)
                    intent.putExtra(HistoryDetailActivity.EXTRA_INV, history.invoice)
                    startActivity(intent)
                }
            })
            binding.rvHistory.adapter = historyAdapter
        }
    }

    private fun setupAction(){
        binding.startDate.setOnClickListener {
            var datePick = DatePickerDialog(requireContext())
            datePick.show()
        }


        binding.endDate.setOnClickListener {
            var datePick = DatePickerDialog(requireContext())
            datePick.show()
        }
    }

}