package com.example.freela

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.adapters.ChatAdapter
import com.example.freela.api.ChatService
import com.example.freela.api.OrderService
import com.example.freela.model.Chat
import com.example.freela.model.Session
import com.example.freela.model.SubCategory
import com.example.freela.network.RetrofitClient
import com.example.freela.view.CreateOrder
import com.example.freela.view.UserDetailsActivity
import com.example.freela.viewModel.ChatViewModel
import com.example.freela.viewModel.OrderViewModel
import com.google.android.material.button.MaterialButton
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayInputStream
import java.util.Locale

class ChatFragment : Fragment() {

    private lateinit var chatViewModel: ChatViewModel
    private lateinit var recyclerViewChats: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chat: List<Chat>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatService = RetrofitClient.getInstance().create(ChatService::class.java)
        chatViewModel = ChatViewModel(chatService)

        val btnUser = view.findViewById<CircleImageView>(R.id.userDetails)
        val createOrder = view.findViewById<MaterialButton>(R.id.createOrder)
        val textView = view.findViewById<TextView>(R.id.hello)
        val txtIcon = view.findViewById<TextView>(R.id.userDetailsWithoutPhoto)
        val message = view.findViewById<TextView>(R.id.subTitle)
        val noContext = view.findViewById<ConstraintLayout>(R.id.noContent)
        val search = view.findViewById<SearchView>(R.id.search)

        recyclerViewChats = view.findViewById(R.id.recyclerViewChats)

        val user = Session.user
        textView?.text = "Olá, ${user?.name}"

        if (user?.photo == "") {
            val hash = user.hashCode()
            txtIcon?.text = user.name.first().toString()
            txtIcon?.background = view.oval(Color.parseColor("#274C77"))
            txtIcon?.setOnClickListener {
                val intent = Intent(activity, UserDetailsActivity::class.java)
                startActivity(intent)
            }
        } else {
            val userDetailsImageView = view.findViewById<CircleImageView>(R.id.userDetails)
            userDetailsImageView?.visibility = View.VISIBLE
            val byteArray = Base64.decode(user?.photo, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(byteArray))
            txtIcon?.visibility = View.GONE
            userDetailsImageView?.setImageBitmap(bitmap)
        }

        // Observar as mudanças na lista de chats
        chatViewModel.chats.observe(viewLifecycleOwner, Observer { chats ->

            if (chats.isNullOrEmpty()) {
                message.text = if (user?.isFreelancer == true) {
                    view.context.getText(R.string.messageChatFreelancer)
                } else {
                    view.context.getText(R.string.messageChatCliente)
                }
                recyclerViewChats.visibility = View.GONE
            } else {
                chat = chats
                noContext.visibility = View.GONE
                recyclerViewChats.visibility = View.VISIBLE
                message.text = ""

                // Configurar o RecyclerView com o adapter apropriado
                chatAdapter = ChatAdapter(chats)
                recyclerViewChats.adapter = chatAdapter
                recyclerViewChats.layoutManager = LinearLayoutManager(view?.context)
            }
        })

        btnUser?.setOnClickListener {
            val intent = Intent(activity, UserDetailsActivity::class.java)
            startActivity(intent)
        }

        search.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        listChats()
    }

    private fun listChats() {
        chatViewModel.getChats()

        chatViewModel.chats.observe(viewLifecycleOwner, Observer { chats ->
            recyclerViewChats.visibility = View.VISIBLE
            chat = chats
            chats?.let {
                chatAdapter = ChatAdapter(chats)
                recyclerViewChats.adapter = chatAdapter
                recyclerViewChats.layoutManager = LinearLayoutManager(view?.context)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    private fun View.oval(@ColorInt color: Int): ShapeDrawable? {
        val oval = ShapeDrawable(OvalShape())
        with(oval){
            intrinsicHeight = height
            intrinsicWidth = width
            paint.color = color
        }
        return oval
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<Chat>()
                for (item in chat) {
                    if (item.userId.name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                        filteredList.add(item)
                    }
                }
            val noContext = view?.findViewById<TextView>(R.id.notFound)
            if (filteredList.isNotEmpty()) {
                noContext?.visibility = View.GONE
                recyclerViewChats.visibility = View.VISIBLE
                chatAdapter.updateChat(filteredList)
            }else{

                noContext?.visibility = View.VISIBLE
                recyclerViewChats.visibility = View.GONE
            }
        }
    }
}
