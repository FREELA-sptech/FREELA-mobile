package com.example.freela

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.freela.api.AuthService
import com.example.freela.model.User
import com.example.freela.network.RetrofitClient
import com.example.freela.view.UserDetailsActivity
import com.google.android.material.button.MaterialButton
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var userDetails: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnUser = view.findViewById<CircleImageView>(R.id.userDetails)
        val createOrder = view.findViewById<MaterialButton>(R.id.createOrder)

        val preferences = requireActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val token = preferences.getString("TOKEN", null)

        if (token != null) {
            fetchUserDetails(token)

            btnUser.setOnClickListener {
                userDetails?.let { user ->
                    val intent = Intent(activity, UserDetailsActivity::class.java)
                    intent.putExtra("userId", user.id)
                    startActivity(intent)
                }
            }

            createOrder.setOnClickListener {
                userDetails?.let { user ->
                    val intent = Intent(activity, CreateOrder::class.java)
                    intent.putExtra("userId", user.id)
                    startActivity(intent)
                }
            }
        }
    }


    private fun fetchUserDetails(token: String) {
        RetrofitClient.getInstance()
            .create(AuthService::class.java)
            .userDetails("Bearer $token")
            .enqueue(object : Callback<User> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        val textView = view?.findViewById<TextView>(R.id.hello)
                        val button = view?.findViewById<MaterialButton>(R.id.createOrder)
                        if (user != null) {
                            if(user.isFreelancer){
                                if (button != null) {
                                    button.visibility = View.GONE
                                }
                            }
                            if (textView != null) {
                                textView.text = "Olá, ${user.name}"

                            }
                            userDetails = user
                        };


                    } else {
                    }
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("ERRO NA API",t.message.toString())
                }
            })

    }
}
