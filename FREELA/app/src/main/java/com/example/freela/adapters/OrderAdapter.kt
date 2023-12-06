package com.example.freela.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.R
import com.example.freela.model.Order
import java.io.ByteArrayInputStream
import android.util.Base64
import androidx.constraintlayout.helper.widget.Carousel
import androidx.recyclerview.widget.LinearLayoutManager

class OrderAdapter(private val orders: List<Order>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
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
        val imageRV: RecyclerView = itemView.findViewById(R.id.ImageRV)
        val subCategoryRV: RecyclerView = itemView.findViewById(R.id.recycler_view_subcategories)

        val imageAdapter = CarouselAdapter()

        fun bind(order: Order) {
            val subCategories = order.subCategories
            textTitleOrder.text = order.title
            textPrize.text = "R$${order.value.toString()}"
            textDeadline.text = order.deadline


            if (!order.photos.isNullOrEmpty()) {
               imageRV.adapter = imageAdapter
               imageAdapter.submitList(order.photos)
            }

            subCategoryRV.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            subCategoryRV.adapter = subCategories?.let { ListSubCategoryAdapter(it) }
        }

    }

}
