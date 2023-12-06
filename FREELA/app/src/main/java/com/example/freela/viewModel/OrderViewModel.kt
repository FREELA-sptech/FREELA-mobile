package com.example.freela.viewModel

import android.content.pm.PackageInstaller.SessionInfo
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.freela.api.OrderService
import com.example.freela.api.SubCategoryService
import com.example.freela.model.Order
import com.example.freela.model.Session
import com.example.freela.model.SubCategory
import com.example.freela.model.dto.request.OrderRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderViewModel(private val orderService: OrderService) : ViewModel(){
    private val _orders = MutableLiveData<List<Order>>() // MutableLiveData para armazenar os pedidos
    val orders: LiveData<List<Order>> // LiveData exposto para observação externa
        get() = _orders

    fun getOrders() {
        orderService.getOrders("Bearer ${Session.token}").enqueue(object : Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                if (response.isSuccessful) {
                    val orders = response.body()
                    orders?.let {
                        _orders.value = it
                    }
                } else {
                    Log.i("Orders", response.toString())
                    Log.i("Token", Session.token.toString())
                }
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {

            }
        })
    }
    fun createOrder(order: OrderRequest) {
        orderService.createOrder("Bearer ${Session.token}",order).enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.isSuccessful) {
                    Log.i("Orders",response.toString())
                } else {
                    Log.i("Orders", response.toString())
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {

            }
        })
    }

}