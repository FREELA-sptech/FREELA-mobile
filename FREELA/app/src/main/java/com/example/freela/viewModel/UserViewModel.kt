package com.example.freela.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.freela.api.AuthService
import com.example.freela.api.OrderService
import com.example.freela.api.ProposalsService
import com.example.freela.api.SubCategoryService
import com.example.freela.model.Session
import com.example.freela.model.User
import com.example.freela.model.dto.request.LoginRequest
import com.example.freela.model.dto.request.RegisterRequest
import com.example.freela.model.dto.request.UserDetailsRequest
import com.example.freela.model.dto.response.LoginResponse
import com.example.freela.network.RetrofitClient
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream

class UserViewModel(private val authService: AuthService) : ViewModel() {
    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess

    private val _freelancers = MutableLiveData<List<User>>()
    val freelancers: LiveData<List<User>> get() = _freelancers

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

    fun loginUser(loginRequest: LoginRequest): LiveData<Boolean> {
        val loginSuccessLiveData = MutableLiveData<Boolean>()

        authService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    response.body()?.token?.let { token ->
                        Session.token = token
                        getUserDetails(token)
                        loginSuccessLiveData.postValue(true)
                    }
                } else {
                    loginSuccessLiveData.postValue(false)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginSuccessLiveData.postValue(false)
            }
        })

        return loginSuccessLiveData
    }

    fun getUserDetails(token: String) {
        val proposalViewModel = ProposalViewModel(RetrofitClient.getInstance().create(ProposalsService::class.java))
        val orderViewModel = OrderViewModel(RetrofitClient.getInstance().create(OrderService::class.java))
        val subCategoriesViewModel = SubCategoryViewModel(RetrofitClient.getInstance().create(SubCategoryService::class.java))

        authService.userDetails("Bearer $token").enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        if(user.isFreelancer){
                            orderViewModel.getOrders()
                        }else{
                            orderViewModel.getOrdersByCostumer()
                            fetchFreelancers(Session.token)
                        }
                        proposalViewModel.getUserProposals(Session.token)
                        subCategoriesViewModel.getSubCategories()
                        Session.updateUser(user) // Atualiza os dados na Session
                    } else {
                        // Lógica para lidar com resposta nula
                    }
                } else {
                    // Lidar com erros ao obter detalhes do usuário após o login
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("User Details", t.message.toString())
            }
        })
    }

    fun updateUserPhoto(token: String, imagePart: MultipartBody.Part?) {

        Log.i("Image",imagePart.toString())
        if (imagePart != null) {
            authService.updatePhoto("Bearer $token", imagePart).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.i("Sucesso no update da foto",response.toString())
                        getUserDetails(token)
                    } else {
                        Log.e("Erro no update da foto",response.toString())
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                }
            })
        }
    }

    fun updateUserDetails(token: String, userUpdateRequest: UserDetailsRequest) {
        authService.updateUserDetails("Bearer $token", userUpdateRequest)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.i("Sucesso na atualização", "Detalhes do usuário atualizados com sucesso")
                        getUserDetails(Session.token)
                    } else {
                        Log.e("Erro na atualização", "Erro ao atualizar os detalhes do usuário")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("Falha na requisição", "Falha na requisição para atualizar os detalhes do usuário: ${t.message}")
                }
            })
    }

    fun fetchFreelancers(token: String) {
        authService.getFreelancers("Bearer $token").enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val freelancersList = response.body()
                    freelancersList?.let {
                        _freelancers.postValue(it)
                        Session.updateFreelancersList(it)
                    }
                } else {

                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                // Lidar com falhas na requisição de freelancers
            }
        })
    }

    fun File.copyInputStreamToFile(inputStream: InputStream) {
        this.outputStream().use { fileOut ->
            inputStream.copyTo(fileOut)
        }
    }
}
