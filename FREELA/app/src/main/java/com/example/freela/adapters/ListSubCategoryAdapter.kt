package com.example.freela.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.R
import com.example.freela.model.SubCategory

class ListSubCategoryAdapter(private val subCategories: List<SubCategory>) : RecyclerView.Adapter<ListSubCategoryAdapter.ListSubCategoryViewHolder>() {

    inner class ListSubCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val subCategoryTextView: TextView = itemView.findViewById(R.id.text_view_subcategory)

        fun bind(subCategory: SubCategory) {
            subCategoryTextView.text = subCategory.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSubCategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_subcategories_item, parent, false)
        return ListSubCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListSubCategoryViewHolder, position: Int) {
        val subCategory = subCategories[position]
        holder.bind(subCategory)
    }

    override fun getItemCount(): Int {
        return subCategories.size
    }
}