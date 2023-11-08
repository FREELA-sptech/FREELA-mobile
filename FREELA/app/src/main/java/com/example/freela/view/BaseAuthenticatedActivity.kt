package com.example.freela.view

import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.transition.ChangeTransform
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.freela.ChatFragment
import com.example.freela.HomeFragment
import com.example.freela.OrdersFragment
import com.example.freela.R
import com.example.freela.databinding.ActivityBaseAuthenticatedBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class BaseAuthenticatedActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityBaseAuthenticatedBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.menu_orders -> {
                    replaceFragment(OrdersFragment())
                    true
                }
                R.id.menu_chat -> {
                    replaceFragment(ChatFragment())
                    true
                }
                else -> false
            }
        }

        val preferences = getSharedPreferences("AUTH", MODE_PRIVATE)
        val token = preferences.getString("TOKEN", null)

        if (token == null ) {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        // Construa um diálogo de confirmação
        AlertDialog.Builder(this)
            .setTitle("Sair do App")
            .setMessage("Tem certeza de que deseja sair do aplicativo?")
            .setPositiveButton("Sim") { dialog, which ->
                // Aqui você remove o token (exemplo)
                removeToken()
                finish()
            }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun removeToken() {
        // Lógica para remover o token (exemplo)
        val preferences = getSharedPreferences("AUTH", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.remove("TOKEN")
        editor.apply()
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit();
    }
}
