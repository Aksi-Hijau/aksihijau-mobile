package com.aksihijau.ui.navigationmenu.profile

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ThumbnailUtils
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.aksihijau.R
import com.aksihijau.api.ApiConfig
import com.aksihijau.api.UserResponse
import com.aksihijau.databinding.ActivityUpdateUserBinding
import com.aksihijau.datastore.TokenPreferences
import com.aksihijau.datastore.TokenViewModel
import com.aksihijau.datastore.TokenViewModelFactory
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.UUID
import kotlin.math.min

class UpdateUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateUserBinding
    private var getFile: File? = null
    private lateinit var tokenViewModel: TokenViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = TokenPreferences.getInstance(dataStore)
        tokenViewModel = ViewModelProvider(this, TokenViewModelFactory(pref))[TokenViewModel::class.java]

        binding.materialToolbar6.setNavigationOnClickListener {
            finish()
        }

        binding.imageCompany2.setOnClickListener{
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 1)
        }

        val name = intent.getStringExtra(EXTRA_NAME)
        val email = intent.getStringExtra(EXTRA_EMAIL)

        binding.name.setText(name)

        binding.email.setText(email)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                val selectedImageUri = data?.data as Uri

                val myFile = uriToFile(selectedImageUri, this)
                getFile = myFile
                binding.imageCompany2.setImageURI(selectedImageUri)

                binding.btSave.setOnClickListener {
                    if(binding.name.text.toString().isEmpty()){
                        Toast.makeText(this, "Masukkan Nama", Toast.LENGTH_SHORT).show()
                    }else if(binding.email.text.toString().isEmpty()){
                        Toast.makeText(this, "Masukkan Email", Toast.LENGTH_SHORT).show()
                    }else {
                        uploadImage(getFile!!, binding.name.text.toString(), binding.email.text.toString());
                    }
                }
            }
        }
    }

    private fun uploadImage(file: File, _name: String, _email : String) {
//        if (getFile != null) {


            val name = _name.toRequestBody("text/plain".toMediaType())
            val email = _email.toRequestBody("text/plain".toMediaType())
            val birthDate = "2002-06-26".toRequestBody("text/plain".toMediaType())

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

        tokenViewModel.getToken().observe(this){token->
            tokenViewModel.getRefreshToken().observe(this){refreshToken->
            val apiService = ApiConfig.getApiService()
            val updateProfile = apiService.updateProfile(token, refreshToken, imageMultipart, name, email, birthDate)
            updateProfile.enqueue(object : retrofit2.Callback<UserResponse>{
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if(response.isSuccessful){
                        Toast.makeText(this@UpdateUserActivity, "Update Pengguna Berhasil", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Toast.makeText(this@UpdateUserActivity, "Update Pengguna Gagal", Toast.LENGTH_SHORT).show()

                }

            })

            }
        }
    }

    fun rotateFile(file: File, isBackCamera: Boolean = false) {
        val matrix = Matrix()
        val bitmap = BitmapFactory.decodeFile(file.path)
        val rotation = if (isBackCamera) 90f else -90f
        matrix.postRotate(rotation)
        if (!isBackCamera) {
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        }
        val result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        result.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
    }

    companion object {
        val EXTRA_NAME = "extra_name"
        val EXTRA_EMAIL = "extra_email"
    }
}