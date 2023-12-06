package com.example.freela.api
import com.example.freela.model.Order
import com.example.freela.model.SubCategory
import retrofit2.Call
import retrofit2.http.*

interface SubCategoryService {
    @GET("/sub-categories")
    fun getSubCategories(): Call<List<SubCategory>>

}
