package com.example.work.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.work.Adapter.BookAdapter
import com.example.work.R
import com.example.work.data.Bookdata
import com.google.firebase.firestore.FirebaseFirestore


class bookdetail : AppCompatActivity() {
    lateinit var bookReference: FirebaseFirestore
    lateinit var bookList: MutableList<Bookdata>
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

    private fun readFirestoreData(){
        var db = bookReference.collection("book")
        db.orderBy("bookID").get()
            .addOnSuccessListener { snapshot -> //or [it] is fine
                if (snapshot != null){
                    bookList.clear()
                    val books = snapshot.toObjects(Bookdata::class.java)
                    for (book in books){
                        bookList.add(book)
                    }
                    //val adapter = BookAdapter(bookList)
                    //listView.adapter = adapter
                    Log.d("Firestore Read",books.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,"Fail to get the book data", Toast.LENGTH_SHORT).show()
            }
    }
}