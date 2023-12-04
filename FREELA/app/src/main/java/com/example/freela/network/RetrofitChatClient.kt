package com.example.freela.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitChatClient {
    const val BASE_URL = "https://freela-chat-service.duckdns.org"

    fun getInstance(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}