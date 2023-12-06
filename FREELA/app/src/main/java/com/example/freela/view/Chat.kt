package com.example.freela.view

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.R
import com.example.freela.adapters.MessageAdapter
import com.example.freela.adapters.oval
import com.example.freela.api.ChatService
import com.example.freela.databinding.ActivityChatBinding
import com.example.freela.databinding.ActivityLoginBinding
import com.example.freela.network.RetrofitClient
import com.example.freela.viewModel.ChatViewModel
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayInputStream

class Chat : AppCompatActivity() {

    private val binding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageInput: TextInputEditText
    private lateinit var sendButton: ImageButton
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var chatViewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val name = intent.getStringExtra("name")
        val photo = intent.getStringExtra("photo")

        if (photo == "") {
            binding.userDetailsWithoutPhoto.text = name
            binding.userDetailsWithoutPhoto.setBackgroundColor(Color.parseColor("#274C77"))
        }else {
            photo.let { photoString ->
                val userDetailsImageView = binding.userDetails
                userDetailsImageView?.visibility = View.VISIBLE
                val byteArray = Base64.decode(photoString, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(byteArray))
                binding.userDetailsWithoutPhoto.visibility = View.GONE
                userDetailsImageView?.setImageBitmap(bitmap)
            }
        }

        binding.nameUser.text = name

        val chatService = RetrofitClient.getInstance().create(ChatService::class.java)
        chatViewModel = ChatViewModel(chatService)

        recyclerView = findViewById(R.id.recyclerViewMessages)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)

        messageAdapter = MessageAdapter(emptyList())
        recyclerView.adapter = messageAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val chatId = intent.getIntExtra("chatId", -1)

        if (chatId != -1) {
            chatViewModel.getMessagesByChat(chatId)
        }

        chatViewModel.messages.observe(this, Observer { messages ->
            recyclerView.visibility = View.VISIBLE
            messages?.let {
                messageAdapter.updateMessage(messages)
                recyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)
            }
        })

        sendButton.setOnClickListener {
            // Lógica para enviar a mensagem
        }
    }
}
