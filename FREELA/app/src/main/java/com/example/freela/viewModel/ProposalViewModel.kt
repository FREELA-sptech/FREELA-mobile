package com.example.freela.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.freela.api.AuthService
import com.example.freela.api.ProposalsService
import com.example.freela.model.Proposals
import com.example.freela.model.Session
import com.example.freela.model.dto.request.ProposalRequest
import com.example.freela.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProposalViewModel(private val proposalsService: ProposalsService) : ViewModel() {

    private val _proposals = MutableLiveData<List<Proposals>>()
    val proposals: LiveData<List<Proposals>> get() = _proposals

    private val _proposalError = MutableLiveData<String>()
    val proposalError: LiveData<String> get() = _proposalError

    fun createProposal(orderId: Int,proposals: ProposalRequest) {
        val userViewModel = UserViewModel(RetrofitClient.getInstance().create(AuthService::class.java))

        proposalsService.createProposal("Bearer ${Session.token}",orderId, proposals).enqueue(object : Callback<Proposals> {
            override fun onResponse(call: Call<Proposals>, response: Response<Proposals>) {
                if (response.isSuccessful) {
                    userViewModel.getUserDetails(Session.token)
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
        val userViewModel = UserViewModel(RetrofitClient.getInstance().create(AuthService::class.java))

        proposalsService.deleteProposal("Bearer ${Session.token}",proposalId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    Log.i("Sucesso", response.toString())
                    userViewModel.getUserDetails(Session.token)
                } else {
                    Log.i("Erro", response.toString())
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

    fun getUserProposals(token: String) {
        Log.d("YourApiService", "getUserProposals: Requesting user proposals")

        proposalsService.getUserProposals("Bearer $token").enqueue(object : Callback<List<Proposals>> {
            override fun onResponse(call: Call<List<Proposals>>, response: Response<List<Proposals>>) {
                if (response.isSuccessful) {
                    val proposalsList = response.body()
                    proposalsList?.let {
                        Log.d("YourApiService", "getUserProposals: Successful response received")
                        // Atualiza a lista de Proposals na Session após obter a resposta da API
                        Session.updateProposalsList(it)
                    }
                } else {
                    Log.e("YourApiService", "getUserProposals: Unsuccessful response ${response.code()}")
                    // Lidar com resposta de erro da API
                }
            }

            override fun onFailure(call: Call<List<Proposals>>, t: Throwable) {
                Log.e("YourApiService", "getUserProposals: Request failed", t)
                // Lidar com falha na requisição à API
            }
        })
    }
}
