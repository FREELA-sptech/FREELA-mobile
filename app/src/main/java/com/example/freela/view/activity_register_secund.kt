package com.example.freela.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import com.example.freela.R
import com.example.freela.databinding.ActivityRegisterFirstBinding
import com.example.freela.databinding.ActivityRegisterSecundBinding

class activity_register_secund : AppCompatActivity() {
    private val binding by lazy {
        ActivityRegisterSecundBinding.inflate(layoutInflater)
    }
    private val selectedCategories = mutableSetOf<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val cardSelected = intent.getStringExtra("type")

        val checkBox1 = findViewById<CheckBox>(R.id.checkBox1)
        val checkBox2 = findViewById<CheckBox>(R.id.checkBox2)
        val checkBox3 = findViewById<CheckBox>(R.id.checkBox3)

        val checkBoxClickListener = View.OnClickListener { view ->
            val checkBox = view as CheckBox
            val categoryNumber = checkBox.tag.toString().toInt()

            if (checkBox.isChecked) {
                selectedCategories.add(categoryNumber)
                binding.btnNext.setBackgroundColor(resources.getColor(R.color.submitBtnColor))
            } else {
                selectedCategories.remove(categoryNumber)
            }

            if(selectedCategories.size == 0 ){
                binding.btnNext.setBackgroundColor(resources.getColor(R.color.disableBtnColor))
            }

        }

        checkBox1.setOnClickListener(checkBoxClickListener)
        checkBox2.setOnClickListener(checkBoxClickListener)
        checkBox3.setOnClickListener(checkBoxClickListener)

        binding.btnNext.setOnClickListener {
            if(selectedCategories.size <= 0 ){
                binding.errorMessage.visibility = View.VISIBLE
                binding.errorMessage.text = "Selecione pelo menos uma categoria!"
            }else {
                binding.errorMessage.visibility = View.GONE
                val selectedCategoryIds = ArrayList(selectedCategories)

                val intent = Intent(this, activity_register_third::class.java)
                intent.putIntegerArrayListExtra("selectedCategoryIds", selectedCategoryIds)
                intent.putExtra("type", cardSelected)
                startActivity(intent)
            }
        }

        binding.btnreturn.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}