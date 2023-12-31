package com.example.freela.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.freela.R
import com.example.freela.databinding.ActivityLoginBinding
import com.example.freela.databinding.ActivityRegisterFirstBinding

class Register : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterFirstBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var selectedCard = ""

        binding.autonomoCard.setOnClickListener {
            selectedCard = "Autônomo"
            binding.autonomoCard.setCardBackgroundColor(resources.getColor(R.color.selectedCardColor))
            binding.clienteCard.setCardBackgroundColor(resources.getColor(R.color.defaultCardColor))
            redirectActivity(selectedCard)
        }

        binding.clienteCard.setOnClickListener {
            selectedCard = "Cliente"
            binding.clienteCard.setCardBackgroundColor(resources.getColor(R.color.selectedCardColor))
            binding.autonomoCard.setCardBackgroundColor(resources.getColor(R.color.defaultCardColor))
            redirectActivity(selectedCard)
        }

        binding.btnreturn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun redirectActivity(selectedCard: String){
        val intent = Intent(this, activity_register_secund::class.java)
        intent.putExtra("type", selectedCard)
        startActivity(intent)
    }
}