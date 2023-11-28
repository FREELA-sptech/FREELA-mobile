package com.example.freela.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.freela.R
import com.example.freela.api.AuthService
import com.example.freela.api.OrderService
import com.example.freela.databinding.ActivityRegisterThirdBinding
import com.example.freela.databinding.ActivityUserDetailsBinding
import com.example.freela.model.Session
import com.example.freela.model.User
import com.example.freela.network.RetrofitClient
import com.example.freela.viewModel.OrderViewModel
import com.example.freela.viewModel.UserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailsActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityUserDetailsBinding.inflate(layoutInflater);
    }

    private lateinit var userDetails: User
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val userService = RetrofitClient.getInstance().create(AuthService::class.java)
        val user = Session.user
        userViewModel = UserViewModel(userService)

        binding.btnreturn.setOnClickListener{
            val intent = Intent(this, BaseAuthenticatedActivity::class.java)
            startActivity(intent)
        }

        binding.editUser.setOnClickListener{
            val intent = Intent(this, EditUser::class.java)
            startActivity(intent)
        }

        binding.changeImage.setOnClickListener{
            showDialog()
        }

        user?.let {
            binding.textView2.text = user.name

            if (user.isFreelancer){
                binding.textDescription.text = user.description
            }else{
                binding.textDescription.visibility = View.GONE
            }
            if (user.photo == "") {
                binding.imgWithoutImage.text = user.name.first().toString()
            }
            if (user.city == "" && user.uf == "" ){
                binding.textCity.text = "Sem registro"
            }else {
                binding.textCity.text = "${user.city} / ${user.uf}"
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0) {
            val imageUri = data?.data
            val imageBase64 = imageUri?.let { convertImageToBase64(it) }
            val token = Session.token

            if (imageBase64 != null && token != null) {
                userViewModel.updateUserPhoto(token, imageBase64)
            }
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheetuploadimage)
        val uploadImage = dialog.findViewById<RelativeLayout>(R.id.uploadImage)
        val close = dialog.findViewById<ImageView>(R.id.close)

        close.setOnClickListener{
            dialog.dismiss()
            Toast.makeText(this,"Selecionar Image",Toast.LENGTH_SHORT).show()
        }

        uploadImage.setOnClickListener{
            selectPhoto()
            dialog.dismiss()
        }

        dialog.show();
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        startActivityForResult(intent,0);
    }

    private fun convertImageToBase64(imageUri: Uri): String {
        val inputStream = contentResolver.openInputStream(imageUri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()

        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}