package com.example.freela

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.adapters.OrderAdapter
import com.example.freela.api.OrderService
import com.example.freela.model.Order
import com.example.freela.model.Session
import com.example.freela.network.RetrofitClient
import com.example.freela.view.CreateOrder
import com.example.freela.view.OrderDetails
import com.example.freela.view.UserDetailsActivity
import com.example.freela.viewModel.OrderViewModel
import com.google.android.material.button.MaterialButton
import de.hdodenhof.circleimageview.CircleImageView

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var orderAdapter: OrderAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = Session.user
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerMain)
        val orderService = RetrofitClient.getInstance().create(OrderService::class.java)
        orderViewModel = OrderViewModel(orderService)
        orderViewModel.getOrders()

        user?.let {
            orderViewModel.orders.observe(viewLifecycleOwner, Observer { orders ->
                orders?.let {
                    Log.i("Orders", "Number of orders")
                    orderAdapter = OrderAdapter(it as MutableList<Order>)
                    orderAdapter.onItemClick = {
                        val intent = Intent(view.context,OrderDetails::class.java)
                        intent.putExtra("order", it)
                        startActivity(intent)
                    }
                    recyclerView.adapter = orderAdapter
                    recyclerView.layoutManager = LinearLayoutManager(view.context)
                }
            })

            val btnUser = view.findViewById<CircleImageView>(R.id.userDetails)
            val createOrder = view.findViewById<MaterialButton>(R.id.createOrder)
            val textView = view.findViewById<TextView>(R.id.hello)
            val txtIcon = view.findViewById<TextView>(R.id.userDetailsWithoutPhoto)
            textView.text = "Olá, ${user.name}"
            if (user.profilePhoto == null) {
                val hash = user.hashCode()
                txtIcon.text = user.name.first().toString()
                txtIcon.background = view.oval(Color.parseColor("#274C77"))
                txtIcon.setOnClickListener {
                    val intent = Intent(activity, UserDetailsActivity::class.java)
                    startActivity(intent)
                }
            }

            if(user.isFreelancer){
                createOrder.visibility = View.GONE
            }

            btnUser.setOnClickListener {
                val intent = Intent(activity, UserDetailsActivity::class.java)
                startActivity(intent)
            }

            createOrder.setOnClickListener {
                val intent = Intent(activity, CreateOrder::class.java)
                startActivity(intent)
            }
        }
    }


    fun View.oval(@ColorInt color: Int): ShapeDrawable? {
        val oval = ShapeDrawable(OvalShape())
        with(oval){
            intrinsicHeight = height
            intrinsicWidth = width
            paint.color = color
        }
        return oval
    }


}
