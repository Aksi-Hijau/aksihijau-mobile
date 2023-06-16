package com.aksihijau.ui.payment.instruksipembyaran

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.aksihijau.databinding.ActivityInstruksiPembayaranBinding
import com.aksihijau.datastore.TokenPreferences
import com.aksihijau.datastore.TokenViewModel
import com.aksihijau.datastore.TokenViewModelFactory
import com.aksihijau.ui.navigationmenu.history.HistoryDetailActivity
import com.aksihijau.ui.payment.detailinstruksi.DetailInstruksiActivity
import com.aksihijau.ui.payment.pilihnominalmetode.Payment
import com.bumptech.glide.Glide

class InstruksiPembayaranActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInstruksiPembayaranBinding
    private lateinit var instruksiPembayaranViewModel: InstruksiPembayaranViewModel
    private lateinit var tokenViewModel: TokenViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val payment = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<Payment>(EXTRA_PAYMENT, Payment::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Payment>(EXTRA_PAYMENT)
        }

        val amount = intent.getIntExtra(EXTRA_AMOUNT, 0)

        instruksiPembayaranViewModel = ViewModelProvider(this)[InstruksiPembayaranViewModel::class.java]
        val pref = TokenPreferences.getInstance(dataStore)
        tokenViewModel = ViewModelProvider(this, TokenViewModelFactory(pref))[TokenViewModel::class.java]

        val slug = intent.getStringExtra(EXTRA_SLUG)
        Log.d("InstruksiPembayaran", "onCreate: $slug")
        tokenViewModel.getToken().observe(this){token->
            tokenViewModel.getRefreshToken().observe(this){refreshToken->
            instruksiPembayaranViewModel.createDonation(token, refreshToken, slug!!, payment!!.method, payment.type, amount)
            }
        }

        binding = ActivityInstruksiPembayaranBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupView()

    }

    private fun setupView(){
        instruksiPembayaranViewModel.isLoading.observe(this){
            binding.progressBar3.visibility = if(it) View.VISIBLE else View.INVISIBLE
        }

        instruksiPembayaranViewModel.dataResponse.observe(this){
            if(it!!.payment!!.bank != null){
                binding.batasWaktu.text = it.deadline
                binding.tvMethod.text = it.payment!!.name
                Glide.with(binding.logoMethod).load(it.payment.logo).into(binding.logoMethod)
                binding.nomorVA.text = it.payment.bank!!.vaNumber
                binding.tvTotalDonasi.text = "Rp " + it.amount.toString()

                binding.btnCek.setOnClickListener {view->
                    val intent = Intent(this, HistoryDetailActivity::class.java)
                    intent.putExtra(HistoryDetailActivity.EXTRA_INV, it.invoice)
                    startActivity(intent)
                }
                binding.tvCaraPembayaran.setOnClickListener {view->
                    val intent = Intent(this, DetailInstruksiActivity::class.java)
                    intent.putExtra(DetailInstruksiActivity.EXTRA_INV, it.invoice)
                    intent.putExtra(DetailInstruksiActivity.EXTRA_AMOUNT, it.amount.toString())
                    startActivity(intent)
                }
            }else {
                var url = ""
                it.payment!!.ewallet!!.actions!!.forEach {
                    if(it!!.name == "deeplink-redirect"){
                        url = it.url!!
                    }
                }
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://$url"
                }

                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
                finish()
            }

        }
    }

    companion object {
        const val EXTRA_AMOUNT = "extra_amount"
        const val EXTRA_PAYMENT = "extra_payment"
        const val EXTRA_SLUG = "extra_slug"
    }
}