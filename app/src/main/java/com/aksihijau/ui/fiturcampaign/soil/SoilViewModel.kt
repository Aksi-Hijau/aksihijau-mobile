package com.aksihijau.ui.fiturcampaign.soil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aksihijau.api.ApiConfig
import com.aksihijau.api.campaignresponse.CampaignDetailsData
import com.aksihijau.api.campaignresponse.Soil
import com.aksihijau.api.campaignresponse.SoilsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SoilViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _soilDetails = MutableLiveData<Soil?>()
    val soilDetails: MutableLiveData<Soil?> = _soilDetails

    fun getSoilDetails(soilId : Int){
        _isLoading.value = true

        val apiService = ApiConfig.getApiService()
        val SoilDetailsRequest = apiService.getSoil(soilId)
        SoilDetailsRequest.enqueue(object : Callback<SoilsResponse>{
            override fun onResponse(call: Call<SoilsResponse>, response: Response<SoilsResponse>) {
                val soilResponse = response.body()
                if (response.isSuccessful && soilResponse != null && soilResponse.success == true){
                    _isLoading.value = false
                    val soilDetails = transformResponseToSoilDetails(soilResponse)
                    _soilDetails.value = soilDetails
                    _isSuccess.value = true
                } else{
                    _isSuccess.value = false
                }
            }

            override fun onFailure(call: Call<SoilsResponse>, t: Throwable) {
                _isLoading.value = false
                _isSuccess.value = false
            }

        })
    }


    private fun transformResponseToSoilDetails(soilResponse: SoilsResponse): Soil? {
        if (soilResponse?.success == true) {
            val SoilData = soilResponse.data


            // Transform the response data to the CampaignDetails object
            val transformedData = Soil(
                SoilData?.id,
                SoilData?.type,
                SoilData?.image,
                SoilData?.body
            )

            return transformedData
        }

        return null
    }


}