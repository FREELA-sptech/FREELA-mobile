package com.example.freela.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.icu.text.SimpleDateFormat
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
import com.example.freela.model.Session
import com.example.freela.model.SubCategory
import com.example.freela.model.User
import de.hdodenhof.circleimageview.CircleImageView
import java.text.ParseException
import java.util.Locale

class ChatAdapter(private val chats: List<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private val items = mutableListOf<Chat>()

    init {
        items.addAll(chats)
    }

    var onItemClick : ((Chat) -> Unit)? = null

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val user = Session.user
        val userDetails: CircleImageView = itemView.findViewById(R.id.userDetails)
        val userDetailsWithoutPhoto: TextView =
            itemView.findViewById(R.id.userDetailsWithoutPhoto)
        val nameUser: TextView = itemView.findViewById(R.id.nameUser)
        val lastUpdate: TextView = itemView.findViewById(R.id.lastUpdate)
        fun bind(chat: Chat) {
            var photo = ""

            if (user !== null) {
                if (user.isFreelancer) {
                    photo = chat.userId.photo
                } else {
                    photo = chat.freelancerId.photo
                }
            }


            if (photo == "") {
                userDetailsWithoutPhoto.text = chat.userId.name.first().toString()
                userDetailsWithoutPhoto.background = itemView.oval(Color.parseColor("#274C77"))
            }else{
                photo.let { photoString ->
                    val userDetailsImageView = itemView.findViewById<CircleImageView>(R.id.userDetails)
                    userDetailsImageView?.visibility = View.VISIBLE
                    val byteArray = Base64.decode(photoString, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(byteArray))
                    userDetailsWithoutPhoto.visibility = View.GONE
                    userDetailsImageView?.setImageBitmap(bitmap)
                }
            }

            if (user !== null) {
                if (user.isFreelancer) {
                    nameUser.text = chat.userId.name
                } else {
                    nameUser.text = chat.freelancerId.name
                }
            }
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            try {
                val date = inputFormat.parse(chat.lastUpdate)
                val formattedTime = outputFormat.format(date)
            } catch (e: ParseException) {
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ChatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val currentChat = items[position]
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(currentChat)
        }
        holder.bind(currentChat)
    }

    override fun getItemCount(): Int = chats.size

    fun updateChat(chats: List<Chat>) {
        items.clear()
        items.addAll(chats)
        notifyDataSetChanged()
    }
}
