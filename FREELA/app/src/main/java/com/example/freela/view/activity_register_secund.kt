package com.example.freela.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freela.R
import com.example.freela.adapters.SubCategoryAdapter
import com.example.freela.api.AuthService
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.example.freela.api.SubCategoryService
import com.example.freela.databinding.ActivityLoginBinding
import com.example.freela.databinding.ActivityRegisterSecundBinding
import com.example.freela.databinding.ActivityRegisterThirdBinding
import com.example.freela.model.Session
import com.example.freela.model.SubCategory
import com.example.freela.network.RetrofitClient
import com.example.freela.viewModel.SubCategoryViewModel
import com.example.freela.viewModel.UserViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar

class activity_register_secund : AppCompatActivity() {
    private lateinit var subCategoryAdapter: SubCategoryAdapter
    private val selectedSubCategories = mutableListOf<SubCategory>()
    private lateinit var subCategoryViewModel: SubCategoryViewModel
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
        Log.i("Lista", subCategoryViewModel.subCategories.value.toString())
        createSubCategory()
    }
    private fun createSubCategory(){
        binding.recyclerMain.layoutManager = LinearLayoutManager(this)
        subCategoryAdapter = SubCategoryAdapter(Session.subCategories) { selectedSubCategory ->
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
    }
}
