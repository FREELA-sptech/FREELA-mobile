package com.example.freela.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.freela.api.AuthService
import com.example.freela.databinding.ActivityLoginBinding
import com.example.freela.model.User
import com.example.freela.model.dto.request.LoginRequest
import com.example.freela.model.dto.request.UpdateUserRequest
import com.example.freela.model.dto.response.LoginResponse
import com.example.freela.network.RetrofitClient
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.PasswordAuthentication

class Login : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.entrar.setOnClickListener {
            val inputEmail = binding.email.text.toString();
            val inputPassword = binding.password.text.toString();

            if (!isValidEmail(inputEmail)){
                binding.email.error = "Email inválido"
            }else if(inputPassword.length < 8){
                binding.password.error = "Senha muito curta"
            }else{
                tryLogin(inputEmail,inputPassword)
            }

        }

        binding.redirect.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        binding.btnreturn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun tryLogin(email: String, password: String) {
        val success = Intent(this, Register::class.java)

        val loginRequest = LoginRequest(
            email, password
        )
        RetrofitClient.getInstance()
            .create(AuthService::class.java)
            .login(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        //sharedPreferences
                        val prefes=
                            getSharedPreferences("AUTH", MODE_PRIVATE)
                        val editor= prefes.edit()
                        editor.putString("TOKEN", response.body().toString())
                        editor.commit()
                        binding.errorMessage.visibility = View.GONE

                        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                            if(task.isSuccessful){

//                                RetrofitClient.getInstance()
//                                    .create(AuthService::class.java)
//                                    .updateUser(UpdateUserRequest())
//                                    .enqueue(object : Callback<User> {
//                                        fun onResponse(
//                                            call: Call<User>,
//                                            response: Response<User>
//                                        ) {
//                                            if (response.isSuccessful) {
//
//                                            }else{
//
//                                            }
//                                        }
//
//                                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                                            binding.errorMessage.visibility = View.VISIBLE
//                                            binding.errorMessage.text = "Email ou senha incorretos!"
//                                        }
//
//                                    })

                                Log.i("my Token",task.getResult())
                            }}

                        Toast.makeText(baseContext, "Login Feito com sucesso! ", Toast.LENGTH_LONG).show()
                        startActivity(success)
                    }else{
                        binding.errorMessage.visibility = View.VISIBLE
                        binding.errorMessage.text = "Email ou senha incorretos!"
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    binding.errorMessage.visibility = View.VISIBLE
                    binding.errorMessage.text = "Email ou senha incorretos!"
                }

            })
    }
}