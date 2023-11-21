package com.example.freela.view

import MyViewAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.freela.databinding.ActivityOrderDetailsBinding
import com.example.freela.model.Order
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class OrderDetails : AppCompatActivity() {
    private val binding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }
    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager2 : ViewPager2
    private lateinit var myViewPagerAdapter: MyViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val order = intent.getParcelableExtra<Order>("order")
        if(order != null){
            binding.title.text = order.title
            binding.prize.text = "R$${order.value.toString()}"
            binding.deadline.text = order.deadline
            myViewPagerAdapter = MyViewAdapter(this);
            myViewPagerAdapter.setOrder(order)
            binding.viewPager.adapter = myViewPagerAdapter
            binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        binding.viewPager.setCurrentItem(tab.position)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // Implement your logic here for when a tab is unselected
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // Implement your logic here for when a tab is reselected
                }
            })
            binding.btnReturn.setOnClickListener{
                val intent = Intent(this, BaseAuthenticatedActivity::class.java)
                startActivity(intent)
                finish()
            }
            binding.createProposal.setOnClickListener{
                val intent = Intent(this, CreateProposal::class.java)
                intent.putExtra("order", order)
                startActivity(intent)
                finish()

            }
        }


    }
}