package com.aksihijau.ui.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.aksihijau.databinding.ActivityOnBoardingBinding
import com.aksihijau.datastore.TokenPreferences
import com.aksihijau.datastore.TokenViewModel
import com.aksihijau.datastore.TokenViewModelFactory
import com.aksihijau.ui.navigationmenu.BottomNavigationActivity
import com.aksihijau.ui.user.login.LoginActivity

class OnBoarding : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
    private lateinit var tokenViewModel: TokenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        setupAction()
    }

    private fun setupViewModel(){
        val pref = TokenPreferences.getInstance(dataStore)
        tokenViewModel = ViewModelProvider(this, TokenViewModelFactory(pref))[TokenViewModel::class.java]

        tokenViewModel.getLoginSettings().observe(this){
            if(it){
                startActivity(Intent(this, BottomNavigationActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction(){
        binding.button.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}