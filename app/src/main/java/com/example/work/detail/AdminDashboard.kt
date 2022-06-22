package com.example.work.detail

import android.app.ProgressDialog
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.work.R
import com.example.work.login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdminDashboard : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)
        firebaseAuth = FirebaseAuth.getInstance()
        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        val BookTitle = findViewById<EditText>(R.id.booknameEt)
        val IdBook = findViewById<EditText>(R.id.bookidEt)
        val Author = findViewById<EditText>(R.id.authorEt)
        val Category = findViewById<EditText>(R.id.categoryEt)
        val Detail = findViewById<EditText>(R.id.detailEt)
        val Barcode = findViewById<EditText>(R.id.barcodeEt)
        backBtn.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, login::class.java))
            finish()
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Waiting...")
        progressDialog.setCanceledOnTouchOutside(false)

        val Submit = findViewById<Button>(R.id.submitbtn)
        val cleardata = findViewById<TextView>(R.id.cleardata)

        Submit.setOnClickListener{

            if (BookTitle.text.toString().isEmpty() && IdBook.text.toString().isEmpty()
                && Author.text.toString().isEmpty() && Category.text.toString().isEmpty()
                && Detail.text.toString().isEmpty())
            {
                Toast.makeText(
                    applicationContext,
                    "Fill in the box", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (BookTitle.text.toString().isEmpty())
            {
                Toast.makeText(
                    applicationContext,
                    "Pleas fill Book Title", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (IdBook.text.toString().isEmpty())
            {
                Toast.makeText(
                    applicationContext,
                    "Pleas fill Book id", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (Author.text.toString().isEmpty())
            {
                Toast.makeText(
                    applicationContext,
                    "Pleas fill Author", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (Category.text.toString().isEmpty())
            {
                Toast.makeText(
                    applicationContext,
                    "Pleas fill Category", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (Detail.text.toString().isEmpty())
            {
                Toast.makeText(
                    applicationContext,
                    "Pleas fill Book Summary", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (Barcode.text.toString().isEmpty())
            {
                Toast.makeText(
                    applicationContext,
                    "Pleas fill Book barcode", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }


            //if input properly, will create new book and send to firebase
            else {
                progressDialog.setMessage("Creating New Book...")
                progressDialog.show()
                addBook()

            }
        }

        cleardata.setOnClickListener {
            BookTitle.text = null
            IdBook.text = null
            Author.text = null
            Category.text = null
            Detail.text = null
            Barcode.text = null
        }

    }

    private fun addBook(){
        // showing progress
        //progressDialog.setMessage("Adding Book")
        //progressDialog.show()
        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        val BookTitle = findViewById<EditText>(R.id.booknameEt)
        val IdBook = findViewById<EditText>(R.id.bookidEt)
        val Author = findViewById<EditText>(R.id.authorEt)
        val Category = findViewById<EditText>(R.id.categoryEt)
        val Detail = findViewById<EditText>(R.id.detailEt)
        val viewCount = 0
        val Barcode = findViewById<EditText>(R.id.barcodeEt)
        val timestamp = "${System.currentTimeMillis()}"
        val AverageRatings = 0.0
        val numUserRated = 0
        //setting up data to add comment in database

        val hashMap = HashMap<String, Any>()
        hashMap["Author"] = Author.text.toString().trim()
        hashMap["AverageRatings"] = AverageRatings.toFloat()
        hashMap["Barcode"] = Barcode.text.toString().toLong()
        hashMap["BookTitle"] = BookTitle.text.toString().trim()
        hashMap["bookId"] = IdBook.text.toString().trim()
        hashMap["bookSummary"] = Detail.text.toString().trim()
        hashMap["category"] = Category.text.toString().trim()
        hashMap["Image"] = ""
        hashMap["dateAdded"] = timestamp.toLong()
        hashMap["viewCount"] = viewCount.toLong()
        hashMap["numUserRated"] = numUserRated.toLong()
        //add data into the the database
        //path book > bookid > comment > commentId > commentdata

        val ref = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Books")

        ref.child(IdBook.text.toString().trim())
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Book added", Toast.LENGTH_SHORT).show()
                BookTitle.text = null
                IdBook.text = null
                Author.text = null
                Category.text = null
                Detail.text = null
                Barcode.text = null
            }
            .addOnFailureListener{ e->
                progressDialog.dismiss()
                Toast.makeText(this,"Failed to add book", Toast.LENGTH_SHORT).show()
            }

    }

}