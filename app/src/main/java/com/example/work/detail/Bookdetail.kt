package com.example.work.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.work.R


class bookdetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookdetail)

        val booksname = findViewById<TextView>(R.id.bookname)
        val authors = findViewById<TextView>(R.id.authors)
        val bookcovers = findViewById<ImageView>(R.id.bookcovers)
        val bookinfo = findViewById<TextView>(R.id.bookdes)


        val name = intent.getStringExtra("name")
        val writer = intent.getStringExtra("writer")
        val coverId = intent.getIntExtra("coverId", R.drawable.book1)
        val booksinfo = intent.getStringExtra("bookInfo")


        booksname.text = name
        authors.text = writer
        bookcovers.setImageResource(coverId)
        bookinfo.text = booksinfo


    }
}