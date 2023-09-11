package com.example.freela

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val button : Button = findViewById(R.id.entrar);
        val redirectLogin : TextView = findViewById(R.id.redirect);
        val btnReturn : ImageView = findViewById(R.id.btnreturn)

        button.setOnClickListener {
            val intent = Intent(this, Chat::class.java)
            startActivity(intent)
        }

        redirectLogin.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        btnReturn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}