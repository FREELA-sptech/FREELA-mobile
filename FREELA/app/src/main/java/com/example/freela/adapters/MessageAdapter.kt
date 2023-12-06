package com.example.freela.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.R
import com.example.freela.model.Chat
import com.example.freela.model.Message
import com.example.freela.model.Session

class MessageAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private val items = mutableListOf<Message>()

    init {
        items.addAll(messages)
    }

    var onItemClick : ((Chat) -> Unit)? = null
    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMessageLeft: TextView? = itemView.findViewById(R.id.textMessageLeft)
        val textMessageRight: TextView? = itemView.findViewById(R.id.textMessageRight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.message_left_item, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageAdapter.MessageViewHolder, position: Int) {
        if (position < items.size) {
            val message = items[position]
            val user = Session.user

            if (user != null) {
                if (message.userIdFrom == user.id) {
                    holder.textMessageLeft?.text = message.message
                    holder.textMessageRight?.visibility = View.GONE
                } else {
                    holder.textMessageRight?.text = message.message
                    holder.textMessageLeft?.visibility = View.GONE
                }
            }
        }
    }


    override fun getItemCount(): Int = items.size

    fun updateMessage(messages: List<Message>) {
        items.clear()
        items.addAll(messages)
        notifyDataSetChanged()
    }
}

