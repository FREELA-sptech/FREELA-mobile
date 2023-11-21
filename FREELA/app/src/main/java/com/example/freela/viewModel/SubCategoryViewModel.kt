package com.example.freela.viewModel

import android.content.pm.PackageInstaller.SessionInfo
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.freela.api.SubCategoryService
import com.example.freela.model.Session
import com.example.freela.model.SubCategory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubCategoryViewModel(private val subCategoryService: SubCategoryService) : ViewModel(){
    private val _subCategories = MutableLiveData<List<SubCategory>>()
    val subCategories: LiveData<List<SubCategory>> get() = _subCategories

    fun getSubCategories() {
        subCategoryService.getSubCategories().enqueue(object :
            Callback<List<SubCategory>> {
            override fun onResponse(call: Call<List<SubCategory>>, response: Response<List<SubCategory>>) {
                if (response.isSuccessful) {
                    val subCategories = response.body()
                    if (subCategories != null) {
                        _subCategories.value = subCategories
                        Session.subCategories = subCategories
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<List<SubCategory>>, t: Throwable) {
                // Lidar com falhas na requisição
            }
        })
    }

}