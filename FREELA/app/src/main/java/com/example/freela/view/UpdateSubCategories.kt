package com.example.freela.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freela.adapters.SubCategoryAdapter
import com.example.freela.api.AuthService
import com.example.freela.databinding.ActivityUpdateSubCategoriesBinding
import com.example.freela.model.Session
import com.example.freela.model.Session.user
import com.example.freela.model.SubCategory
import com.example.freela.model.dto.request.UserDetailsRequest
import com.example.freela.network.RetrofitClient
import com.example.freela.viewModel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class UpdateSubCategories : AppCompatActivity() {
    private lateinit var subCategoryAdapter: SubCategoryAdapter
    private val selectedSubCategories = mutableListOf<SubCategory>()
    private lateinit var subCategories: List<SubCategory>
    private val binding by lazy {
        ActivityUpdateSubCategoriesBinding.inflate(layoutInflater)
    }
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val authService = RetrofitClient.getInstance().create(AuthService::class.java)
        userViewModel = UserViewModel(authService)
        subCategories = Session.subCategories
        createSubCategory()
    }

    private fun createSubCategory(){
        val user = Session.user
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
        updateSubCategories()
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
                if(user != null){
                    val userUpdate = if (user.description.isNullOrBlank()) {
                        UserDetailsRequest(
                            user.name, user.city, user.uf,"" ,selectedSubCategoryIds, "", user.photo
                        )
                    } else {
                        UserDetailsRequest(
                            user.name, user.city, user.uf,user.description ,selectedSubCategoryIds, "", user.photo
                        )
                    }
                    if (userUpdate != null) {
                        userViewModel.updateUserDetails(Session.token,userUpdate)
                        onBackPressed()
                    }
                }
            } else {
                Snackbar.make(binding.root, "Selecione pelo menos um interesse", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.btnreturn.setOnClickListener {
            val intent = Intent(this, EditUser::class.java)
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
                        if (subCategory.name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(
                                Locale.ROOT))) {
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
    private fun updateSubCategories() {
        val selectedSubCategoryIds = user?.subCategories?.map { it.id }
        subCategories.forEach { subCategory ->
            if (selectedSubCategoryIds?.contains(subCategory.id) == true) {
                subCategory.isSelected = true
            }
        }
        subCategoryAdapter.setUpdateSubCategories(subCategories)
    }
}