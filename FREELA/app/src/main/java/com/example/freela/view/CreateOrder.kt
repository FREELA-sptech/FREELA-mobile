package com.example.freela.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.freela.api.OrderService
import com.example.freela.databinding.ActivityCreateOrderBinding
import com.example.freela.databinding.ActivityCreateProposalBinding
import com.example.freela.model.dto.request.OrderRequest
import com.example.freela.model.dto.request.ProposalRequest
import com.example.freela.network.RetrofitClient
import com.example.freela.util.helpers.Helpers
import com.example.freela.viewModel.OrderViewModel
import com.example.freela.viewModel.ProposalViewModel
import com.google.android.material.textfield.TextInputEditText

class CreateOrder : AppCompatActivity() {
    private val binding by lazy {
        ActivityCreateOrderBinding.inflate(layoutInflater)
    }
    private lateinit var orderViewModel: OrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val helpers = Helpers();
        val orderService = RetrofitClient.getInstance().create(OrderService::class.java)
        orderViewModel = OrderViewModel(orderService)

        binding.createOrder.setOnClickListener{
            val inputFields = listOf<TextInputEditText>(
                binding.title,
                binding.description,
                binding.deadline,
                binding.value,
            )

            if (helpers.isInputValid(inputFields)) {
                performRegistration()
                onBackPressed()
                finish()
            }
        }
    }
    private fun performRegistration() {
        val description = binding.description?.text.toString()
        val title = binding.title?.text.toString()
        val deadline = binding.deadline?.text.toString()
        val value = binding.value?.text.toString()
        val floatValue = value.toFloatOrNull() ?: 0.0f

        val orderRequest = OrderRequest(
            description,title,floatValue,listOf(1, 2, 3, 4, 5),deadline
        )
        orderViewModel.createOrder(orderRequest)


    }
}