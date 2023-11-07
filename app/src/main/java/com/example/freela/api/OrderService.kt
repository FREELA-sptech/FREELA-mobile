package com.example.freela.api
import com.example.freela.model.Order
import retrofit2.Call
import retrofit2.http.*

interface OrderService {

    @POST("/order")
    fun createOrder(order: Order):
            Call<Order>

}