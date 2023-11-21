package com.example.freela.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.freela.R
import com.example.freela.api.AuthService
import com.example.freela.databinding.ActivityRegisterThirdBinding
import com.example.freela.databinding.ActivityUserDetailsBinding
import com.example.freela.model.Session
import com.example.freela.model.User
import com.example.freela.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailsActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityUserDetailsBinding.inflate(layoutInflater);
    }

    private lateinit var userDetails: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val sharedPreferences: SharedPreferences = getSharedPreferences("AUTH", MODE_PRIVATE)
        val token: String? = sharedPreferences.getString("TOKEN", null)

        binding.btnReturn.setOnClickListener{
            val intent = Intent(this, BaseAuthenticatedActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.exit.setOnClickListener{
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.remove("TOKEN")
            editor.apply()
            val intent = Intent(this, BaseAuthenticatedActivity::class.java)
            Session.token = ""
            startActivity(intent)
            finish()
        }


        if (token != null) {
            fetchUserDetails(token)
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
                            binding.name.setText(user.name)
                            binding.city.setText("${user.city}, ${user.uf}")
                            if(user.isFreelancer){
                                binding.TitleDescription.visibility = View.VISIBLE
                                binding.description.setText(user.description)
                            }
                            binding.name.setText(user.name)
                            userDetails = user

                        };
                    }
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("ERRO NA API",t.message.toString())
                }
            })

    }
}