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
import com.example.freela.api.AuthService
import com.example.freela.databinding.ActivityRegisterThirdBinding
import com.example.freela.model.User
import com.example.freela.model.dto.request.LoginRequest
import com.example.freela.model.dto.request.RegisterRequest
import com.example.freela.model.dto.response.LoginResponse
import com.example.freela.network.RetrofitClient
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

        binding.btnreturn.setOnClickListener {
            onBackPressed()
        }

        binding.register.setOnClickListener {
            if (isInputValid()) {
                performRegistration()
            }
        }

        binding.btnreturn.setOnClickListener {
            val intent = Intent(this, activity_register_secund::class.java)
            startActivity(intent)
        }

        binding.redirect.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }
    private fun isInputValid(): Boolean {
        val inputFields = listOf<TextInputEditText>(
            binding.user,
            binding.name,
            binding.email,
            binding.cpf,
            binding.password
        )

        var isValid = true

        for (inputText in inputFields) {
            if (inputText != null && inputText.text.isNullOrBlank()) {
                inputText.error = "Campo obrigatório"
                isValid = false
            } else {
                inputText.error = null
            }
        }

        return isValid
    }

    private fun performRegistration() {
        val cardSelected = intent.getStringExtra("type")
        val listaDeIDs: List<Int> = listOf(1, 2, 3, 4, 5)
        val usuario = binding.user?.text.toString()
        val nome = binding.name?.text.toString()
        val email = binding.email?.text.toString()
        val cpf = binding.cpf?.text.toString()
        val senha = binding.password?.text.toString()
        val type = cardSelected == "Autônomo"

        val userRequest = RegisterRequest(
            nome,email,senha,listaDeIDs,"","",type,"001"
        )

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
                        onBackPressed()
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
