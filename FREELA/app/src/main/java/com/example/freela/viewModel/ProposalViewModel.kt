package com.example.freela.viewModel

import Proposals
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.freela.api.ProposalsService
import com.example.freela.model.Session
import com.example.freela.model.dto.request.ProposalRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProposalViewModel(private val proposalsService: ProposalsService) : ViewModel() {

    private val _proposals = MutableLiveData<List<Proposals>>()
    val proposals: LiveData<List<Proposals>> get() = _proposals

    private val _proposalError = MutableLiveData<String>()
    val proposalError: LiveData<String> get() = _proposalError

    fun createProposal(orderId: Int,proposals: ProposalRequest) {
        proposalsService.createProposal("Bearer ${Session.token}",orderId, proposals).enqueue(object : Callback<Proposals> {
            override fun onResponse(call: Call<Proposals>, response: Response<Proposals>) {
                if (response.isSuccessful) {
                    Log.i("Sucesso", response.toString())
                } else {
                    Log.i("Erro", response.toString())
                    _proposalError.value = "Erro ao criar proposta"
                }
            }

            override fun onFailure(call: Call<Proposals>, t: Throwable) {
                Log.e("CREATE PROPOSAL", t.message.toString())
            }
        })
    }

    fun deleteProposal(proposalId: Int) {
        proposalsService.deleteProposal("Bearer ${Session.token}",proposalId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    // Lógica para tratar a resposta da exclusão da proposta
                    // Por exemplo, atualizar a lista de propostas após a exclusão bem-sucedida
                } else {
                    _proposalError.value = "Erro ao excluir proposta"
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun editProposal(proposalId: Int) {
        // Implemente a lógica para editar uma proposta usando o método do serviço
    }

    fun editProposalStatus(proposalId: Int) {
        // Implemente a lógica para editar o status de uma proposta usando o método do serviço
    }

    fun getUserProposals() {
        // Implemente a lógica para buscar as propostas de um usuário usando o método do serviço
    }
}
