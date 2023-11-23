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

        binding.btnreturn.setOnClickListener{
            val intent = Intent(this, BaseAuthenticatedActivity::class.java)
            startActivity(intent)
        }

        binding.editUser.setOnClickListener{
            val intent = Intent(this, EditUser::class.java)
            startActivity(intent)
        }
    }
}