package com.example.work.ui.booklist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.work.Adapter.BookAdapter
import com.example.work.R
import com.example.work.data.Bookdata
import com.example.work.databinding.ActivityNewbookBinding
import com.google.firebase.firestore.FirebaseFirestore

class newbook : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newbooklist : ArrayList<Bookdata>
    private lateinit var newbookadapter : BookAdapter
    private lateinit var db : FirebaseFirestore

    //view binding
    private lateinit var binding: ActivityNewbookBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityNewbookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.newpage)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        newbooklist = arrayListOf()
        newbookadapter = BookAdapter(this, newbooklist)
        recyclerView.adapter = newbookadapter

        binding.backBtn.setOnClickListener{
            onBackPressed()
        }


    }
}