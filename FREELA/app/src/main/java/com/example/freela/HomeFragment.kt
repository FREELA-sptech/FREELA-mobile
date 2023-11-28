package com.example.freela

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.R
import com.example.freela.adapters.OrderAdapter
import com.example.freela.api.OrderService
import com.example.freela.model.Session
import com.example.freela.model.User
import com.example.freela.network.RetrofitClient
import com.example.freela.view.CreateOrder
import com.example.freela.view.OrderDetails
import com.example.freela.view.UserDetailsActivity
import com.example.freela.viewModel.OrderViewModel
import com.google.android.material.button.MaterialButton
import de.hdodenhof.circleimageview.CircleImageView

class HomeFragment : Fragment() {

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerMain)
        val orderService = RetrofitClient.getInstance().create(OrderService::class.java)
        orderViewModel = OrderViewModel(orderService)

        val btnUser = view.findViewById<CircleImageView>(R.id.userDetails)
        val createOrder = view.findViewById<MaterialButton>(R.id.createOrder)
        val textView = view.findViewById<TextView>(R.id.hello)
        val txtIcon = view.findViewById<TextView>(R.id.userDetailsWithoutPhoto)

        val user = Session.user
        textView?.text = "Olá, ${user?.name}"

        if (user?.photo == "") {
            val hash = user.hashCode()
            txtIcon?.text = user.name.first().toString()
            txtIcon?.background = view.oval(Color.parseColor("#274C77"))
            txtIcon?.setOnClickListener {
                val intent = Intent(activity, UserDetailsActivity::class.java)
                startActivity(intent)
            }
        }

        if (user?.isFreelancer == true) {
            createOrder?.visibility = View.GONE
        }

        btnUser?.setOnClickListener {
            val intent = Intent(activity, UserDetailsActivity::class.java)
            startActivity(intent)
        }

        createOrder?.setOnClickListener {
            val intent = Intent(activity, CreateOrder::class.java)
            startActivity(intent)
        }

        if (user != null) {
            listOrders()
        }
    }

    override fun onResume() {
        super.onResume()
        val user = Session.user
        if (user != null) {
            listOrders()
        }
    }

    private fun View.oval(@ColorInt color: Int): ShapeDrawable? {
        val oval = ShapeDrawable(OvalShape())
        with(oval){
            intrinsicHeight = height
            intrinsicWidth = width
            paint.color = color
        }
        return oval
    }

    private fun listOrders() {
        if (Session.orders.isNullOrEmpty()) {
            orderViewModel.getOrders()
            orderViewModel.orders.observe(viewLifecycleOwner, Observer { orders ->
                orders?.let {
                    orderAdapter = OrderAdapter(orders)
                    orderAdapter.onItemClick = { order ->
                        val intent = Intent(view?.context, OrderDetails::class.java)
                        intent.putExtra("order", order)
                        startActivity(intent)
                    }
                    recyclerView.adapter = orderAdapter
                    recyclerView.layoutManager = LinearLayoutManager(view?.context)
                }
            })
        } else {
            orderAdapter = OrderAdapter(Session.orders!!)
            orderAdapter.onItemClick = { order ->
                val intent = Intent(view?.context, OrderDetails::class.java)
                intent.putExtra("order", order)
                startActivity(intent)
            }
            recyclerView.adapter = orderAdapter
            recyclerView.layoutManager = LinearLayoutManager(view?.context)
        }
    }
}
