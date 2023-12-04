package com.example.freela

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.R
import com.example.freela.adapters.FreelancerAdapter
import com.example.freela.adapters.OrderAdapter
import com.example.freela.api.AuthService
import com.example.freela.api.OrderService
import com.example.freela.model.Session
import com.example.freela.model.User
import com.example.freela.network.RetrofitClient
import com.example.freela.view.CreateOrder
import com.example.freela.view.OrderDetails
import com.example.freela.view.UserDetailsActivity
import com.example.freela.viewModel.OrderViewModel
import com.example.freela.viewModel.UserViewModel
import com.google.android.material.button.MaterialButton
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayInputStream

class HomeFragment : Fragment() {

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var freelancerAdapter: FreelancerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerMain)
        val orderService = RetrofitClient.getInstance().create(OrderService::class.java)
        orderViewModel = OrderViewModel(orderService)
        userViewModel = UserViewModel(RetrofitClient.getInstance().create(AuthService::class.java))

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
        }else{
            val userDetailsImageView = view.findViewById<CircleImageView>(R.id.userDetails)
            userDetailsImageView?.visibility = View.VISIBLE
            val byteArray = Base64.decode(user?.photo, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(byteArray))
            txtIcon?.visibility = View.GONE
            userDetailsImageView?.setImageBitmap(bitmap)
        }

        btnUser?.setOnClickListener {
            val intent = Intent(activity, UserDetailsActivity::class.java)
            startActivity(intent)
        }

        if (user != null) {
            if(user.isFreelancer){
                listOrders()
            }else{
                listFreelancers()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val user = Session.user
        if (user != null) {
            if(user.isFreelancer){
                listOrders()
            }else{
                listFreelancers()
            }
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
                val loader = view?.findViewById<ConstraintLayout>(R.id.loading)
                loader?.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                orders?.let {
                    orderAdapter = OrderAdapter(orders)
                    orderAdapter.onItemClick = { order ->
                        val intent = Intent(view?.context, OrderDetails::class.java)
                        intent.putExtra("orderId", order.id)
                        startActivity(intent)
                    }
                    recyclerView.adapter = orderAdapter
                    recyclerView.layoutManager = LinearLayoutManager(view?.context)
                }
            })
        } else {
            val loader = view?.findViewById<ConstraintLayout>(R.id.loading)
            loader?.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            orderAdapter = OrderAdapter(Session.orders!!)
            orderAdapter.onItemClick = { order ->
                val intent = Intent(view?.context, OrderDetails::class.java)
                intent.putExtra("orderId", order.id)
                startActivity(intent)
            }
            recyclerView.adapter = orderAdapter
            recyclerView.layoutManager = LinearLayoutManager(view?.context)
        }
    }

    private fun listFreelancers() {

        if (Session.freelancers.isNullOrEmpty()) {
            userViewModel.fetchFreelancers(Session.token)
            userViewModel.freelancers.observe(viewLifecycleOwner, Observer { freelancers ->
                val loader = view?.findViewById<ConstraintLayout>(R.id.loading)
                loader?.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                freelancers?.let {
                    freelancerAdapter = FreelancerAdapter(freelancers)
                    freelancerAdapter.onItemClick = { freelancer ->
                        Log.i("Freelancer","Clicado")
                    }
                    recyclerView.adapter = freelancerAdapter
                    recyclerView.layoutManager = LinearLayoutManager(view?.context)
                }
            })
        } else {
            val loader = view?.findViewById<ConstraintLayout>(R.id.loading)
            loader?.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            freelancerAdapter = FreelancerAdapter(Session.freelancers!!)
            freelancerAdapter.onItemClick = { freelancer ->
                Log.i("Freelancer","Clicado")
            }
            recyclerView.adapter = freelancerAdapter
            recyclerView.layoutManager = LinearLayoutManager(view?.context)
        }
    }
}
