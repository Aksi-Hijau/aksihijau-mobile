package com.aksihijau.ui.navigationmenu.history

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.aksihijau.R
import com.aksihijau.api.DataItemDonationHistories
import com.aksihijau.databinding.ActivityHistoryDetailBinding
import com.aksihijau.datastore.TokenPreferences
import com.aksihijau.datastore.TokenViewModel
import com.aksihijau.datastore.TokenViewModelFactory
import com.aksihijau.ui.navigationmenu.profile.detailinstruksi.DetailInstruksiActivity
import com.bumptech.glide.Glide


class HistoryDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHistoryDetailBinding
    private lateinit var historyDetailViewModel: HistoryDetailViewModel
    private lateinit var tokenViewModel: TokenViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        val pref = TokenPreferences.getInstance(dataStore)
        tokenViewModel = ViewModelProvider(this, TokenViewModelFactory(pref))[TokenViewModel::class.java]
        historyDetailViewModel = ViewModelProvider(this)[HistoryDetailViewModel::class.java]
        val invoice = intent.getStringExtra(EXTRA_INV)


        tokenViewModel.getToken().observe(this){token->
            tokenViewModel.getRefreshToken().observe(this){rtoken->
                historyDetailViewModel.getHistoryDetail(token, rtoken, invoice!!)
            }
        }

        setContentView(binding.root)

        setupView()
    }

    private fun setupView(){
        historyDetailViewModel.historyDetail.observe(this){history->
            binding.msg1.text = if(history!!.status == "paid") "Terima Kasih" else if(history.status == "pending") "Menunggu Pembayaran" else "Donasi Dibatalkan"
            binding.msg2.text = if(history.status == "paid") "Donasimu telah berhasil dan akan segera disalurkan"
                                else if(history.status == "pending") "Segera Lakukan Pembayaran hingga \n${history.deadline}"
                                else "Donasi telah gagal dan gagal tercatat oleh sistem"
            binding.tvDate.text = history.createdAt!!.substring(0, 10)
            binding.tvId.text = history.invoice
            binding.tvMetode.text = history.payment!!.name
            binding.tvStatus.text = history.status

            if(history.status == "pending"){
                binding.tvStatus.setTextColor(getColor(R.color.yellow))
                binding.tvGotoInstruksi.visibility = View.VISIBLE
                binding.tvGotoInstruksi.setOnClickListener {
                    val intent = Intent(this, DetailInstruksiActivity::class.java)
                    intent.putExtra(DetailInstruksiActivity.EXTRA_INV, history.invoice)
                    intent.putExtra(DetailInstruksiActivity.EXTRA_AMOUNT, history.amount)
                    startActivity(intent)
                }
            }else if (history.status == "paid"){
                binding.tvStatus.setTextColor(getColor(R.color.green_1000))
            }else {
                binding.tvStatus.setTextColor(getColor(R.color.red))
            }

            Glide.with(binding.imgItemPhoto).load(history.campaignImage).into(binding.imgItemPhoto)
            binding.tvItemName.text = history.campaignTitle
            binding.tvJumlahdonasi.text = "Rp " + history.amount
        }
    }

    companion object {
        const val EXTRA_INV = "extra_inv"
    }
}