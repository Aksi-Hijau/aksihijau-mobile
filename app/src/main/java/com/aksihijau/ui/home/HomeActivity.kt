package com.aksihijau.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.aksihijau.databinding.ActivityHomeBinding
import com.aksihijau.datastore.TokenPreferences
import com.aksihijau.datastore.TokenViewModel
import com.aksihijau.datastore.TokenViewModelFactory
import com.aksihijau.ui.view.SplashScreen

class HomeActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
    private lateinit var tokenViewModel: TokenViewModel

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
    }
    private fun setupViewModel(){
        val pref = TokenPreferences.getInstance(dataStore)
        tokenViewModel = ViewModelProvider(this, TokenViewModelFactory(pref))[TokenViewModel::class.java]
    }

    private fun setupAction(){
        binding.logoutButton.setOnClickListener {
            tokenViewModel.saveToken("")
            tokenViewModel.saveRefreshToken("")
            tokenViewModel.saveLoginSetting(false)

            val intent = Intent(this, SplashScreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}