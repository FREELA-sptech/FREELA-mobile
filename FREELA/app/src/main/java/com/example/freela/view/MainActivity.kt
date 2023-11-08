package com.example.freela.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.freela.R
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val button : Button = findViewById(R.id.start);
        val redirectLogin : TextView = findViewById(R.id.entrar);



        button.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        redirectLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Log.i("token",task.result)
                }
            }
            }
}