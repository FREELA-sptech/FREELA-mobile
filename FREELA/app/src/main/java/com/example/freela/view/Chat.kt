package com.example.freela.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.R
import com.example.freela.adapters.MessageAdapter
import com.example.freela.api.ChatService
import com.example.freela.network.RetrofitClient
import com.example.freela.viewModel.ChatViewModel
import com.google.android.material.textfield.TextInputEditText

class Chat : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageInput: TextInputEditText
    private lateinit var sendButton: ImageButton
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var chatViewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val chatService = RetrofitClient.getInstance().create(ChatService::class.java)
        chatViewModel = ChatViewModel(chatService)

        recyclerView = findViewById(R.id.recyclerViewMessages)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)

        messageAdapter = MessageAdapter(emptyList())
        recyclerView.adapter = messageAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        chatViewModel.messages.observe(this, { messages ->
            messageAdapter.updateMessages(messages)
            recyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)
        })

        val chatId = intent.getIntExtra("chatId", -1)

        if (chatId != -1) {
            chatViewModel.getMessagesByChat(chatId)
        }


        sendButton.setOnClickListener {
            // Lógica para enviar a mensagem
        }

    }
}
