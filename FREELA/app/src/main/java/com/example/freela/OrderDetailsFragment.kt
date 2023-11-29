package com.example.freela
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.R
import com.example.freela.model.Order

private const val ARG_ORDER = "order"

class OrderDetailsFragment : Fragment() {

    private var order: Order? = null

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
        val description = view.findViewById<TextView>(R.id.description)

        order?.let {
            description.text = it.description

        }
    }

    companion object {
        @JvmStatic
        fun newInstance(order: Order) =
            OrderDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ORDER, order)
                }
            }
    }
}
