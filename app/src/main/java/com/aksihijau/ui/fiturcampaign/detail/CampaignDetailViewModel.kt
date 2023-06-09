package com.aksihijau.ui.fiturcampaign.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aksihijau.api.ApiConfig
import com.aksihijau.api.campaignresponse.CampaignDetailsData
import com.aksihijau.api.campaignresponse.CampaignDetailsResponse
import com.aksihijau.api.campaignresponse.Donation
import com.aksihijau.api.campaignresponse.DonaturResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CampaignDetailViewModel(private val context: Context) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _campaignDetails = MutableLiveData<CampaignDetailsData?>()
    val campaignDetails: MutableLiveData<CampaignDetailsData?> = _campaignDetails

    private val _donatur = MutableLiveData<List<Donation>>()
    val donaturs: LiveData<List<Donation>> = _donatur

    fun getCampaignDetails(slug: String) {
        _isLoading.value = true

        val apiService = ApiConfig.getApiService()
        val campaignDetailsRequest = apiService.getCampaignDetails(slug)
        campaignDetailsRequest.enqueue(object : Callback<CampaignDetailsResponse> {
            override fun onResponse(
                call: Call<CampaignDetailsResponse>,
                response: Response<CampaignDetailsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val campaignDetailsResponse = response.body()
                    val campaignDetails = transformResponseToCampaignDetails(campaignDetailsResponse)
                    _campaignDetails.value = campaignDetails
                    _isSuccess.value = true
                } else {
                    _isSuccess.value = false
                }
            }

            override fun onFailure(call: Call<CampaignDetailsResponse>, t: Throwable) {
                _isLoading.value = false
                _isSuccess.value = false
            }
        })
    }

    private fun transformResponseToCampaignDetails(response: CampaignDetailsResponse?): CampaignDetailsData? {
        if (response?.success == true) {
            val campaignData = response.data


            // Transform the response data to the CampaignDetails object
            val transformedData = CampaignDetailsData(
                campaignData?.id,
                campaignData?.soilId,
                campaignData?.slug,
                campaignData?.title,
                campaignData?.image,
                campaignData?.target,
                campaignData?.deadline,
                campaignData?.description,
                campaignData?.updatedAt,
                campaignData?.createdAt,
                campaignData?.reportsCount,
                campaignData?.fundraiser,
                campaignData?.soil,
                campaignData?.donationsCount,
                campaignData?.remainingDays,
                campaignData?.collected,
                campaignData?.active,
                campaignData?.latestDonations,
            )
            
            return transformedData
        }

        return null
    }

    fun getDonatur_CampaignDetail(slug: String){
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