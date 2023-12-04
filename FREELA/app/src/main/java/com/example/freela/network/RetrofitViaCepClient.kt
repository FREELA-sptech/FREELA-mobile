package com.example.freela.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitViaCepClient {
    const val BASE_URL = "https://freela-chat-service.duckdns.org/swagger-ui/index.html#/chat-controller"

    fun getInstance(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}