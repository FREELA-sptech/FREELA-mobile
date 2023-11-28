package com.example.freela.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.freela.R
import com.example.freela.api.AuthService
import com.example.freela.databinding.ActivityUpdateCityUserBinding
import com.example.freela.model.Session
import com.example.freela.model.Session.user
import com.example.freela.model.User
import com.example.freela.model.dto.request.UserDetailsRequest
import com.example.freela.network.RetrofitClient
import com.example.freela.util.helpers.Helpers
import com.example.freela.viewModel.UserViewModel
import com.google.android.material.textfield.TextInputEditText

class UpdateCityUser : AppCompatActivity() {
    private val binding by lazy {
        ActivityUpdateCityUserBinding.inflate(layoutInflater)
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
        binding.city.setText(user.city)
        binding.state.setText(user.uf)

        binding.btnreturn.setOnClickListener {
            onBackPressed()
        }

        binding.btnNext.setOnClickListener {
            Log.i("Editar usuario", "quase sucesso")
            val inputFields = listOf<TextInputEditText>(
                binding.city,
                binding.state,
            )

            if (helpers.isInputValid(inputFields)) {
                performRegistration()
                finish()
            }
        }
    }

    private fun performRegistration() {
        val user = Session.user

        val city = binding.city.text.toString()
        val state = binding.state.text.toString()

        if(user != null){
            val selectedSubCategoryIds = user.subCategories.map { it.id }
            val userUpdate = if (user.description.isNullOrBlank()) {
                UserDetailsRequest(
                    user.name,city, state,"" ,selectedSubCategoryIds, "", user.photo
                )
            } else {
                UserDetailsRequest(
                    user.name, city, state,user.description ,selectedSubCategoryIds, "", user.photo
                )
            }

            Log.i("Editar usuario", "quase sucesso")
            userViewModel.updateUserDetails(Session.token,userUpdate)
            onBackPressed()
        }
    }
}