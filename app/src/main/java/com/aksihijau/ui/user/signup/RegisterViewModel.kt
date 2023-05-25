package com.aksihijau.ui.user.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aksihijau.api.ApiConfig
import com.aksihijau.api.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val isSukses = MutableLiveData<Boolean>()

    fun register(name: String, email: String, password: String){
        val apiService = ApiConfig.getApiService()
        val registerRequest = apiService.register(name, email, password)
        isLoading.postValue(true)
        registerRequest.enqueue(object: Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                isLoading.postValue(false)
                if(response.isSuccessful){
                    isSukses.postValue(true)
                }else {
                    isSukses.postValue(false)
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                isLoading.postValue(false)
                isSukses.postValue(false)
            }

        })
    }
}