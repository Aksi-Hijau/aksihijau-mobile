package com.aksihijau.ui.makecampaign.SoilAnalysis

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aksihijau.R
import com.aksihijau.databinding.ActivitySoilAnalysisBinding
import com.aksihijau.ml.Soilmodel96
import com.aksihijau.ui.fiturcampaign.soil.SoilActivity
import com.aksihijau.ui.webview.Makecampaign_webview_activity
import okio.IOException
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.min

class SoilAnalysisActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySoilAnalysisBinding
    private val imageSize = 224


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 3)
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoilAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("Soil Analysis")
        }
        binding.toolbar.root.setTitleTextColor(resources.getColor(R.color.white))

        getPermission()
        onClick()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getPermission() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun onClick(){
        binding.btnCameraX.setOnClickListener { startCamera() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCheck.setOnClickListener { checksoil() }
        binding.btnMakecampaignbysoil.setOnClickListener { makecampaign() }
    }

    private fun makecampaign() {
        val intent = Intent(this, Makecampaign_webview_activity::class.java)
        startActivity(intent)
    }

    private fun startCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, 3)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        }
    }



    private fun checksoil() {
        val resultText = binding.tvHasil.text.toString()
        val id = when (resultText) {
            "Regosol" -> 1
            "Mediteran" -> 2
            "Aluvial" -> 3
            "Organosol" -> 4
            "Podsolit" -> 5
            "Andosol" -> 6
            else -> -1
        }

        if (id != -1) {
            val intent = Intent(this, SoilActivity::class.java)
            intent.putExtra("soilId", id)
            startActivity(intent)
        }
    }


    private fun startGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, 1)
    }

    private fun classifyImage(image: Bitmap) {
        try {
            val model = Soilmodel96.newInstance(applicationContext)

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, imageSize, imageSize, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())

            val intValues = IntArray(imageSize * imageSize)
            image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
            var pixel = 0

            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val value = intValues[pixel++]
                    byteBuffer.putFloat(((value shr 16) and 0xFF) * (1f / 255))
                    byteBuffer.putFloat(((value shr 8) and 0xFF) * (1f / 255))
                    byteBuffer.putFloat((value and 0xFF) * (1f / 255))
                }
            }

            inputFeature0.loadBuffer(byteBuffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.getOutputFeature0AsTensorBuffer()

            val confidences = outputFeature0.floatArray
            var maxPos = 0
            var maxConfidence = 0f

            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }

            val classes = arrayOf("Aluvial", "Andosol", "Mediteran", "Organosol", "Podsolit", "Regosol")
            binding.tvHasil.text = classes[maxPos]

            model.close()
        } catch (e: IOException) {
            // TODO: Handle the exception
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                val image = data?.extras?.get("data") as Bitmap
                val dimension = min(image.width, image.height)
                val croppedImage = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
                binding.ivPreview.setImageBitmap(croppedImage)

                val resizedImage = Bitmap.createScaledBitmap(croppedImage, imageSize, imageSize, false)
                classifyImage(resizedImage)
            } else if (requestCode == 1) {
                val selectedImageUri = data?.data
                val inputStream = contentResolver.openInputStream(selectedImageUri!!)
                val image = BitmapFactory.decodeStream(inputStream)
                binding.ivPreview.setImageBitmap(image)

                val resizedImage = Bitmap.createScaledBitmap(image, imageSize, imageSize, false)
                classifyImage(resizedImage)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}













