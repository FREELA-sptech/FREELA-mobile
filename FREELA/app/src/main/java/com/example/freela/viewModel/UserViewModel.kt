package com.example.freela.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.freela.api.AuthService
import com.example.freela.model.Session
import com.example.freela.model.User
import com.example.freela.model.dto.request.LoginRequest
import com.example.freela.model.dto.request.RegisterRequest
import com.example.freela.model.dto.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(private val authService: AuthService) : ViewModel() {
    private val _loggedInUser = MutableLiveData<User>()
    val loggedInUser: LiveData<User> get() = _loggedInUser

    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String> get() = _loginError

    fun registerUser(registerRequest: RegisterRequest) {
        authService.register(registerRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                // Lógica para tratar a resposta do registro
                // Se bem-sucedido, pode-se fazer uma chamada para obter detalhes do usuário
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Lidar com falhas na requisição de registro
            }
        })
    }

    fun loginUser(loginRequest: LoginRequest, callback: (Boolean) -> Unit) {
        authService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {

                    response.body()?.token?.let { token ->
                        Session.token = token
                        getUserDetails(token)
                    }
                    callback(true)
                } else {
                    _loginError.value = "Erro ao fazer login"
                    callback(false)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("ERRO NA API",t.message.toString())
                callback(false)
            }
        })
    }

    private fun getUserDetails(token: String) {
        authService.userDetails("Bearer $token").enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.e("User Details",response.toString())
                if (response.isSuccessful) {
                    val user = response.body()
                    Log.i("User Details",user.toString())
                    if (user != null) {
                        _loggedInUser.value = user
                        Session.user = user
                    } else {
                        // Lógica para lidar com resposta nula
                    }
                } else {
                    // Lidar com erros ao obter detalhes do usuário após o login
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("User Details",t.message.toString())
            }
        })
    }
}