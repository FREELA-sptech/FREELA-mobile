package com.example.freela.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.freela.api.AuthService
import com.example.freela.databinding.ActivityUpdateNameUserBinding
import com.example.freela.model.Session
import com.example.freela.model.User
import com.example.freela.model.dto.request.UserDetailsRequest
import com.example.freela.network.RetrofitClient
import com.example.freela.util.helpers.Helpers
import com.example.freela.viewModel.UserViewModel
import com.google.android.material.textfield.TextInputEditText

class UpdateNameUser : AppCompatActivity() {
    private val binding by lazy {
        ActivityUpdateNameUserBinding.inflate(layoutInflater)
    }
    private lateinit var userViewModel: UserViewModel
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val authService = RetrofitClient.getInstance().create(AuthService::class.java)
        userViewModel = UserViewModel(authService)
        val helpers = Helpers();
        user = Session.user!!
        if(user.isFreelancer){
            binding.name.setText(user.name)
            binding.description.setText(user.description)
            binding.btnNext.setOnClickListener {
                val inputFields = listOf<TextInputEditText>(
                    binding.name,
                    binding.description,
                )

                if (helpers.isInputValid(inputFields)) {
                    val redirect = Intent(this, SuccessActivity::class.java)
                    val intent = Intent(this, Login::class.java)
                    redirect.putExtra("action","Cadastro")
                    redirect.putExtra("message","Cadastro de usuario realizado com sucesso!")
                    redirect.putExtra("redirect",intent)
                    performRegistration()
                    startActivity(redirect)
                    finish()
                }
            }
        }else{
            binding.name.setText(user.name)
            binding.description.visibility = View.GONE
            binding.btnNext.setOnClickListener {
                val inputFields = listOf<TextInputEditText>(
                    binding.name
                )

                if (helpers.isInputValid(inputFields)) {
                    val redirect = Intent(this, SuccessActivity::class.java)
                    val intent = Intent(this, Login::class.java)
                    redirect.putExtra("action","Cadastro")
                    redirect.putExtra("message","Cadastro de usuario realizado com sucesso!")
                    redirect.putExtra("redirect",intent)
                    performRegistration()
                    startActivity(redirect)
                    finish()
                }
            }
        }


        binding.btnreturn.setOnClickListener {
            onBackPressed()
        }
    }
    private fun performRegistration() {

        val nome = binding.name?.text.toString()
        val description = binding.description?.text.toString()

        if(user != null){
            if(user.isFreelancer){
                val selectedSubCategoryIds = user.subCategories?.map { it.id }
                val userUpdate = selectedSubCategoryIds?.let {
                    UserDetailsRequest(
                        nome, user.city, user.uf,description , it, "", user.photo
                    )
                }
                if (userUpdate != null) {
                    userViewModel.updateUserDetails(Session.token,userUpdate)
                    onBackPressed()
                }
            }else{
                val selectedSubCategoryIds = user.subCategories?.map { it.id }
                val userUpdate = selectedSubCategoryIds?.let {
                    UserDetailsRequest(
                        nome, user.city, user.uf,"", it, "", user.photo
                    )
                }
                if (userUpdate != null) {
                    userViewModel.updateUserDetails(Session.token,userUpdate)
                    onBackPressed()
                }
            }

        }
    }
}