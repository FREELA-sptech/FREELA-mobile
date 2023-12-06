package com.example.freela.api

import com.example.freela.model.Chat
import com.example.freela.model.User
import com.example.freela.model.dto.request.LoginRequest
import com.example.freela.model.dto.request.RegisterRequest
import com.example.freela.model.dto.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatService {

    @GET("/chats")
    fun getChats(
        @Header("Authorization") token: String,
        @Query("userId") userId: Int?,
        @Query("isFreelancer") isFreelancer: Boolean?,
    ): Call<List<Chat>>

    @POST("/chats")
    fun sendMessage(@Body loginRequest: LoginRequest):
            Call<LoginResponse>

    @GET("/chats/messages/")
    fun getMessagesByChat(@Header("Authorization") token: String): Call<User>
}