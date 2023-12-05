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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.R
import com.example.freela.adapters.ProposalAdapter
import com.example.freela.api.AuthService
import com.example.freela.api.ProposalsService
import com.example.freela.databinding.ActivityProposalsBinding
import com.example.freela.model.Proposals
import com.example.freela.model.Session
import com.example.freela.network.RetrofitClient
import com.example.freela.viewModel.ProposalViewModel
import com.google.android.material.snackbar.Snackbar

class Proposals : AppCompatActivity() {
    private val binding by lazy {
        ActivityProposalsBinding.inflate(layoutInflater)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var proposalsAdapter: ProposalAdapter
    private lateinit var proposalViewModel: ProposalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        proposalViewModel = ProposalViewModel(RetrofitClient.getInstance().create(ProposalsService::class.java))

        binding.btnreturn.setOnClickListener {
            val intent = Intent(this, UserDetailsActivity::class.java)
            startActivity(intent);
        }

        binding.discoverOrders.setOnClickListener{
            val intent = Intent(this, BaseAuthenticatedActivity::class.java)
            startActivity(intent);
        }

        recyclerView = binding.recyclerProposals
        proposalsAdapter = ProposalAdapter(Session.proposals as MutableList<Proposals>)
        recyclerView.adapter = proposalsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        proposalsAdapter.onItemClick = { proposal, clickType ->
            if (clickType == "itemClick") {
                showDialog(proposal)
            } else if (clickType == "actionClick") {
                showDialog(proposal)
            }
        }
        binding.textView4.text = proposalsAdapter.itemCount.toString()
    }

    private fun showDialog(proposals: Proposals) {
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
            showProposalsDetails(proposals)
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
                    proposalViewModel.deleteProposal(proposals.id)
                    Snackbar.make(binding.root, "Proposta deletada com sucesso", Snackbar.LENGTH_SHORT).show()
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

    private fun showProposalsDetails(proposals: Proposals) {
        val dialog = Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.activity_proposals_details)
        val close = dialog.findViewById<ImageView>(R.id.btnreturn)
        val titleUser = dialog.findViewById<TextView>(R.id.textView2)
        val category = dialog.findViewById<TextView>(R.id.description2)
        val description = dialog.findViewById<TextView>(R.id.description)

        close.setOnClickListener {
            dialog.dismiss()
        }

        titleUser.text = proposals.user?.name
        category.text = proposals.user?.description
        description.text = proposals.description



        dialog.show();
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

    }
}