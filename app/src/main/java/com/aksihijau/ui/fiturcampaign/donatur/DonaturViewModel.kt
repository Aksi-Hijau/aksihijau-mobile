package com.aksihijau.ui.fiturcampaign.donatur

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aksihijau.api.ApiConfig
import com.aksihijau.api.campaignresponse.Donation
import com.aksihijau.api.campaignresponse.DonaturResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DonaturViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _donatur = MutableLiveData<List<Donation>>()
    val donaturs: LiveData<List<Donation>> = _donatur

    fun getDonatur(slug: String){
        _isLoading.value = true

        val apiService = ApiConfig.getApiService()
        val DonaturRequest = apiService.getDonatur(slug)
        DonaturRequest.enqueue(object : Callback<DonaturResponse>{
            override fun onResponse(
                call: Call<DonaturResponse>,
                response: Response<DonaturResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val donaturResponse = response.body()?.data
                    donaturResponse?.let { data ->
                        _donatur.value = data
                    }
                    _isSuccess.value = true
                } else {
                    _isSuccess.value = false
                }
            }

            override fun onFailure(call: Call<DonaturResponse>, t: Throwable) {
                _isLoading.value = false
                _isSuccess.value = false
            }
        })
    }
}