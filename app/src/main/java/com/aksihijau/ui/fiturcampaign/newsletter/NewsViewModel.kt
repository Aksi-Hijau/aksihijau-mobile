package com.aksihijau.ui.fiturcampaign.newsletter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aksihijau.api.ApiConfig
import com.aksihijau.api.campaignresponse.Donation
import com.aksihijau.api.campaignresponse.Report
import com.aksihijau.api.campaignresponse.ReportResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _report = MutableLiveData<List<Report>>()
    val reports: LiveData<List<Report>> = _report

    fun getNewsletter(slug : String){
        _isLoading.value = true

        val apiService = ApiConfig.getApiService()
        val ReportRequest = apiService.getReports(slug)
        ReportRequest.enqueue(object : Callback<ReportResponse>{
            override fun onResponse(
                call: Call<ReportResponse>,
                response: Response<ReportResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    val reportResponse = response.body()?.data
                    reportResponse?.let { data ->
                        _report.value = data
                    }
                    _isSuccess.value = true
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                _isLoading.value = false
                _isSuccess.value = false
            }

        })
    }

}