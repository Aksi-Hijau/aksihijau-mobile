package com.aksihijau.ui.user.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.aksihijau.databinding.ActivityRegisterBinding
import com.aksihijau.ui.user.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel(){
        registerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            RegisterViewModel::class.java)

        registerViewModel.isLoading.observe(this){
            binding.progressBar.visibility = if(it) View.VISIBLE else View.INVISIBLE
        }
        
        registerViewModel.isSukses.observe(this) {
            if(it){
                Toast.makeText(this, "Register Sukses", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }else {
                Toast.makeText(this, "Register Gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setupAction(){

        binding.signupButton.setOnClickListener {
            if((binding.textFieldName.text?.length ?: 0) == 0){
                Toast.makeText(this, "Kolom Nama Kosong", Toast.LENGTH_SHORT).show()
            }else if((binding.textFieldEmail.text?.length ?: 0) == 0){
                Toast.makeText(this, "Kolom Email Kosong", Toast.LENGTH_SHORT).show()
            }else if((binding.textFieldPassword.text?.length ?: 0) == 0){
                Toast.makeText(this, "Kolom Password Kosong", Toast.LENGTH_SHORT).show()
            }else if(!checkEmail(binding.textFieldEmail.text.toString())){
                Toast.makeText(this, "Format Email tidak sesuai", Toast.LENGTH_SHORT).show()
            }else if((binding.textFieldPassword.text?.length ?: 0) < 6){
                Toast.makeText(this, "Karakter Password Minimal 6 karakter", Toast.LENGTH_SHORT).show()
            }else {
                val name = binding.textFieldName.text.toString()
                val email = binding.textFieldEmail.text.toString()
                val password = binding.textFieldPassword.text.toString()
                registerViewModel.register(name, email, password)
            }
        }

        binding.loginTextButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun checkEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}