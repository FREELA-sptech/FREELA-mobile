package com.example.freela.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.freela.R
import com.example.freela.SuccessActivity
import com.example.freela.api.AuthService
import com.example.freela.databinding.ActivityRegisterThirdBinding
import com.example.freela.model.User
import com.example.freela.model.dto.request.LoginRequest
import com.example.freela.model.dto.request.RegisterRequest
import com.example.freela.model.dto.response.LoginResponse
import com.example.freela.network.RetrofitClient
import com.example.freela.util.helpers.Helpers
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class activity_register_third : AppCompatActivity() {
    private val binding by lazy {
        ActivityRegisterThirdBinding.inflate(layoutInflater);
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val helpers = Helpers();

        binding.btnreturn.setOnClickListener {
            onBackPressed()
        }

        binding.register.setOnClickListener {
            val inputFields = listOf<TextInputEditText>(
                binding.name,
                binding.email,
                binding.password
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

    private fun performRegistration() {
        val cardSelected = intent.getStringExtra("type")
        val subCategoriesIds = intent.getIntArrayExtra("subCategoriesIds")
        val subCategoriesIdsList = subCategoriesIds?.toList()
        val nome = binding.name?.text.toString()
        val email = binding.email?.text.toString()
        val senha = binding.password?.text.toString()
        val type = cardSelected == "Autônomo"

        val userRequest = subCategoriesIdsList?.let {
            RegisterRequest(
                nome,email,senha, it,"","",type
            )
        }

        if (userRequest != null) {
            RetrofitClient.getInstance()
                .create(AuthService::class.java)
                .register(userRequest)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(baseContext, "Registro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(baseContext, "Falha no registro. Tente novamente.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(baseContext, "Falha no registro. Tente novamente.", Toast.LENGTH_SHORT).show()
                        Log.e("ERRO NA API",t.message.toString())
                    }
                })
        }
    }
}
