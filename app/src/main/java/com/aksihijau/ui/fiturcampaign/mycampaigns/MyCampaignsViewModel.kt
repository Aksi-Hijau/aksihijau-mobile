package com.aksihijau.ui.fiturcampaign.mycampaigns

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aksihijau.api.ApiConfig
import com.aksihijau.api.campaignresponse.DataCampaign
import com.aksihijau.api.campaignresponse.MyCampaignsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyCampaignsViewModel : ViewModel() {
    var dataCampaigns = MutableLiveData<List<DataCampaign>?>()
    fun getMyCampaigns(token: String, rtoken: String){
        val apiService = ApiConfig.getApiService()
        val r = apiService.getMyCampaigns(token, rtoken)
        r.enqueue(object : Callback<MyCampaignsResponse>{
            override fun onResponse(
                call: Call<MyCampaignsResponse>,
                response: Response<MyCampaignsResponse>
            ) {
                if(response.isSuccessful){


                    dataCampaigns.postValue(response.body()!!.data as List<DataCampaign>?)
                }
            }

            override fun onFailure(call: Call<MyCampaignsResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}