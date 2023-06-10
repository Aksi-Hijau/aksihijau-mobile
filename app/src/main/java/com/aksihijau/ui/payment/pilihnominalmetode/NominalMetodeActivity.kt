package com.aksihijau.ui.payment.pilihnominalmetode

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aksihijau.R
import com.aksihijau.databinding.ActivityNominalMetodeBinding
import com.aksihijau.databinding.DialogPilihMetodeBinding
import com.aksihijau.ui.fiturcampaign.detail.CampaignDetailActivity
import com.aksihijau.ui.fiturcampaign.detail.CampaignDetailFactory
import com.aksihijau.ui.fiturcampaign.detail.CampaignDetailViewModel
import com.aksihijau.ui.navigationmenu.history.History
import com.aksihijau.ui.navigationmenu.history.HistoryAdapter
import com.aksihijau.ui.navigationmenu.history.HistoryDetailActivity
import com.aksihijau.ui.payment.instruksipembyaran.InstruksiPembayaranActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.color.utilities.MaterialDynamicColors.background
import kotlin.properties.Delegates

class NominalMetodeActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SLUG = "extra_slug"
    }

    private lateinit var binding : ActivityNominalMetodeBinding
    private var banyakDonasi : Int  = 0
    private lateinit var nominalMetodeViewModel: NominalMetodeViewModel
    private lateinit var campaignDetailViewModel : CampaignDetailViewModel
    private lateinit var _payment: Payment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNominalMetodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val slug = intent.getStringExtra(CampaignDetailActivity.EXTRA_SLUG)

        campaignDetailViewModel = ViewModelProvider(this, CampaignDetailFactory(this))[
            CampaignDetailViewModel::class.java]

        nominalMetodeViewModel = ViewModelProvider(this)[NominalMetodeViewModel::class.java]

        nominalMetodeViewModel.getPayments()
        campaignDetailViewModel.getCampaignDetails(slug!!)

        campaignDetailViewModel.isLoading.observe(this, { isLoading ->
            // Handle loading state here
            binding.judulKampanye.visibility = View.INVISIBLE
        })

        campaignDetailViewModel.isSuccess.observe(this, { isSuccess ->
            binding.judulKampanye.visibility = View.VISIBLE
            // Handle success state here
        })

        campaignDetailViewModel.campaignDetails.observe(this, { campaignDetails ->
            binding.judulKampanye.text = campaignDetails!!.title
        })


        binding.tvMetodePembayaran.text = "Pilih Metode Pembayaran"
        binding.tvTotalDonasi.text = "Rp 0"
        binding.btnLanjutPembyaran.text = "Rp " + banyakDonasi.toString() + " | Donasi"
        setupAction()
    }

    private fun setupAction(){

        binding.materialToolbar2.setNavigationOnClickListener {
            finish()
        }

        binding.btn5000.setOnClickListener {
            banyakDonasi = 5000
            binding.btn5000.setTextColor(getColor(R.color.white))
            binding.btn50000.setTextColor(getColor(R.color. black))
            binding.btn10000.setTextColor(getColor(R.color.black))
            binding.btn25000.setTextColor(getColor(R.color.black))
            binding.btn5000.setBackgroundColor(getColor(R.color.green_1000))
            binding.btn10000.setBackgroundColor(getColor(R.color.white))
            binding.btn25000.setBackgroundColor(getColor(R.color.white))
            binding.btn50000.setBackgroundColor(getColor(R.color.white))

            binding.tvTotalDonasi.text = "Rp " + banyakDonasi.toString();
            binding.btnLanjutPembyaran.text = "Rp " + banyakDonasi.toString() + " | Donasi"

        }

        binding.btn10000.setOnClickListener {
            banyakDonasi = 10000
            binding.btn10000.setTextColor(getColor(R.color.white))
            binding.btn5000.setTextColor(getColor(R.color. black))
            binding.btn50000.setTextColor(getColor(R.color.black))
            binding.btn25000.setTextColor(getColor(R.color.black))
            binding.btn10000.setBackgroundColor(getColor(R.color.green_1000))
            binding.btn5000.setBackgroundColor(getColor(R.color.white))
            binding.btn25000.setBackgroundColor(getColor(R.color.white))
            binding.btn50000.setBackgroundColor(getColor(R.color.white))

            binding.tvTotalDonasi.text = "Rp " + banyakDonasi.toString();
            binding.btnLanjutPembyaran.text = "Rp " + banyakDonasi.toString() + " | Donasi"

        }

        binding.btn25000.setOnClickListener {
            banyakDonasi = 25000
            binding.btn25000.setTextColor(getColor(R.color.white))
            binding.btn5000.setTextColor(getColor(R.color. black))
            binding.btn10000.setTextColor(getColor(R.color.black))
            binding.btn50000.setTextColor(getColor(R.color.black))
            binding.btn25000.setBackgroundColor(getColor(R.color.green_1000))
            binding.btn10000.setBackgroundColor(getColor(R.color.white))
            binding.btn5000.setBackgroundColor(getColor(R.color.white))
            binding.btn50000.setBackgroundColor(getColor(R.color.white))

            binding.tvTotalDonasi.text = "Rp " + banyakDonasi.toString();
            binding.btnLanjutPembyaran.text = "Rp " + banyakDonasi.toString() + " | Donasi"

        }

        binding.btn50000.setOnClickListener {
            banyakDonasi = 50000
            binding.btn50000.setTextColor(getColor(R.color.white))
            binding.btn5000.setTextColor(getColor(R.color. black))
            binding.btn10000.setTextColor(getColor(R.color.black))
            binding.btn25000.setTextColor(getColor(R.color.black))
            binding.btn50000.setBackgroundColor(getColor(R.color.green_1000))
            binding.btn10000.setBackgroundColor(getColor(R.color.white))
            binding.btn25000.setBackgroundColor(getColor(R.color.white))
            binding.btn5000.setBackgroundColor(getColor(R.color.white))

            binding.tvTotalDonasi.text = "Rp " + banyakDonasi.toString();
            binding.btnLanjutPembyaran.text = "Rp " + banyakDonasi.toString() + " | Donasi"

        }


        binding.inputtext.addTextChangedListener {
            try {
                banyakDonasi = it.toString().toInt()
            }catch (e : Exception){
                banyakDonasi = 0
            }
            binding.tvTotalDonasi.text = "Rp " + banyakDonasi.toString()
            binding.btn50000.setTextColor(getColor(R.color.black))
            binding.btn5000.setTextColor(getColor(R.color. black))
            binding.btn10000.setTextColor(getColor(R.color.black))
            binding.btn25000.setTextColor(getColor(R.color.black))
            binding.btn50000.setBackgroundColor(getColor(R.color.white))
            binding.btn10000.setBackgroundColor(getColor(R.color.white))
            binding.btn25000.setBackgroundColor(getColor(R.color.white))
            binding.btn5000.setBackgroundColor(getColor(R.color.white))
            binding.btnLanjutPembyaran.text = "Rp " + banyakDonasi.toString() + " | Donasi"

        }

        binding.btnMetodePembayaran.setOnClickListener {
            val dialog = BottomSheetDialog(this)

            val bindDialog = DialogPilihMetodeBinding.inflate(layoutInflater)

            bindDialog.rvMetode.layoutManager = LinearLayoutManager(this)


            nominalMetodeViewModel.paymentlive.observe(this){
                val adapter = PaymentAdapter(it)

                adapter.setOnItemClickCallback(object : PaymentAdapter.OnItemClickCallback{
                    override fun onItemClicked(Payment: Payment) {
                        _payment = Payment
                        binding.tvMetodePembayaran.text = _payment.name
                        dialog.dismiss()
                    }
                })

                bindDialog.rvMetode.adapter = adapter
                dialog.setContentView(bindDialog.root)

                dialog.show()
            }

        }

        binding.btnLanjutPembyaran.setOnClickListener {

            if(banyakDonasi <= 1000){
                Toast.makeText(this, "Donasi Minimal Rp 1000", Toast.LENGTH_SHORT).show()
            } else if (!this::_payment.isInitialized){
                Toast.makeText(this, "Pilih Metode Pembayaran", Toast.LENGTH_SHORT).show()
            } else {
                val slug = intent.getStringExtra(EXTRA_SLUG)

                val intent = Intent(this, InstruksiPembayaranActivity::class.java)
                intent.putExtra(InstruksiPembayaranActivity.EXTRA_AMOUNT, banyakDonasi)
                intent.putExtra(InstruksiPembayaranActivity.EXTRA_PAYMENT, _payment)
                intent.putExtra(InstruksiPembayaranActivity.EXTRA_SLUG, slug)
                startActivity(intent)
                finish()
            }
        }
    }
}