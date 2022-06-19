package com.example.work.detail

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.example.work.R
import com.example.work.login
import com.google.firebase.auth.FirebaseAuth

class AdminDashboard : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)
        firebaseAuth = FirebaseAuth.getInstance()
        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, login::class.java))
            finish()
        }
    }



}