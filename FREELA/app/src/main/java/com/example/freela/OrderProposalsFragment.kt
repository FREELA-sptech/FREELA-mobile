package com.example.freela
import Proposals
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.adapters.ProposalAdapter
import com.example.freela.model.Order
import com.example.freela.view.ProposalsDetails

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
            proposalsAdapter.onItemClick = {
                val intent = Intent(view.context, ProposalsDetails::class.java)
                intent.putExtra("proposals", it)
                startActivity(intent)
            }
        }
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
