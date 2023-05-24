package com.aksihijau.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.aksihijau.databinding.ActivityLoginBinding
import com.aksihijau.datastore.TokenPreferences
import com.aksihijau.datastore.TokenViewModel
import com.aksihijau.datastore.TokenViewModelFactory
import com.aksihijau.ui.home.HomeActivity
import com.aksihijau.ui.signup.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var tokenViewModel: TokenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel(){
        val pref = TokenPreferences.getInstance(dataStore)
        tokenViewModel = ViewModelProvider(this, TokenViewModelFactory(pref))[TokenViewModel::class.java]

        loginViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[LoginViewModel::class.java]

        loginViewModel.isLoading.observe(this){
            binding.progressBar2.visibility = if(it) View.VISIBLE else View.INVISIBLE
        }

        loginViewModel.isSukses.observe(this){
            if(it){
                tokenViewModel.saveLoginSetting(true)
                Toast.makeText(this, "Login Sukses", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }else {
                Toast.makeText(this, "Login Gagal", Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.token.observe(this){
            tokenViewModel.saveToken(it)
        }

        loginViewModel.refreshToken.observe(this){
            tokenViewModel.saveRefreshToken(it)
        }


    }
    private fun setupAction(){
        binding.loginButton.setOnClickListener {
            if((binding.textFieldEmail.text?.length ?: 0) == 0){
                Toast.makeText(this, "Kolom Email Kosong", Toast.LENGTH_SHORT).show()
            }else if((binding.textFieldPassword.text?.length ?: 0) == 0){
                Toast.makeText(this, "Kolom Password Kosong", Toast.LENGTH_SHORT).show()
            }else if(!checkEmail(binding.textFieldEmail.text.toString())){
                Toast.makeText(this, "Format Email tidak sesuai", Toast.LENGTH_SHORT).show()
            }else if((binding.textFieldPassword.text?.length ?: 0) < 6){
                Toast.makeText(this, "Karakter Password Minimal 6 karakter", Toast.LENGTH_SHORT).show()
            }else {
                val email = binding.textFieldEmail.text.toString()
                val password = binding.textFieldPassword.text.toString()
                loginViewModel.login(email, password)
            }
        }

        binding.signupTextButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }
    private fun checkEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}