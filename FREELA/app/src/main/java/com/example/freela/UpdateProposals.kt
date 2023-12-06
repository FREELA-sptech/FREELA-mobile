package com.example.freela

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.freela.api.AuthService
import com.example.freela.api.ProposalsService
import com.example.freela.databinding.ActivityUpdateProposalsBinding
import com.example.freela.model.Proposals
import com.example.freela.model.Session
import com.example.freela.model.User
import com.example.freela.model.dto.request.ProposalRequest
import com.example.freela.model.dto.request.RegisterRequest
import com.example.freela.model.dto.response.LoginResponse
import com.example.freela.network.RetrofitClient
import com.example.freela.util.helpers.Helpers
import com.example.freela.view.BaseAuthenticatedActivity
import com.example.freela.view.Login
import com.example.freela.view.Proposal
import com.example.freela.view.SuccessActivity
import com.example.freela.viewModel.ProposalViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateProposals : AppCompatActivity() {
    private val binding by lazy {
        ActivityUpdateProposalsBinding.inflate(layoutInflater)
    }

    private lateinit var proposalViewModel: ProposalViewModel
    private lateinit var proposal: Proposals

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val helpers = Helpers();
        proposal = intent.getParcelableExtra<Proposals>("proposal")!!;
        val proposalsService = RetrofitClient.getInstance().create(ProposalsService::class.java)
        proposalViewModel = ProposalViewModel(proposalsService)

        binding.btnreturn.setOnClickListener {
            finish()
        }

        binding.description.setText(proposal?.description)
        binding.deadline.setText(proposal?.deadline)
        binding.value.setText(proposal?.value.toString())

        binding.update.setOnClickListener {
            val inputFields = listOf<TextInputEditText>(
                binding.description,
                binding.deadline,
                binding.value,
            )

            if (helpers.isInputValid(inputFields)) {
                performRegistration()
            }
        }

    }

    private fun performRegistration() {
        val description = binding.description.text.toString()
        val deadline = binding.deadline.text.toString()
        val value = binding.value.text.toString()
        val floatValue = value.toFloatOrNull() ?: 0.0f

        val proposalRequest = ProposalRequest(
            floatValue,description,deadline
        )

        val response = proposalViewModel.editProposal(proposal.id,proposalRequest)

        response.observe(this){success ->
            if (success) {
                showDialog()
            } else {
                Snackbar.make(binding.root, "Erro ao atualizar proposta", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sucesso!")
        builder.setMessage("Atualização feita com sucesso")

        builder.setPositiveButton("OK") { dialog, _ ->
            loadProposals()
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun loadProposals(){
        Session.proposalsListLiveData.observe(this, Observer<List<Proposals>?> { proposal ->
            proposal?.let {
                val intent = Intent(this, Proposal::class.java)
                startActivity(intent)
                finish()
            }
        })
    }
}