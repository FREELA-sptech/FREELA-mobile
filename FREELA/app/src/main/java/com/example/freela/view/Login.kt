package com.example.freela.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.freela.api.AuthService
import com.example.freela.api.ProposalsService
import com.example.freela.databinding.ActivityIntroBinding
import com.example.freela.databinding.ActivityLoginBinding
import com.example.freela.model.User
import com.example.freela.model.Session
import com.example.freela.model.User
import com.example.freela.model.dto.request.LoginRequest
import com.example.freela.model.dto.response.LoginResponse
import com.example.freela.network.RetrofitClient
import com.example.freela.viewModel.ProposalViewModel
import com.example.freela.viewModel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class Login : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var userViewModel: UserViewModel

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
                tryLogin(inputEmail, inputPassword, it)
            }
        }

        binding.btnreturn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun tryLogin(email: String, password: String, view: View) {
        val progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
        binding.entrar.isEnabled = false
        binding.entrar.setTextColor(Color.parseColor("#274C77"))

        var loginAttempted = true // Defina como true antes de fazer a chamada do login

        val loginRequest = LoginRequest(email, password)
        val loginResultLiveData = userViewModel.loginUser(loginRequest)

        loginResultLiveData.observe(this) { loginSuccess ->
            if (loginSuccess) {


//              val token = loginResponse?.token
//                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
//                    if(task.isSuccessful){
//                        if (token != null) {
//                            updateFcm(task.result, token)
//                        }
//                        Log.i("token no login",task.result)
//                    }
//                }

                Snackbar.make(binding.root, "Login feito com sucesso", Snackbar.LENGTH_SHORT).show()
                loadHome()
            } else {
                showErrorDialog()
            }

            progressBar.visibility = View.GONE
            binding.entrar.isEnabled = true
            binding.entrar.setTextColor(Color.parseColor("#ffffff"))
        }
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
    private fun showErrorDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Erro ao fazer login")
        builder.setMessage("Usuario não encontrado")

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun loadHome(){
        Session.userLiveData.observe(this, Observer<User?> { user ->
            user?.let {
                val home = Intent(this, BaseAuthenticatedActivity::class.java)
                startActivity(home)
                finish()
            }
        })
    }

}
