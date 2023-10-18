package com.example.freela.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.freela.R

class activity_register_secund : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_secund)

        val button : ImageView = findViewById(R.id.btnNext);
        val btnReturn : ImageView = findViewById(R.id.btnreturn);
        button.setOnClickListener {
            val intent = Intent(this, activity_register_third::class.java)
            startActivity(intent)
        }

        btnReturn.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}