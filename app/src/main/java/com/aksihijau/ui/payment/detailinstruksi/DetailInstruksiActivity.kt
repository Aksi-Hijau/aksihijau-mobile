package com.aksihijau.ui.payment.detailinstruksi

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aksihijau.api.InstructionsItem
import com.aksihijau.databinding.ActivityDetailInstruksiBinding
import com.aksihijau.datastore.TokenPreferences
import com.aksihijau.datastore.TokenViewModel
import com.aksihijau.datastore.TokenViewModelFactory
import com.bumptech.glide.Glide

class DetailInstruksiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailInstruksiBinding
    private lateinit var detailInstruksiViewModel: DetailInstruksiViewModel
    private lateinit var tokenViewModel: TokenViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailInstruksiBinding.inflate(layoutInflater)
        detailInstruksiViewModel = ViewModelProvider(this)[DetailInstruksiViewModel::class.java]
        val pref = TokenPreferences.getInstance(dataStore)
        tokenViewModel = ViewModelProvider(this, TokenViewModelFactory(pref))[TokenViewModel::class.java]

        val invoice = intent.getStringExtra(EXTRA_INV)

//        Log.d("DetailInstruksiActivity", "onCreate: $amount")

        tokenViewModel.getToken().observe(this){token->
            tokenViewModel.getRefreshToken().observe(this){ rtoken->
                detailInstruksiViewModel.getDetailInstruksi(token, rtoken, invoice!!)
            }
        }

        binding.materialToolbar4.setNavigationOnClickListener {
            finish()
        }

        setContentView(binding.root)

        setupView()
    }

    private fun setupView(){
        detailInstruksiViewModel.dataDetailInstruksi.observe(this){
            binding.tvMethodName.text = it!!.payment!!.name
            Glide.with(binding.imageMethod).load(it.payment!!.logo).into(binding.imageMethod)
            binding.nomorVA.text = it.payment.vaNumber

            val amount = intent.getStringExtra(EXTRA_AMOUNT)

            binding.tvTotalDonasi.text = "Rp $amount"

            binding.rvInstruksi.layoutManager = LinearLayoutManager(this)
            val adapter = InstruksiAdapter(it.instructions as List<InstructionsItem>)

            binding.rvInstruksi.adapter = adapter
        }
    }

    companion object {
        const val EXTRA_INV = "extra_inv"
        const val EXTRA_AMOUNT = "extra_amount"
    }
}