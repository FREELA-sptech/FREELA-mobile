package com.example.freela.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.example.freela.api.AuthService
import com.example.freela.databinding.ActivityLoginBinding
import com.example.freela.model.User
import com.example.freela.model.dto.request.LoginRequest
import com.example.freela.model.dto.response.LoginResponse
import com.example.freela.network.RetrofitClient
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                val snackbar = Snackbar.make(it,"Email inválido!",Snackbar.LENGTH_SHORT)
                snackbar.show();

            }else if(inputPassword.length < 8){
                binding.password.error = "Senha muito curta"
                val snackbar = Snackbar.make(it,"Senha muito curta!",Snackbar.LENGTH_SHORT)
                snackbar.show();

            }else{
                tryLogin(inputEmail,inputPassword,it)
            }

        }

        binding.redirect.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun tryLogin(email: String, password: String, view: View) {
        val progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
        binding.entrar.isEnabled = false
        binding.entrar.setTextColor(Color.parseColor("#274C77"))


        val success = Intent(this, BaseAuthenticatedActivity::class.java)
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
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (response.isSuccessful) {
                            val loginResponse = response.body()
                            val token = loginResponse?.token
                            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                                if(task.isSuccessful){
                                    if (token != null) {
                                        updateFcm(task.result, token)
                                    }
                                    Log.i("token no login",task.result)
                                }
                            }
                            // Salvar o token nas preferências compartilhadas
                            val sharedPreferences = getSharedPreferences("AUTH", MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("TOKEN", token)
                            editor.apply()
                            finish()
                            val snackbar = Snackbar.make(view, "Login Feito com sucesso!", Snackbar.LENGTH_SHORT)
                            snackbar.show()
                            startActivity(success)
                        }else{
                            val snackbar = Snackbar.make(view,"Email ou senha inválida!",Snackbar.LENGTH_SHORT)
                            snackbar.show()
                            binding.entrar.isEnabled = true
                            binding.entrar.setTextColor(Color.parseColor("#f7f7f7"))
                        }
                        progressBar.visibility = View.GONE
                    },3000)
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        val snackbar = Snackbar.make(view,"Erro no servidor!",Snackbar.LENGTH_SHORT)
                        snackbar.show();
                        Log.e("ERRO NA API",t.message.toString())
                        binding.entrar.isEnabled = true
                        binding.entrar.setTextColor(Color.parseColor("#f7f7f7"))
                        progressBar.visibility = View.GONE

                    },3000)

                }

            })
    }

    private fun updateFcm(fcm: String, token: String){
        RetrofitClient.getInstance()
            .create(AuthService::class.java)
            .updateToken("Bearer $token",fcm)
            .enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                Log.i("token no login",response.toString())
                            }
                        }

                    }else{
                        Log.e("ERRO NA API",response.toString())
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("ERRO NA API",t.message.toString())
                }
            })


    }
}