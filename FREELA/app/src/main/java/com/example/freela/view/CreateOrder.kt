package com.example.freela.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.freela.R
import com.example.freela.databinding.ActivityCreateOrderBinding

class CreateOrder : AppCompatActivity() {
    private val binding by lazy {
        ActivityCreateOrderBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)




    }
}