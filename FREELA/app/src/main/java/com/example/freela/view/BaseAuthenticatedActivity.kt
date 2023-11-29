package com.example.freela.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.freela.ChatFragment
import com.example.freela.HomeFragment
import com.example.freela.OrdersFragment
import com.example.freela.R
import com.example.freela.api.SubCategoryService
import com.example.freela.databinding.ActivityBaseAuthenticatedBinding
import com.example.freela.databinding.ActivityOrderDetailsBinding
import com.example.freela.model.Session
import com.example.freela.network.RetrofitClient
import com.example.freela.viewModel.SubCategoryViewModel
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
                R.id.menu_search -> {
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
        Log.i("TOKEN PÓS LOGIN",Session.token)
        if (Session.token.isEmpty()) {
            redirectToLogin()
        }
    }

    private fun redirectToLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Sair do App")
            .setMessage("Tem certeza de que deseja sair do aplicativo?")
            .setPositiveButton("Sim") { dialog, which ->
                removeToken()
                finish()
                redirectToLogin()
            }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun removeToken() {
        Session.token = ""
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}
