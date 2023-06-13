package com.aksihijau.ui.navigationmenu.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aksihijau.api.ApiConfig
import com.aksihijau.api.DataUser
import com.aksihijau.api.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {
    var user = MutableLiveData<DataUser?>()

    private val _text = MutableLiveData<String>().apply {
        value = "This is Profile Fragment"
    }
    val text: LiveData<String> = _text

    fun getUser(token: String, rtoken: String){
        val apiService = ApiConfig.getApiService()
        val userResp = apiService.getUser("Bearer $token", rtoken)
        userResp.enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if(response.isSuccessful){
                    Log.d("ProfileVewModel", "onResponse: ${response.body()}")
                    user.postValue(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}