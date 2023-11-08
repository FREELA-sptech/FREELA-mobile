package com.example.freela.model

data class User(
    val id: Int,
    val name: String,
    val profilePhoto: ByteArray,
    val rate: Double,
    val uf: String,
    val city: String,
    val isFreelancer: Boolean,
    val description: String?,
    val subCategories: List<SubCategory>
)