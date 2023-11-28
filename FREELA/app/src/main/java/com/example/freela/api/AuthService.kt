package com.example.freela.api

import com.example.freela.model.User
import com.example.freela.model.dto.request.LoginRequest
import com.example.freela.model.dto.request.RegisterRequest
import com.example.freela.model.dto.request.UserDetailsRequest
import com.example.freela.model.dto.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthService {
    @POST("/users")
    fun register(@Body registerRequest: RegisterRequest):
            Call<LoginResponse>

    @POST("/users/authenticate")
    fun login(@Body loginRequest: LoginRequest):
            Call<LoginResponse>

    @GET("/users")
    fun userDetails(@Header("Authorization") token: String): Call<User>
    @PUT("users/profile-photo")
    fun updatePhoto(@Header("Authorization") token: String, @Body image: String): Call<Void>

    @PATCH("/users")
    fun updateUserDetails(
        @Header("Authorization") token: String,
        @Body userDetails: UserDetailsRequest
    ): Call<Void>

}