package com.aksihijau.ui.navigationmenu.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aksihijau.api.ApiConfig
import com.aksihijau.api.DataHistoryDetail
import com.aksihijau.api.HistoryDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryDetailViewModel : ViewModel() {
    var historyDetail = MutableLiveData<DataHistoryDetail?>()

    fun getHistoryDetail(token:String, refreshToken:String, invoice:String){
        val apiService = ApiConfig.getApiService()
        val r = apiService.getHistoryDetail(token, refreshToken, invoice)
        r.enqueue(object : Callback<HistoryDetailResponse>{
            override fun onResponse(
                call: Call<HistoryDetailResponse>,
                response: Response<HistoryDetailResponse>
            ) {
                if(response.isSuccessful){
                    historyDetail.postValue(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<HistoryDetailResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}