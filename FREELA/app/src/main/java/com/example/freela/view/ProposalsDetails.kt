package com.example.freela.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.freela.R
import com.example.freela.model.Order
import com.example.freela.model.Proposals

class ProposalsDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proposals_details)
        val proposals = intent.getParcelableExtra<Proposals>("proposals")
    }
}