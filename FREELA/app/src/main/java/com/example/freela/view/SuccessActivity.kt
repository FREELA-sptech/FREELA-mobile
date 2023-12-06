package com.example.freela.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.freela.databinding.ActivityRegisterThirdBinding
import com.example.freela.databinding.ActivitySuccessBinding

class SuccessActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySuccessBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val intent = intent.getParcelableExtra<Intent>("redirect")
        val action = intent?.getStringExtra("action")
        val message = intent?.getStringExtra("message")

        binding.action.text = action
        binding.message.text = message
        binding.redirect.setOnClickListener{
            startActivity(intent)
            finish();
        }
    }
}