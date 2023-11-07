package com.example.freela.api

import com.example.freela.model.User
import com.example.freela.model.dto.request.LoginRequest
import com.example.freela.model.dto.request.RegisterRequest
import com.example.freela.model.dto.request.UpdateUserRequest
import com.example.freela.model.dto.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthService {
    @POST("/users")
    fun register(@Body registerRequest: RegisterRequest):
            Call<LoginResponse>

    @POST("/users/authenticate")
    fun login(@Body loginRequest: LoginRequest):
            Call<LoginResponse>

    @PUT("/users/{userId}")
    fun updateUser(@Body updateUserRequest: UpdateUserRequest):
            Call<User>

    @GET("/users")
    fun userDetails(): Call<User>

}