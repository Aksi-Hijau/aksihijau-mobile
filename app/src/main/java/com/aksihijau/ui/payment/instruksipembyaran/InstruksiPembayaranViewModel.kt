package com.aksihijau.ui.payment.instruksipembyaran

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aksihijau.api.ApiConfig
import com.aksihijau.api.ApiService
import com.aksihijau.api.Bank
import com.aksihijau.api.CreateDonationResponse
import com.aksihijau.api.DataCreateDonation
import com.aksihijau.api.Ewallet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InstruksiPembayaranViewModel : ViewModel() {
    var dataResponse = MutableLiveData<DataCreateDonation?>()
    var isLoading = MutableLiveData<Boolean>()
    fun createDonation(token:String, refreshToken:String, slug:String, paymentMethod:String, paymentType:String, amount:Int){
        isLoading.postValue(true)
        val apiService = ApiConfig.getApiService()
        val createDonationResponse = apiService.createDonation("Bearer $token", refreshToken,slug, amount, paymentType, paymentMethod)

        createDonationResponse.enqueue(object : Callback<CreateDonationResponse>{
            override fun onResponse(
                call: Call<CreateDonationResponse>,
                response: Response<CreateDonationResponse>
            ) {
                isLoading.postValue(false)
                if(response.isSuccessful){
                    dataResponse.postValue(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<CreateDonationResponse>, t: Throwable) {
                isLoading.postValue(false)
            }

        })
    }
}