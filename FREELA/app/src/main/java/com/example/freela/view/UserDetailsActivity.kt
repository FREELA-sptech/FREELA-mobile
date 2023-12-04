package com.example.freela.view

import okhttp3.MediaType
import okhttp3.RequestBody
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Base64
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.example.freela.R
import com.example.freela.api.AuthService
import com.example.freela.databinding.ActivityUserDetailsBinding
import com.example.freela.model.Session
import com.example.freela.model.User
import com.example.freela.network.RetrofitClient
import com.example.freela.viewModel.UserViewModel
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MultipartBody
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream

class UserDetailsActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityUserDetailsBinding.inflate(layoutInflater)
    }

    private lateinit var userDetails: User
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val userService = RetrofitClient.getInstance().create(AuthService::class.java)
        val user = Session.user
        userViewModel = UserViewModel(userService)

        binding.btnreturn.setOnClickListener {
            val intent = Intent(this, BaseAuthenticatedActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.editUser.setOnClickListener {
            val intent = Intent(this, EditUser::class.java)
            startActivity(intent)
            finish()
        }

        binding.changeImage.setOnClickListener {
            showDialog()
        }

        user?.let {
            binding.textView2.text = user.name

            if (user.isFreelancer) {
                binding.textDescription.text = user.description
                binding.proposals.visibility = View.VISIBLE
                binding.orders.visibility = View.GONE
                binding.proposals.setOnClickListener {
                    val intent = Intent(this, EditUser::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                binding.proposals.visibility = View.GONE
                binding.orders.visibility = View.VISIBLE
                binding.titleAbout.visibility = View.GONE
                binding.textDescription.visibility = View.GONE
            }
            if (user.photo == "") {
                binding.imgWithoutImage.text = user.name.first().toString()
            }else{
                val userDetailsImageView = binding.userDetails
                userDetailsImageView.visibility = View.VISIBLE
                val byteArray = Base64.decode(user.photo, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(byteArray))
                binding.changeImage.visibility = View.GONE
                userDetailsImageView.setImageBitmap(bitmap)
                userDetailsImageView.setOnClickListener {
                    showDialog()
                }            }
            if (user.city == "" && user.uf == "") {
                binding.textCity.text = "Sem registro"
            } else {
                binding.textCity.text = "${user.city} / ${user.uf}"
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == RESULT_OK) {
            val imageUri = data?.data
            val token = Session.token

            if (imageUri != null && token != null) {
                val imagePart = convertImageToMultipart(imageUri)
                if (imagePart != null) {
                    userViewModel.updateUserPhoto(token, imagePart)
                } else {
                    // Tratamento se a conversão falhar
                }
            }
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheetuploadimage)
        val uploadImage = dialog.findViewById<RelativeLayout>(R.id.uploadImage)
        val close = dialog.findViewById<ImageView>(R.id.close)

        close.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(this, "Selecionar Imagem", Toast.LENGTH_SHORT).show()
        }

        uploadImage.setOnClickListener {
            selectPhoto()
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
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
