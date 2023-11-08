package com.example.freela

import android.annotation.SuppressLint
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
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    private lateinit var userDetails: User

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnUser = view.findViewById<CircleImageView>(R.id.userDetails)

        val preferences = requireActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val token = preferences.getString("TOKEN", null)

        if (token != null) {
            fetchUserDetails(token)
            val intent = Intent(this, UserDetailsActivity::class.java)
            intent.putExtra("userId", userDetails.id)

            btnUser.setOnClickListener {
                startActivity(intent)
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
                        if (user != null) {
                            userDetails = user
                        };
                        val textView = view?.findViewById<TextView>(R.id.hello)
                        if (textView != null) {
                            textView.text = "Olá, ${user?.name}"
                        }
                    } else {
                    }
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("ERRO NA API",t.message.toString())
                }
            })

    }
}
