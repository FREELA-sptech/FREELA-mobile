package com.example.freela.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.freela.R
import com.example.freela.api.AuthService
import com.example.freela.api.OrderService
import com.example.freela.api.ProposalsService
import com.example.freela.databinding.ActivityCreateProposalBinding
import com.example.freela.databinding.ActivityRegisterThirdBinding
import com.example.freela.model.Order
import com.example.freela.model.dto.request.ProposalRequest
import com.example.freela.model.dto.request.RegisterRequest
import com.example.freela.model.dto.response.LoginResponse
import com.example.freela.network.RetrofitClient
import com.example.freela.util.helpers.Helpers
import com.example.freela.viewModel.OrderViewModel
import com.example.freela.viewModel.ProposalViewModel
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateProposal : AppCompatActivity() {
    private val binding by lazy {
        ActivityCreateProposalBinding.inflate(layoutInflater);
    }

    private lateinit var proposalViewModel: ProposalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val order = intent.getParcelableExtra<Order>("order")
        val helpers = Helpers();
        val proposalsService = RetrofitClient.getInstance().create(ProposalsService::class.java)
        proposalViewModel = ProposalViewModel(proposalsService)

        binding.createProposal.setOnClickListener{
            val inputFields = listOf<TextInputEditText>(
                binding.description,
                binding.deadline,
                binding.value,
            )

            if (helpers.isInputValid(inputFields)) {
                if (order != null) {
                    performRegistration(order.id)
                }
                onBackPressed()
                finish()
            }
        }

    }

    private fun performRegistration(orderId: Int) {
        val description = binding.description?.text.toString()
        val deadline = binding.deadline?.text.toString()
        val value = binding.value?.text.toString()
        val floatValue = value.toFloatOrNull() ?: 0.0f

        val proposalRequest = ProposalRequest(
            floatValue,description,deadline
        )
        proposalViewModel.createProposal(orderId,proposalRequest)


    }
}