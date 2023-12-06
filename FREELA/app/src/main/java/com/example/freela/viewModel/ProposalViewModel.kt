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

    fun deleteProposal(proposalId: Int): LiveData<Boolean>{
        val userViewModel = UserViewModel(RetrofitClient.getInstance().create(AuthService::class.java))
        val success = MutableLiveData<Boolean>()

        proposalsService.deleteProposal("Bearer ${Session.token}",proposalId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    Log.i("Sucesso", response.toString())
                    userViewModel.getUserDetails(Session.token)
                    success.postValue(true)
                } else {
                    Log.i("Erro", response.toString())
                    _proposalError.value = "Erro ao excluir proposta"
                    success.postValue(false)
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                success.postValue(false)
            }
        })

        return success
    }

    fun editProposal(proposalId: Int, updatedProposal: ProposalRequest): LiveData<Boolean> {
        val userViewModel = UserViewModel(RetrofitClient.getInstance().create(AuthService::class.java))
        val success = MutableLiveData<Boolean>()

        proposalsService.editProposal("Bearer ${Session.token}", proposalId.toString(), updatedProposal)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        userViewModel.getUserDetails(Session.token)
                        getUserProposals(Session.token)
                        Log.i("Sucesso", response.toString())
                        success.postValue(true)
                    } else {
                        Log.i("Erro", response.toString())
                        _proposalError.value = "Erro ao editar proposta"
                        success.postValue(false)
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.e("EDIT PROPOSAL", t.message.toString())
                    success.postValue(false)
                }
            })

        return success
    }

    fun editProposalStatus(proposalId: String, updatedStatus: String) : LiveData<Boolean>  {
        val userViewModel = UserViewModel(RetrofitClient.getInstance().create(AuthService::class.java))
        val success = MutableLiveData<Boolean>()

        proposalsService.editProposalStatus("Bearer ${Session.token}", proposalId, updatedStatus)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        userViewModel.getUserDetails(Session.token)
                        getUserProposals(Session.token)
                        Log.i("Sucesso", response.toString())
                        success.postValue(true)
                    } else {
                        Log.i("Erro", response.toString())
                        _proposalError.value = "Erro ao editar o status da proposta"
                        success.postValue(false)
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.e("EDIT PROPOSAL STATUS", t.message.toString())
                    success.postValue(false)
                }
            })

        return success
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
            }
        })
    }
}
