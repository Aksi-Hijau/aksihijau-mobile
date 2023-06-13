package com.aksihijau.ui.payment.detailinstruksi

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aksihijau.api.ApiConfig
import com.aksihijau.api.DataDetailInstruksi
import com.aksihijau.api.DetailInstruksiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailInstruksiViewModel : ViewModel() {
    var dataDetailInstruksi = MutableLiveData<DataDetailInstruksi?>()

    fun getDetailInstruksi(token:String, rtoken:String, inv:String){
        val apiService = ApiConfig.getApiService()
        val r = apiService.getDetailInstruksi(token, rtoken, inv)
        r.enqueue(object : Callback<DetailInstruksiResponse>{
            override fun onResponse(
                call: Call<DetailInstruksiResponse>,
                response: Response<DetailInstruksiResponse>
            ) {
                if(response.isSuccessful){
                    dataDetailInstruksi.postValue(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<DetailInstruksiResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}