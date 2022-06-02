package com.example.work.ui.booklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.work.Adapter.BookAdapter
import com.example.work.R
import com.example.work.data.Bookdata
import com.example.work.databinding.ActivityAllbookBinding
import com.example.work.databinding.ActivityNewbookBinding
import com.google.firebase.firestore.FirebaseFirestore

class allbook : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var allbooklist: ArrayList<Bookdata>
    private lateinit var allbookadapter: BookAdapter
    private lateinit var db: FirebaseFirestore

    //view binding
    private lateinit var binding: ActivityAllbookBinding



    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityAllbookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.allbookpage
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        allbooklist = arrayListOf()
        allbookadapter = BookAdapter(this, allbooklist)
        recyclerView.adapter = allbookadapter

        binding.backBtn.setOnClickListener{
            onBackPressed()
        }

        //handle backbutton click, go back
        binding.backBtn.setOnClickListener{
            onBackPressed()
        }


    }
}