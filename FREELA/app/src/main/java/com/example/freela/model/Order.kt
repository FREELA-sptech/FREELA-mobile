package com.example.freela.model

data class Order(
    val id: Int,
    val description: String,
    val title: String,
    val maxValue: Double,
    val user: User,
    val expirationTime: String,
    val subCategories: List<SubCategory>,
//    val photos: List<PhotosResponse>,
    val proposals: List<Proposals>,
    val isAccepted: Boolean
)