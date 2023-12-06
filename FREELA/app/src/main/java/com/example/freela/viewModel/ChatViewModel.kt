package com.example.freela.viewModel

import android.content.pm.PackageInstaller.SessionInfo
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.freela.api.ChatService
import com.example.freela.api.OrderService
import com.example.freela.api.SubCategoryService
import com.example.freela.model.Chat
import com.example.freela.model.Order
import com.example.freela.model.Session
import com.example.freela.model.SubCategory
import com.example.freela.model.dto.request.OrderRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatViewModel(private val chatService: ChatService) : ViewModel() {
    private val _chats = MutableLiveData<List<Chat>>() // MutableLiveData para armazenar os chats
    val chats: LiveData<List<Chat>> // LiveData exposto para observação externa
        get() = _chats

    private val _chatDetails = MutableLiveData<Chat>() // MutableLiveData para armazenar os detalhes do chat
    val chatDetails: LiveData<Chat> // LiveData exposto para observação externa
        get() = _chatDetails


    fun getChats() {
        chatService.getChats("Bearer ${Session.token}", Session.user?.id, Session.user?.isFreelancer).enqueue(object : Callback<List<Chat>> {
            override fun onResponse(call: Call<List<Chat>>, response: Response<List<Chat>>) {
                if (response.isSuccessful) {
                    val chats = response.body()
                    chats?.let {
                        _chats.value = it
                        Log.i("Chats", response.toString())
                        Session.updateChatsList(chats)
                    }
                } else {
                    Log.i("Chats", response.toString())
                    Log.i("Token", Session.token.toString())
                }
            }

            override fun onFailure(call: Call<List<Chat>>, t: Throwable) {
                Log.i("Chats", t.message.toString())
            }
        })
    }
}
