package com.example.freela.api
import com.example.freela.model.Order
import com.example.freela.model.SubCategory
import com.example.freela.model.User
import com.example.freela.model.dto.request.OrderRequest
import retrofit2.Call
import retrofit2.http.*

interface OrderService {

    @POST("/orders")
    fun createOrder(@Header("Authorization") token: String,@Body order: OrderRequest):
            Call<Order>
    @GET("/orders")
    fun getOrders(@Header("Authorization") token: String): Call<List<Order>>

    @GET("/orders/{orderId}")
    fun getOrderDetails(
        @Header("Authorization") token: String,
        @Path("orderId") orderId: Int
    ): Call<Order>
}