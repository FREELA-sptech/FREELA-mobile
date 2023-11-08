package com.example.freela.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.freela.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val button : Button = findViewById(R.id.start);
        val redirectLogin : TextView = findViewById(R.id.entrar);

        val preferences = getSharedPreferences("AUTH", MODE_PRIVATE)
        val token = preferences.getString("TOKEN", null)

        if (token != null) {
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