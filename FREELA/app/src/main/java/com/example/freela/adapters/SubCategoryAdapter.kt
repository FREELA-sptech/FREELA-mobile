package com.example.freela.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.R
import com.example.freela.model.SubCategory

class SubCategoryAdapter(
    private val subCategories: List<SubCategory>,
    private val onSubCategorySelected: (SubCategory) -> Unit
) : RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder>() {

    private val items = mutableListOf<SubCategory>()

    init {
        items.addAll(subCategories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_category_item, parent, false)
        return SubCategoryViewHolder(view)
    }
    fun getSelectedCategoryIds(): List<Int> {
        return items.filter { it.isSelected }
            .mapNotNull { it.category?.id }
            .distinct()
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        val subCategory = items[position]
        holder.bind(subCategory)
    }

    fun updateSubCategories(subCategories: List<SubCategory>) {
        items.clear()
        items.addAll(subCategories)
        notifyDataSetChanged()
    }
    fun setUpdateSubCategories(subCategories: List<SubCategory>) {
        val selectedCategoryIds = subCategories
            .filter { it.isSelected }
            .map { it.category?.id }
            .distinct()

        if (selectedCategoryIds.size > 1 || selectedCategoryIds.isEmpty()) {
            items.clear()
            items.addAll(subCategories)
        } else {
            val selectedCategoryId = selectedCategoryIds.firstOrNull()
            selectedCategoryId?.let {
                filterSubCategoriesByCategory(it)
            }
        }
        notifyDataSetChanged()
    }

    inner class SubCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxSubCategory)
        private val textSubCategoryName: TextView = itemView.findViewById(R.id.textSubCategoryName)
        private val textCategoryName: TextView = itemView.findViewById(R.id.textCategoryName)

        fun bind(subCategory: SubCategory) {
            with(subCategory) {
                textSubCategoryName.text = name
                textCategoryName.text = category?.name
                checkBox.setOnCheckedChangeListener(null)
                checkBox.isChecked = isSelected

                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    isSelected = isChecked
                    onSubCategorySelected(subCategory)

                    val selectedCategoryIds = subCategories
                        .filter { it.isSelected }
                        .map { it.category?.id }
                        .distinct()

                    if (selectedCategoryIds.size > 1 || selectedCategoryIds.isEmpty()) {
                        items.clear()
                        items.addAll(subCategories)
                    } else {
                        val selectedCategoryId = selectedCategoryIds.firstOrNull()
                        selectedCategoryId?.let {
                            filterSubCategoriesByCategory(it)
                        }
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun filterSubCategoriesByCategory(categoryId: Int) {
        val itemsToDisplay = subCategories.filter { it.category?.id == categoryId }
        items.clear()
        items.addAll(itemsToDisplay)
        notifyDataSetChanged()
    }
}
