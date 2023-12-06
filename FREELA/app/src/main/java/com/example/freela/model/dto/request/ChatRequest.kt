package com.example.freela.model.dto.request

data class ChatRequest (
    val freelancerId: Int,
    val userId: Int,
    val orderId: Int,
    val lastUpdate: String
)