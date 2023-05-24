package com.aksihijau.datastore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TokenViewModel(private val pref: TokenPreferences)  : ViewModel() {
    fun getLoginSettings(): LiveData<Boolean> {
        return pref.getLoginSetting().asLiveData()
    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun saveLoginSetting(isLogin: Boolean) {
        viewModelScope.launch {
            pref.saveLoginSetting(isLogin)
        }
    }

    fun saveToken(token: String){
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    fun saveRefreshToken(refreshToken: String){
        viewModelScope.launch {
            pref.saveRefreshToken(refreshToken)
        }
    }
}

class TokenViewModelFactory(private val pref: TokenPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TokenViewModel::class.java)) {
            return TokenViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}