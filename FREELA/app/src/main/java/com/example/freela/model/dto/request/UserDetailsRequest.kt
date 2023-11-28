package com.example.freela.model.dto.request

data class UserDetailsRequest(
    val name: String,
    val city: String,
    val uf: String,
    val description: String,
    val subCategoriesIds: List<Int>,
    val deviceId: String,
    val photo: String
)