package com.example.work.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.work.R
import com.example.work.databinding.ActivityAdminDashboardBinding

class AdminDashboard : AppCompatActivity() {

    //binding
    private lateinit var binding: ActivityAdminDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set back btn
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        //add function for adding book to the database





    }
}