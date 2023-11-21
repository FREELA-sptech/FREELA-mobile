package com.example.freela.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.freela.api.AuthService
import com.example.freela.databinding.ActivityLoginBinding
import com.example.freela.model.Session
import com.example.freela.model.dto.request.LoginRequest
import com.example.freela.model.dto.response.LoginResponse
import com.example.freela.network.RetrofitClient
import com.example.freela.viewModel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.PasswordAuthentication

class Login : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var userViewModel: UserViewModel // Declare o UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val authService = RetrofitClient.getInstance().create(AuthService::class.java)
        userViewModel = UserViewModel(authService)

        binding.entrar.setOnClickListener {
            val inputEmail = binding.email.text.toString()
            val inputPassword = binding.password.text.toString()

            if (!isValidEmail(inputEmail)) {
                binding.email.error = "Email inválido"
                val snackbar = Snackbar.make(it, "Email inválido!", Snackbar.LENGTH_SHORT)
                snackbar.show()

            } else if (inputPassword.length < 8) {
                binding.password.error = "Senha muito curta"
                val snackbar = Snackbar.make(it, "Senha muito curta!", Snackbar.LENGTH_SHORT)
                snackbar.show()

            } else {
                tryLogin(inputEmail,inputPassword,it)
            }
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


        val home = Intent(this, BaseAuthenticatedActivity::class.java)
        val loginRequest = LoginRequest(
            email, password
        )
        userViewModel.loginUser(loginRequest) { success ->
            Log.e("Login",success.toString())
            if (success) {
                val snackbar = Snackbar.make(view, "Login Feito com sucesso!", Snackbar.LENGTH_SHORT)
                snackbar.show()
                finish()
                startActivity(home)
            } else {
                val snackbar = Snackbar.make(view,"Email ou senha inválida!",Snackbar.LENGTH_SHORT)
                snackbar.show()
                binding.entrar.isEnabled = true
                binding.entrar.setTextColor(Color.parseColor("#f7f7f7"))
            }
        }
    }
}