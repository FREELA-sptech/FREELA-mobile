package com.example.freela.view

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.R
import com.example.freela.adapters.OrderAdapter
import com.example.freela.adapters.ProposalAdapter
import com.example.freela.api.OrderService
import com.example.freela.api.ProposalsService
import com.example.freela.databinding.ActivityOrdersBinding
import com.example.freela.databinding.ActivityProposalsBinding
import com.example.freela.model.Order
import com.example.freela.model.Proposals
import com.example.freela.model.Session
import com.example.freela.network.RetrofitClient
import com.example.freela.viewModel.OrderViewModel
import com.example.freela.viewModel.ProposalViewModel
import com.google.android.material.snackbar.Snackbar

class Orders : AppCompatActivity() {
    private val binding by lazy {
        ActivityOrdersBinding.inflate(layoutInflater)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var ordersAdapter: OrderAdapter
    private lateinit var orderViewModel: OrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        orderViewModel = OrderViewModel(RetrofitClient.getInstance().create(OrderService::class.java))

        binding.btnreturn.setOnClickListener {
            val intent = Intent(this, UserDetailsActivity::class.java)
            startActivity(intent);
        }

        binding.discoverFreelancers.setOnClickListener{
            val intent = Intent(this, BaseAuthenticatedActivity::class.java)
            startActivity(intent);
        }

        recyclerView = binding.recyclerOrders
        if(Session.orders?.isEmpty() == true){
            recyclerView.visibility = View.GONE
            binding.noContent.visibility = View.VISIBLE
        }else{
            ordersAdapter = OrderAdapter(Session.orders!!)
        }

        recyclerView.adapter = ordersAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        ordersAdapter.onItemClick = { order ->
            showDialog(order)
        }
        binding.textView4.text = ordersAdapter.itemCount.toString()
    }

    private fun showDialog(order: Order) {
        val dialog = Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.actions_proposals)
        val close = dialog.findViewById<ImageView>(R.id.close)
        val edit = dialog.findViewById<RelativeLayout>(R.id.editar)
        val delete = dialog.findViewById<RelativeLayout>(R.id.delete)
        val details = dialog.findViewById<RelativeLayout>(R.id.details)

        close.setOnClickListener {
            dialog.dismiss()
        }

        details.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, Order::class.java)
            startActivity(intent);
        }

        edit.setOnClickListener {
            dialog.dismiss()
            Snackbar.make(binding.root, "Editar", Snackbar.LENGTH_SHORT).show()
        }

        delete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Tem certeza de que deseja excluir a proposta?")
                .setCancelable(false)
                .setPositiveButton("Sim") { _, _ ->
//                    orderViewModel.deleteProposal(proposals.id)
                    Snackbar.make(binding.root, "Pedido deletado com sucesso", Snackbar.LENGTH_SHORT).show()
                }
                .setNegativeButton("Não") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        dialog.show();
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

    }
}