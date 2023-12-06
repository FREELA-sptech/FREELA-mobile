package com.example.freela.api

import Proposals
import com.example.freela.model.dto.request.ProposalRequest
import retrofit2.Call
import retrofit2.http.*

interface ProposalsService {

    @POST("/proposals/{orderId}")
    fun createProposal(
        @Header("Authorization") token: String,
        @Path("orderId") orderId: Int,
        @Body proposal: ProposalRequest
    ): Call<Proposals>

    @DELETE("/proposals/{proposalId}")
    fun deleteProposal(
        @Header("Authorization") token: String,
        @Path("proposalId") proposalId: Int
    ): Call<Unit>

    @PATCH("/proposals/{proposalId}")
    fun editProposal(
        @Header("Authorization") token: String,
        @Path("proposalId") proposalId: String,
        @Body updatedProposal: ProposalRequest
    ): Call<Unit>

    @PATCH("/proposals/status/{proposalId}")
    fun editProposalStatus(
        @Header("Authorization") token: String,
        @Path("proposalId") proposalId: String,
        @Body updatedStatus: String
    ): Call<Unit>

    @GET("/proposals/user")
    fun getUserProposals(
        @Header("Authorization") token: String
    ): Call<List<Proposals>>
}