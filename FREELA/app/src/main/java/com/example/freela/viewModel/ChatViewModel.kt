package com.example.freela.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.freela.api.ChatService
import com.example.freela.model.Chat
import com.example.freela.model.Message
import com.example.freela.model.Session
import com.example.freela.model.dto.request.ChatRequest
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

    private val _messages = MutableLiveData<List<Message>>() // MutableLiveData para armazenar as mensagens
    val messages: LiveData<List<Message>> // LiveData exposto para observação externa
        get() = _messages

    // Função para obter a lista de chats
    fun getChats() {
        chatService.getChats("Bearer ${Session.token}", Session.user?.id, Session.user?.isFreelancer)
            .enqueue(object : Callback<List<Chat>> {
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

    // Função para obter as mensagens de um chat específico
    fun getMessagesByChat(id: Int) {
        chatService.getMessagesByChat("Bearer ${Session.token}", id)
            .enqueue(object : Callback<List<Message>> {
                override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                    if (response.isSuccessful) {
                        val messages = response.body()
                        messages?.let {
                            _messages.value = it
                            Log.i("Mensagens do Chat", response.toString())
                        }
                    } else {
                        Log.i("Mensagens do Chat Erro", response.toString())
                    }
                }

                override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                    Log.i("Mensagens do Chat Erro", t.message.toString())
                }
            })
    }

    fun createChat(freelancerId: Int, userId: Int, orderId: Int, lastUpdate: String): LiveData<Boolean> {
        val newChat = ChatRequest(freelancerId, userId, orderId, lastUpdate)
        val success = MutableLiveData<Boolean>()

        chatService.createChat("Bearer ${Session.token}", newChat).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Lógica após o chat ser criado com sucesso
                    Log.i("CreateChat", "Chat created successfully")
                    getChats()
                    success.postValue(true)

                } else {
                    Log.i("CreateChat", response.toString())
                    success.postValue(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                success.postValue(false)
            }
        })

        return success
    }



}
