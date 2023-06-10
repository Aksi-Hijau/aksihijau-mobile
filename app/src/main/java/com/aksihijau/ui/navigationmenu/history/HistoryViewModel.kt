package com.aksihijau.ui.navigationmenu.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aksihijau.api.ApiConfig
import com.aksihijau.api.DataItemDonationHistories
import com.aksihijau.api.DonationHistoriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is history Fragment"
    }
    val text: LiveData<String> = _text

    var histories = MutableLiveData<List<DataItemDonationHistories>>()
    fun getDonationHistories(token: String, refreshToken: String){
        val apiService = ApiConfig.getApiService()
        val donationReq = apiService.getDonationHistories("Bearer " + token, refreshToken)
        donationReq.enqueue(object : Callback<DonationHistoriesResponse> {
            override fun onResponse(
                call: Call<DonationHistoriesResponse>,
                response: Response<DonationHistoriesResponse>
            ) {
                if(response.isSuccessful){
                    histories.postValue(response.body()!!.data as List<DataItemDonationHistories>)
                }
            }

            override fun onFailure(call: Call<DonationHistoriesResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}