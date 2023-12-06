package com.example.freela.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.R
import com.example.freela.model.Order

class OrderAdapter(private val orders: MutableList<Order>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    var onItemClick : ((Order) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item,parent,false)
        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(orders[position])
        }
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val textTitleOrder: TextView = itemView.findViewById(R.id.titleOrder)
        private val textPrize: TextView = itemView.findViewById(R.id.prize)
        private val textDeadline: TextView = itemView.findViewById(R.id.deadline)

        fun bind(order: Order) {
            textTitleOrder.text = order.title
            textPrize.text = "R$${order.value.toString()}"
            textDeadline.text = order.deadline

        }

    }

}
