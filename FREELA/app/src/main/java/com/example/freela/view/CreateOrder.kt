package com.example.freela.view

import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.freela.adapters.CarouselAdapter
import com.example.freela.adapters.ViewAdapterOrder
import com.example.freela.api.OrderService
import com.example.freela.databinding.ActivityCreateOrderBinding
import com.example.freela.databinding.ActivityCreateProposalBinding
import com.example.freela.model.Session
import com.example.freela.model.dto.request.OrderRequest
import com.example.freela.model.dto.request.ProposalRequest
import com.example.freela.network.RetrofitClient
import com.example.freela.util.helpers.Helpers
import com.example.freela.viewModel.OrderViewModel
import com.example.freela.viewModel.ProposalViewModel
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.InputStream

class CreateOrder : AppCompatActivity() {
    private val binding by lazy {
        ActivityCreateOrderBinding.inflate(layoutInflater)
    }
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var changeImage: RelativeLayout
    private lateinit var viewPager: ViewPager
    private lateinit var imageUri: String
    private lateinit var chooseImageList: MutableList<MultipartBody.Part>
    private lateinit var chooseImageListArray: ArrayList<Uri>
    private lateinit var imageRV: RecyclerView
    private lateinit var imageAdapter: CarouselAdapter
    private lateinit var photos: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val helpers = Helpers();
        changeImage = binding.chooseImage
        imageRV = binding.imgView
        imageAdapter = CarouselAdapter()

        val orderService = RetrofitClient.getInstance().create(OrderService::class.java)
        orderViewModel = OrderViewModel(orderService)

        binding.createOrder.setOnClickListener{
            val inputFields = listOf<TextInputEditText>(
                binding.title,
                binding.description,
                binding.deadline,
                binding.value,
            )

            if (helpers.isInputValid(inputFields)) {
                performRegistration()
                onBackPressed()
                finish()
            }
        }

        changeImage.setOnClickListener {
            PickImageFromGalery()
        }
    }

    private fun PickImageFromGalery() {
        var intent = Intent()
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.clipData != null) {
            val clipData = data.clipData
            val token = Session.token

            if (clipData != null && token != null) {
                for (i in 0 until clipData.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    chooseImageListArray.add(imageUri)
                    val imagePart = convertImageToMultipart(imageUri)
                    photos.add()

                    if (imagePart != null) {
                        chooseImageList.add(imagePart)
                    }
                    SetAdapter()
                }
            }
        }
    }

    private fun SetAdapter() {
        imageRV.adapter = imageAdapter
        imageAdapter.submitList(chooseImageList)
    }


    private fun performRegistration() {
        val description = binding.description?.text.toString()
        val title = binding.title?.text.toString()
        val deadline = binding.deadline?.text.toString()
        val value = binding.value?.text.toString()
        val floatValue = value.toFloatOrNull() ?: 0.0f

        val orderRequest = OrderRequest(
            description,title,floatValue,listOf(1, 2, 3, 4, 5),deadline
        )
        orderViewModel.createOrder(orderRequest)


    }

    private fun convertImageToMultipart(imageUri: Uri): MultipartBody.Part? {
        val contentResolver: ContentResolver = applicationContext.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(imageUri)

        inputStream?.let {
            val file = File(cacheDir, contentResolver.getFileName(imageUri))
            file.copyInputStreamToFile(inputStream)

            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            return MultipartBody.Part.createFormData("image", file.name, requestFile)
        }

        return null
    }

    private fun ContentResolver.getFileName(imageUri: Uri): String {
        var name = ""
        val returnCursor = this.query(imageUri, null, null, null, null)
        returnCursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            name = it.getString(nameIndex)
        }
        returnCursor?.close()
        return name
    }

    private fun File.copyInputStreamToFile(inputStream: InputStream) {
        this.outputStream().use { fileOut ->
            inputStream.copyTo(fileOut)
        }
    }
}