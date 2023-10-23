package com.example.freela.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        setContentView(R.layout.activity_register_third)

        binding.register.setOnClickListener {
            val intent = Intent(this, activity_home::class.java)
            val registerRequest = RegisterRequest(
                "FreelancerTeste",
                "freelancer@gmail.com",
                "12345678",
                listOf(1,2,3),
                "São Paulo",
                "SP",
                true,
            )

            RetrofitClient.getInstance()
                .create(AuthService::class.java)
                .register(registerRequest)
                .enqueue(object : Callback<User> {
                    override fun onResponse(
                        call: Call<User>,
                        response: Response<User>
                    ) {
                        if (response.isSuccessful) {

                            Toast.makeText(baseContext, "Login Feito com sucesso! ", Toast.LENGTH_LONG).show()

                        }
                    }
                    override fun onFailure(call: Call<User>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
//            startActivity(intent)
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
}