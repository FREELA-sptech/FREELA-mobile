package com.example.freela.model

data class User(
    val id: Int,
    val name: String,
//    val profilePhoto: ByteArray,
    val rate: Double,
    val uf: String,
    val city: String,
    val deviceId: String,
    val subCategories: List<SubCategory>
)