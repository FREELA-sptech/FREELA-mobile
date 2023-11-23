package com.example.freela.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freela.R
import com.example.freela.adapters.SubCategoryAdapter
import com.example.freela.api.AuthService
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.example.freela.api.SubCategoryService
import com.example.freela.databinding.ActivityRegisterSecundBinding
import com.example.freela.model.Session
import com.example.freela.model.SubCategory
import com.example.freela.network.RetrofitClient
import com.example.freela.viewModel.SubCategoryViewModel
import com.example.freela.viewModel.UserViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class activity_register_secund : AppCompatActivity() {
    private lateinit var subCategoryAdapter: SubCategoryAdapter
    private val selectedSubCategories = mutableListOf<SubCategory>()
    private lateinit var subCategoryViewModel: SubCategoryViewModel
    private lateinit var subCategories: List<SubCategory>
    private val binding by lazy {
        ActivityRegisterSecundBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnNext.isEnabled = false
        binding.btnNext.setTextColor(Color.parseColor("#274C77"))
        val subCategoryService = RetrofitClient.getInstance().create(SubCategoryService::class.java)
        subCategoryViewModel = SubCategoryViewModel(subCategoryService)
        subCategoryViewModel.getSubCategories()
        subCategoryViewModel.subCategories.observe(this, { newSubCategories ->
            subCategories = newSubCategories
            createSubCategory()
        })
    }

    private fun createSubCategory(){
        binding.recyclerMain.layoutManager = LinearLayoutManager(this)
        subCategoryAdapter = SubCategoryAdapter(subCategories) { selectedSubCategory ->
            if (selectedSubCategories.contains(selectedSubCategory)) {
                selectedSubCategories.remove(selectedSubCategory)
            } else {
                selectedSubCategories.add(selectedSubCategory)
            }
            if(selectedSubCategories.isNotEmpty()){
                binding.btnNext.isEnabled = true
                binding.btnNext.setTextColor(Color.parseColor("#f7f7f7"))
            }else{
                binding.btnNext.isEnabled = false
                binding.btnNext.setTextColor(Color.parseColor("#274C77"))
            }
        }
        binding.recyclerMain.adapter = subCategoryAdapter
        binding.search.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        binding.btnNext.setOnClickListener {
            val selectedSubCategoryIds: List<Int> = selectedSubCategories.map { it.id }.distinct()
            if (selectedSubCategoryIds.isNotEmpty()) {
                val intent = Intent(this, activity_register_third::class.java)
                intent.putExtra("subCategoriesIds", selectedSubCategoryIds.toIntArray())
                startActivity(intent)
            } else {

                Snackbar.make(binding.root, "Selecione pelo menos um interesse", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.btnreturn.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<SubCategory>()

            val selectedCategoryIds = subCategoryAdapter.getSelectedCategoryIds()

            if (selectedCategoryIds.isEmpty()) {
                for (subCategory in subCategories) {
                    if (subCategory.name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                        filteredList.add(subCategory)
                    }
                }
            } else {
                for (subCategory in subCategories) {
                    if (selectedCategoryIds.contains(subCategory.category?.id)) {
                        if (subCategory.name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                            filteredList.add(subCategory)
                        }
                    }
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(this, "Nenhum Registro Encontrado", Toast.LENGTH_SHORT).show()
            } else {
                subCategoryAdapter.updateSubCategories(filteredList)
            }
        }
    }
}
