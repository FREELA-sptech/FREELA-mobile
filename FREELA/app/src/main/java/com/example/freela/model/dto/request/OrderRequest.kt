package com.example.freela.model.dto.request

data class OrderRequest (
    val description: String,
    val title: String,
    val value: Float,
    val subCategoriesIds: List<Int>,
    val deadline: String
)