package com.aksihijau.ui.navigationmenu.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aksihijau.api.ApiConfig
import com.aksihijau.api.campaignresponse.CampaignResponse
import com.aksihijau.api.campaignresponse.DataCampaign
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _campaigns = MutableLiveData<List<DataCampaign>>()
    val campaigns: LiveData<List<DataCampaign>> = _campaigns


    fun getCampaigns_home(){
        _isLoading.value = true

        val apiService = ApiConfig.getApiService()
        val campaignRequest = apiService.getCampaigs()
        campaignRequest.enqueue(object : Callback<CampaignResponse> {
            override fun onResponse(
                call: Call<CampaignResponse>,
                response: Response<CampaignResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    val campaignResponse = response.body()
                    campaignResponse?.data?.let { data ->
                        _campaigns.value = data
                    }
                    _isSuccess.value = true
                } else {
                    _isSuccess.value = false
                }
            }

            override fun onFailure(call: Call<CampaignResponse>, t: Throwable) {
                _isLoading.value = false
                _isSuccess.value = false
            }

        })
    }
}