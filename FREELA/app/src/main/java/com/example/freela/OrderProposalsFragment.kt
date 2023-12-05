package com.example.freela
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.adapters.ProposalAdapter
import com.example.freela.model.Order
import com.example.freela.model.Proposals
import com.example.freela.view.ProposalsDetails
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

private const val ARG_ORDER = "order"

class OrderProposalsFragment : Fragment() {

    private var order: Order? = null
    private lateinit var proposalsAdapter: ProposalAdapter

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
        return inflater.inflate(R.layout.fragment_order_proposals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val description = view.findViewById<TextView>(R.id.description)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerMain)

        order?.let {
            proposalsAdapter = ProposalAdapter(it.proposals as MutableList<Proposals>)
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

    companion object {
        @JvmStatic
        fun newInstance(order: Order) =
            OrderProposalsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ORDER, order)
                }
            }
    }
}
