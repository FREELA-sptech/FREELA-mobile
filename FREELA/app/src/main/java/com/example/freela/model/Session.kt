package com.example.freela.model

object Session {
    var token: String = ""
    var user: User? = null // Inicializa como nulo ou com um valor padrão, se aplicável
    var subCategories: List<SubCategory> = emptyList() // Inicializa como uma lista vazia, por exemplo
}