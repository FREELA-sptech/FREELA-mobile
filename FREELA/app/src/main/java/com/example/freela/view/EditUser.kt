package com.example.freela.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.freela.databinding.ActivityEditUserBinding

class EditUser : AppCompatActivity() {
    private val binding by lazy {
        ActivityEditUserBinding.inflate(layoutInflater);
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnreturn.setOnClickListener {
            val intent = Intent(this, UserDetailsActivity::class.java);
            startActivity(intent)
            finish()
        }

        binding.subCategories.setOnClickListener{
            val intent = Intent(this, UpdateSubCategories::class.java);
            startActivity(intent)
        }

        binding.name.setOnClickListener {
            val intent = Intent(this, UpdateNameUser::class.java);
            startActivity(intent)
        }

        binding.city.setOnClickListener{
            val intent = Intent(this, UpdateCityUser::class.java);
            startActivity(intent)
        }
    }


}