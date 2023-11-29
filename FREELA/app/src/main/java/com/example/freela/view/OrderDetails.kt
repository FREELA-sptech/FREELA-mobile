package com.example.freela.view

import MyViewAdapter
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
import androidx.viewpager2.widget.ViewPager2
import com.example.freela.R
import com.example.freela.api.OrderService
import com.example.freela.api.ProposalsService
import com.example.freela.databinding.ActivityOrderDetailsBinding
import com.example.freela.model.Order
import com.example.freela.model.Session
import com.example.freela.model.dto.request.ProposalRequest
import com.example.freela.network.RetrofitClient
import com.example.freela.util.helpers.Helpers
import com.example.freela.viewModel.OrderViewModel
import com.example.freela.viewModel.ProposalViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.textfield.TextInputEditText

class OrderDetails : AppCompatActivity() {
    private val binding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }
    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager2 : ViewPager2
    private lateinit var myViewPagerAdapter: MyViewAdapter
    private lateinit var proposalViewModel: ProposalViewModel
    private lateinit var orderViewModel: OrderViewModel
    private val helpers = Helpers();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val orderService = RetrofitClient.getInstance().create(OrderService::class.java)
        val proposalsService = RetrofitClient.getInstance().create(ProposalsService::class.java)
        proposalViewModel = ProposalViewModel(proposalsService)
        orderViewModel = OrderViewModel(orderService)

        val order = intent.getParcelableExtra<Order>("order")
        val user = Session.user
        if(order != null){
            binding.title.text = order.title
            binding.prize.text = "R$${order.value.toString()}"
            binding.deadline.text = order.deadline
            if(user != null){
               if(!user.isFreelancer){
                   binding.createProposal.visibility = View.GONE
               }else if(user.id == order.user?.id){
                   binding.createProposal.visibility = View.GONE
               }else{
                   binding.createProposal.visibility = View.VISIBLE
               }
            }
            myViewPagerAdapter = MyViewAdapter(this);
            myViewPagerAdapter.setOrder(order)
            binding.viewPager.adapter = myViewPagerAdapter
            binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        binding.viewPager.setCurrentItem(tab.position)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // Implement your logic here for when a tab is unselected
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // Implement your logic here for when a tab is reselected
                }
            })
            binding.btnReturn.setOnClickListener{
                val intent = Intent(this, BaseAuthenticatedActivity::class.java)
                startActivity(intent)
                finish()
            }
            binding.createProposal.setOnClickListener{
                showDialog(order)

            }
        }
    }

    private fun showDialog(order: Order) {
        val dialog = Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.activity_create_proposal)
        val close = dialog.findViewById<ImageView>(R.id.btnreturn)
        val register = dialog.findViewById<MaterialButton>(R.id.createProposal)
        val description = dialog.findViewById<TextInputEditText>(R.id.description)
        val deadline = dialog.findViewById<TextInputEditText>(R.id.deadline)
        val value = dialog.findViewById<TextInputEditText>(R.id.value)

        register.setOnClickListener {
            val inputFields = listOf<TextInputEditText>(
                description,
                deadline,
                value,
            )

            if (helpers.isInputValid(inputFields)) {
                if (order != null) {
                    val description = description?.text.toString()
                    val deadline = deadline.text.toString()
                    val value = value.text.toString()
                    val floatValue = value.toFloatOrNull() ?: 0.0f

                    val proposalRequest = ProposalRequest(
                        floatValue,description,deadline
                    )
                    proposalViewModel.createProposal(order.id,proposalRequest)
                    orderViewModel.getOrders()
                }
                onBackPressed()
                dialog.dismiss()
            }

        }

        close.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show();
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }


}