package com.example.capstone.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.capstone.data.local.UserSession
import com.example.capstone.databinding.ActivityTestModelBinding
import com.example.capstone.ml.AiCareModel
import com.example.capstone.ui.viewmodel.TestModelViewModel
import com.example.capstone.ui.viewmodel.factory.ViewModelFactory
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*


class TestModelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestModelBinding
    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private lateinit var tvOutput: TextView

    private lateinit var cataractOutput: TextView
    private lateinit var glaucomaOutput: TextView
    private lateinit var normalOutput: TextView

    private lateinit var uploadButton: Button
    private var modelFile: File? = null

    private val GALLERY_REQUEST_CODE = 123

    private lateinit var testModelViewModel: TestModelViewModel

    val timeStamp: String = SimpleDateFormat(
        Companion.FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestModelBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        imageView = binding.imageView
        button = binding.btnCaptureImage
        tvOutput = binding.tvResult

        cataractOutput = binding.tvCataractResult
        glaucomaOutput = binding.tvGlaucomaResult
        normalOutput = binding.tvNormalResult

        uploadButton = binding.btnUploadImage

        val buttonLoad = binding.btnLoadImage

        button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
            ) {
                takePicturePreview.launch(null)
            }
            else {
                requestPermission.launch(android.Manifest.permission.CAMERA)
            }
        }

        buttonLoad.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg","image/png","image/jpg")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                onresult.launch(intent)
            }else {
                requestPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        tvOutput.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${tvOutput.text}"))
            startActivity(intent)
        }

        val pref = UserSession.getInstance(dataStore)
        testModelViewModel = ViewModelProvider(this, ViewModelFactory(pref, this))[TestModelViewModel::class.java]
    }

    //request camera permission
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){ granted->
        if (granted){
            takePicturePreview.launch(null)
        }else {
            Toast.makeText(this, "Permission Denied !! Try again", Toast.LENGTH_SHORT).show()
        }
    }

    //launch camera and take picture
    private val takePicturePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){ bitmap->
        if(bitmap != null){
            imageView.setImageBitmap(
                Bitmap.createScaledBitmap(
                bitmap, 512, 512, true
            ))
            outputGenerator(bitmap)
        }
    }

    //to get image from gallery
    private val onresult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        Log.i("TAG", "This is the result: ${result.data} ${result.resultCode}")
        onResultReceived(GALLERY_REQUEST_CODE,result)
    }

    private fun onResultReceived(requestCode: Int, result: ActivityResult?){
        when(requestCode){
            GALLERY_REQUEST_CODE ->{
                if (result?.resultCode == Activity.RESULT_OK){
                    result.data?.data?.let{uri ->
                        Log.i("TAG", "onResultReceived: $uri")
                        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                        imageView.setImageBitmap(
                            Bitmap.createScaledBitmap(
                            bitmap, 512, 512, true
                        ))
                        outputGenerator(bitmap)
                    }
                }else {
                    Log.e("TAG", "onActivityResult: error in selecting image")
                }
            }
        }
    }

    private fun outputGenerator(bitmap: Bitmap){
        val aiCareModel = AiCareModel.newInstance(this)

        val newBitmap = Bitmap.createScaledBitmap(
            bitmap, 512, 512, true
        )
//        val buffer = ByteBuffer.allocate(1024 * 1024 * 3)
//        buffer.rewind()
//        newBitmap.copyPixelsToBuffer(buffer)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 512, 512, 3), DataType.FLOAT32)

        val imageProcessor = ImageProcessor.Builder()
            .add(
                ResizeOp(
                    512,
                    512,
                    ResizeOp.ResizeMethod.BILINEAR
                )
            ) //.add(new NormalizeOp(127.5f, 127.5f))
            .build()

        var tImage = TensorImage(DataType.FLOAT32)

        tImage.load(newBitmap)
        tImage = imageProcessor.process(tImage)

//        inputFeature0.loadBuffer(buffer)
        inputFeature0.loadBuffer(tImage.buffer)

        val outputs = aiCareModel.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        var temp = 0f
        var index = 0
        for (i in 0..2) {
            val current = outputFeature0.getFloatValue(i)
            val currentString = String.format("%.2f", outputFeature0.getFloatValue(i) * 100) + "%"
            if (outputFeature0.getFloatValue(i) > temp) {
                temp = current
                index = i
            }
            when (i) {
                0 -> cataractOutput.text = currentString
                1 -> glaucomaOutput.text = currentString
                2 -> normalOutput.text = currentString
            }
        }

        var label = ""

        when (index) {
            0 -> label = "Cataract"
            1 -> label = "Glaucoma"
            2 -> label = "Normal"
        }

        tvOutput.text = label
        Log.i("TAG", "outputGenerator: $label")

        uploadButton.isEnabled = true
        uploadButton.setOnClickListener {
            testModelViewModel.userToken.observe(this) { token ->
                testModelViewModel.uploadResult(token, label, temp, this)
            }
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }

    }

    companion object {
        private const val FILENAME_FORMAT = "dd-MMM-yyyy"
    }
}