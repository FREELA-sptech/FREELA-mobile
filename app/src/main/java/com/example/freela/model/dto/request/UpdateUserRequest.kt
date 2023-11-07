package com.example.freela.model.dto.request

data class UpdateUserRequest (
    val name: String,
    val city: String,
    val uf: String,
    val description: String,
    val deviceId: String,
    val subCategoriesIds: List<Int>,
)
