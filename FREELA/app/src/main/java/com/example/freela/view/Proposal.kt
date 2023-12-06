package com.example.freela.view

import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.R
import com.example.freela.UpdateProposals
import com.example.freela.adapters.ProposalAdapter
import com.example.freela.adapters.oval
import com.example.freela.api.ProposalsService
import com.example.freela.databinding.ActivityProposalsBinding
import com.example.freela.model.Proposals
import com.example.freela.model.Session
import com.example.freela.network.RetrofitClient
import com.example.freela.viewModel.ProposalViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayInputStream

class Proposal : AppCompatActivity() {
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
            finish()
            dialog.dismiss()
            val intent = Intent(this, UpdateProposals::class.java)
            intent.putExtra("proposal", proposals)
            startActivity(intent);
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
        val titleUserOrder = dialog.findViewById<TextView>(R.id.userOrder)
        val description = dialog.findViewById<TextView>(R.id.description)
        val prize = dialog.findViewById<TextView>(R.id.prize)
        val prizeOrder = dialog.findViewById<TextView>(R.id.prizeOrder)
        val deadline = dialog.findViewById<TextView>(R.id.deadlineValue)
        val deadlineOrder = dialog.findViewById<TextView>(R.id.deadlineValueOrder)
        val titleOrder = dialog.findViewById<TextView>(R.id.titleOrder)
        val descriptionOrder = dialog.findViewById<TextView>(R.id.descriptionOrder)
        val txtIcon: TextView = dialog.findViewById(R.id.imgWithoutImage)
        val txtIconOrder: TextView = dialog.findViewById(R.id.userOrderDetailsWithoutPhoto)
        val containerTxt: ConstraintLayout = dialog.findViewById(R.id.changeImage)
        val btns: LinearLayout = dialog.findViewById(R.id.btns)
        val accept: MaterialButton = dialog.findViewById(R.id.acceptProposal)
        val recuse: MaterialButton = dialog.findViewById(R.id.recuseProposal)

        close.setOnClickListener {
            dialog.dismiss()
        }

        titleOrder.text = proposals.order?.user?.name
        titleUser.text = proposals.user?.name
        description.text = proposals.description
        descriptionOrder.text = proposals.order?.description
        titleOrder.text = proposals.order?.title
        deadline.text = proposals.deadline
        deadlineOrder.text = proposals.order?.deadline
        prizeOrder.text = "R$ ${proposals.order?.value.toString()}"
        prize.text = "R$ ${proposals.value.toString()}"

        if (proposals.user?.photo == "") {
            txtIcon.text = proposals.user.name.first().toString()
            txtIcon.setBackgroundColor(Color.parseColor("#274C77"))
        }else{
            containerTxt.visibility = View.GONE
            proposals.user?.photo?.let { photoString ->
                val userDetailsImageView = dialog.findViewById<CircleImageView>(R.id.userDetails)
                userDetailsImageView?.visibility = View.VISIBLE
                val byteArray = Base64.decode(photoString, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(byteArray))
                txtIcon.visibility = View.GONE
                userDetailsImageView?.setImageBitmap(bitmap)
            }
        }

        if (proposals.order?.user?.photo == "") {
            txtIconOrder.text = proposals.order?.user?.name?.first().toString()
            txtIconOrder.setBackgroundColor(Color.parseColor("#274C77"))
        }else{
            proposals.order?.user?.photo?.let { photoString ->
                val userDetailsImageView = dialog.findViewById<CircleImageView>(R.id.userOrderDetails)
                userDetailsImageView?.visibility = View.VISIBLE
                val byteArray = Base64.decode(photoString, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(byteArray))
                txtIconOrder.visibility = View.GONE
                userDetailsImageView?.setImageBitmap(bitmap)
            }
        }

        if(proposals.user?.id == Session.user?.id){
            btns.visibility = View.GONE
        }

        accept.setOnClickListener{

            dialog.dismiss()
        }

        recuse.setOnClickListener{

            dialog.dismiss()
        }

        dialog.show();
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

    }
}