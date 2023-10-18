package com.example.freela.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.freela.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.entrar.setOnClickListener {
            val intent = Intent(this, Chat::class.java)

            val inputEmail = binding.email.text.toString();
            val inputPassword = binding.password.text.toString();

            if (!isValidEmail(inputEmail)){
                binding.email.error = "Email inválido"
            }else if(inputPassword.length < 8){
                binding.password.error = "Senha muito curta"
            }else{
                startActivity(intent)
            }

        }

        binding.redirect.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        binding.btnreturn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}