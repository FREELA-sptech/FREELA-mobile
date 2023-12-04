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
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.api.OrderService
import com.example.freela.model.Session
import com.example.freela.network.RetrofitClient
import com.example.freela.view.CreateOrder
import com.example.freela.view.UserDetailsActivity
import com.example.freela.viewModel.OrderViewModel
import com.google.android.material.button.MaterialButton
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayInputStream
class ChatFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnUser = view.findViewById<CircleImageView>(R.id.userDetails)
        val createOrder = view.findViewById<MaterialButton>(R.id.createOrder)
        val textView = view.findViewById<TextView>(R.id.hello)
        val txtIcon = view.findViewById<TextView>(R.id.userDetailsWithoutPhoto)
        val message = view.findViewById<TextView>(R.id.subTitle)

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
        }else{
            val userDetailsImageView = view.findViewById<CircleImageView>(R.id.userDetails)
            userDetailsImageView?.visibility = View.VISIBLE
            val byteArray = Base64.decode(user?.photo, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(byteArray))
            txtIcon?.visibility = View.GONE
            userDetailsImageView?.setImageBitmap(bitmap)
        }

        if (user?.isFreelancer == true) {
            createOrder?.visibility = View.GONE
            message.text = view.context.getText(R.string.messageChatFreelancer)
        }else{
            message.text = view.context.getText(R.string.messageChatCliente)
        }

        btnUser?.setOnClickListener {
            val intent = Intent(activity, UserDetailsActivity::class.java)
            startActivity(intent)
        }
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
}