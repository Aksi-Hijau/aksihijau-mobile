package com.aksihijau.ui.payment.pilihnominalmetode

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aksihijau.api.ApiConfig
import com.aksihijau.api.CreateDonationResponse
import com.aksihijau.api.LoginResponse
import com.aksihijau.api.PaymentsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NominalMetodeViewModel : ViewModel() {
        var paymentlive = MutableLiveData<ArrayList<Payment>>()

    fun getPayments() {
        val apiService = ApiConfig.getApiService()
        val paymentRequest = apiService.getPayments()
        paymentRequest.enqueue(object: Callback<PaymentsResponse> {
            override fun onResponse(
                call: Call<PaymentsResponse>,
                response: Response<PaymentsResponse>
            ) {
                if(response.isSuccessful){
                    var payment = ArrayList<Payment>()
                    val bank = response.body()!!.data!!.bank
                    val ewallet = response.body()!!.data!!.ewallet

                    bank!!.forEach(){
                        payment.add(Payment(it!!.id, it.type, it.method, it.name, it.logo))
                    }
                    ewallet!!.forEach(){
                        payment.add(Payment(it!!.id, it.type, it.method, it.name, it.logo))
                    }
                    paymentlive.postValue(payment)
                }else {

                }
            }

            override fun onFailure(call: Call<PaymentsResponse>, t: Throwable) {

            }

        })
    }
}