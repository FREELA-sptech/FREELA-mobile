package com.example.freela.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.freela.R
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.example.freela.api.SubCategoryService
import com.example.freela.model.Session
import com.example.freela.network.RetrofitClient
import com.example.freela.viewModel.SubCategoryViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var subCategoryViewModel: SubCategoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        val subCategoryService = RetrofitClient.getInstance().create(SubCategoryService::class.java)
        subCategoryViewModel = SubCategoryViewModel(subCategoryService)
        subCategoryViewModel.getSubCategories()

        val button : Button = findViewById(R.id.start);
        val redirectLogin : TextView = findViewById(R.id.entrar);

        if (Session.token.isNotEmpty()) {
            val redirect = Intent(this, BaseAuthenticatedActivity::class.java)
            startActivity(redirect)
        }

        button.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        redirectLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}