package com.example.freela
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Base64
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.adapters.ProposalAdapter
import com.example.freela.api.ProposalsService
import com.example.freela.model.Order
import com.example.freela.model.Proposals
import com.example.freela.model.Session
import com.example.freela.network.RetrofitClient
import com.example.freela.view.BaseAuthenticatedActivity
import com.example.freela.viewModel.ProposalViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayInputStream

private const val ARG_ORDER = "order"

class OrderRefuseFragment : Fragment() {

    private var order: Order? = null
    private lateinit var proposalsAdapter: ProposalAdapter
    private lateinit var proposalViewModel: ProposalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            order = it.getParcelable(ARG_ORDER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerMain)
        proposalViewModel = ProposalViewModel(RetrofitClient.getInstance().create(ProposalsService::class.java))

        order?.let {
            val listaFiltrada = it.proposals?.filter { it.status == "OPEN" }
            proposalsAdapter = ProposalAdapter(listaFiltrada as MutableList<Proposals>)
            recyclerView.adapter = proposalsAdapter
            recyclerView.layoutManager = LinearLayoutManager(view.context)
            proposalsAdapter.onItemClick = { proposals, clickType ->
                if (clickType == "itemClick") {
                    showDialog(view, proposals)
                }
            }
        }
    }

    private fun showDialog(view: View, proposals: Proposals) {
        val dialog = Dialog(view.context);
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

        val proposalsId = proposals.id.toString()

        accept.setOnClickListener{
            val builder = AlertDialog.Builder(view.context)
            builder.setMessage("Tem certeza que deseja aceitar a proposta?")
                .setCancelable(false)
                .setPositiveButton("Sim") { _, _ ->
                    val response = proposalViewModel.editProposalStatus(proposalsId,"ACCEPTED")
                    response.observe(viewLifecycleOwner){ success ->
                        if(success){
                            Snackbar.make(view, "Proposta aceita com sucesso", Snackbar.LENGTH_SHORT).show()
                            loadHome()
                        }else{
                            Snackbar.make(view, "Algo deu errado", Snackbar.LENGTH_SHORT).show()
                        }
                    }

                }
                .setNegativeButton("Não") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
            dialog.dismiss()
        }

        recuse.setOnClickListener{
            val builder = AlertDialog.Builder(view.context)
            builder.setMessage("Tem certeza de que deseja rejeitar a proposta?")
                .setCancelable(false)
                .setPositiveButton("Sim") { _, _ ->
                    val response = proposalViewModel.editProposalStatus(proposalsId,"REFUSED")
                    response.observe(viewLifecycleOwner){ success ->
                        if(success){
                            Snackbar.make(view, "Proposta recusada com sucesso", Snackbar.LENGTH_SHORT).show()
                            loadHome()
                        }else{
                            Snackbar.make(view, "Algo deu errado", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("Não") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
            dialog.dismiss()
        }

        if(proposals.user?.id == Session.user?.id){
            btns.visibility = View.GONE
        }



        dialog.show();
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

    }

    private fun loadHome(){
        Session.proposalsListLiveData.observe(viewLifecycleOwner, Observer<List<Proposals>?> { proposals ->
            proposals?.let {
                val home = Intent(view?.context, BaseAuthenticatedActivity::class.java)
                startActivity(home)
            }
        })
    }

}
