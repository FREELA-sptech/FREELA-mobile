package com.example.freela

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class activity_register_third : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_third)

        val button : Button = findViewById(R.id.entrar);
        val btnReturn : ImageView = findViewById(R.id.btnreturn);
        val redirect : TextView = findViewById(R.id.redirect);

        button.setOnClickListener {
            val intent = Intent(this, activity_home::class.java)
            startActivity(intent)
        }

        btnReturn.setOnClickListener {
            val intent = Intent(this, activity_register_secund::class.java)
            startActivity(intent)
        }

        redirect.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }
}