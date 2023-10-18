package com.example.freela.model

data class Proposals(
    val id: Int,
    val proposalValue: Double,
    val originUser: User,
    val description: String,
    val expirationTime: String,
    val destinedOrder: Int,
    val isAccepted: Boolean,
    val isRefused: Boolean
)