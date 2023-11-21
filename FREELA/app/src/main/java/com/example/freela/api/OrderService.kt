package com.example.freela.api
import com.example.freela.model.Order
import com.example.freela.model.SubCategory
import com.example.freela.model.User
import retrofit2.Call
import retrofit2.http.*

interface OrderService {

    @POST("/order")
    fun createOrder(order: Order):
            Call<Order>
    @GET("/orders")
    fun getOrders(@Header("Authorization") token: String): Call<List<Order>>
}