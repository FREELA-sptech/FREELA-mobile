package com.example.freela.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.freela.model.Order
import com.example.freela.model.SubCategory
import com.example.freela.model.User
import com.example.freela.model.Proposals
import android.os.Parcel
import android.os.Parcelable
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream

object Session {
    var token: String = ""
        set(value) {
            field = value
            _tokenLiveData.value = value
        }
        get() {
            return field
        }

    private val _userLiveData = MutableLiveData<User?>()
    val userLiveData: LiveData<User?> = _userLiveData

    private val _tokenLiveData = MutableLiveData<String>()
    val tokenLiveData: LiveData<String> = _tokenLiveData

    private val _orderListLiveData = MutableLiveData<List<Order>>()
    val orderListLiveData: LiveData<List<Order>> = _orderListLiveData

    private val _proposalsListLiveData = MutableLiveData<List<Proposals>>()
    val proposalsListLiveData: LiveData<List<Proposals>> = _proposalsListLiveData

    private val _freelancersListLiveData = MutableLiveData<List<User>>()
    val freelancersListLiveData: LiveData<List<User>> = _freelancersListLiveData

    var user: User?
        get() = _userLiveData.value
        set(value) {
            _userLiveData.value = value
        }

    var orders: List<Order>?
        get() = _orderListLiveData.value
        set(value) {
            _orderListLiveData.value = value
        }

    var proposals: List<Proposals>?
        get() = _proposalsListLiveData.value
        set(value) {
            _proposalsListLiveData.value = value
        }

    var freelancers: List<User>?
        get() = _freelancersListLiveData.value
        set(value) {
            _freelancersListLiveData.value = value
        }

    var subCategories: List<SubCategory> = emptyList()
        private set

    fun updateUser(newUser: User?) {
        user = newUser
    }

    fun updateSubCategories(newSubCategories: List<SubCategory>) {
        subCategories = newSubCategories
    }

    fun updateOrderList(newOrderList: List<Order>) {
        _orderListLiveData.value = newOrderList
    }

    fun updateProposalsList(newProposalsList: List<Proposals>) {
        proposals = newProposalsList
    }

    fun updateFreelancersList(newFreelancersList: List<User>) {
        freelancers = newFreelancersList
    }
}
