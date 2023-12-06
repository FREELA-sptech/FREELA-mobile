package com.example.freela.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.R
import com.example.freela.model.Order
import java.io.ByteArrayInputStream
import android.util.Base64
import androidx.constraintlayout.helper.widget.Carousel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freela.model.Chat
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(private val chats: List<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Defina as visualizações do item do chat aqui
        val userDetails: CircleImageView = itemView.findViewById(R.id.userDetails)
        val userDetailsWithoutPhoto: TextView = itemView.findViewById(R.id.userDetailsWithoutPhoto)
        val nameUser: TextView = itemView.findViewById(R.id.nameUser)
        val lastUpdate: TextView = itemView.findViewById(R.id.lastUpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ChatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val currentChat = chats[position]

        holder.nameUser.text = currentChat.userId.name
        holder.lastUpdate.text = currentChat.lastUpdate
    }

    override fun getItemCount(): Int {
        return chats.size
    }
}
