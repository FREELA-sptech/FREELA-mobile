package com.example.freela.model.dto.request

data class RegisterRequest(
    val nome: String,
    val email: String,
    val senha: String,
    val subCategoryId: List<Int>,
    val city: String,
    val uf: String,
    val isFreelancer: Boolean
)