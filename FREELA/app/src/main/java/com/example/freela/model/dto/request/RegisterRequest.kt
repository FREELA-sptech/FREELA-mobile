package com.example.freela.model.dto.request

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val subCategoriesIds: List<Int>,
    val city: String,
    val uf: String,
    val isFreelancer: Boolean,
    val devideId : String
)