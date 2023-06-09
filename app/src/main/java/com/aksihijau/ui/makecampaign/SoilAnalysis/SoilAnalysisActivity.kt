package com.aksihijau.ui.makecampaign.SoilAnalysis

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aksihijau.R
import com.aksihijau.databinding.ActivitySoilAnalysisBinding
import com.aksihijau.ml.Soilmodel96
import okio.IOException
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.min

class SoilAnalysisActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySoilAnalysisBinding
    private var bitmap : Bitmap? = null
    private val imageSize = 224


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.invalid_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoilAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
//        binding.btnCameraX.setOnClickListener { startCamera() }
        binding.btnGallery.setOnClickListener { startGallery() }
//        binding.btnCheck.setOnClickListener { predic() }
    }


    private fun startGallery() {
//        val intent = Intent()
//        intent.action = Intent.ACTION_GET_CONTENT
//        intent.type = "image/*"
//        val chooser = Intent.createChooser(intent, "Choose a Picture")
//        launcherIntentGallery.launch(chooser)
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, 1)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri? = result.data?.data
            selectedImg?.let {
                val inputStream = contentResolver.openInputStream(selectedImg)
                bitmap = BitmapFactory.decodeStream(inputStream)
                binding.ivPreview.setImageBitmap(bitmap)
            }
        }
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

    private fun predic(){
        var tensorImage = TensorImage(DataType.FLOAT32)
        val classes = arrayOf("Aluvial", "Andosol", "Mediteran", "Organosol", "Podsolit", "Regosol")
        var imageProcessor = ImageProcessor.Builder()
//            .add(NormalizeOp(0.0f, 255.0f))
//            .add(TransformToGrayscaleOp())
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .build()
        tensorImage.load(bitmap)

        tensorImage = imageProcessor.process(tensorImage)

        val model = Soilmodel96.newInstance(this)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorImage.buffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

        var maxIdx = 0
        outputFeature0.forEachIndexed{ index, fl->
            if(outputFeature0[maxIdx] < fl){
                maxIdx = index
            }
        }


        binding.tvHasil.setText(classes[maxIdx])


        // Releases model resources if no longer used.
        model.close()
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}













