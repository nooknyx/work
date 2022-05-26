package com.example.work.detail

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.work.Adapter.AdapterComment
import com.example.work.R
import com.example.work.data.Bookdata
import com.example.work.Model.ModelComment
import com.example.work.databinding.ActivityBookdetailBinding
import com.example.work.databinding.AddCommentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore


class bookdetail : AppCompatActivity() {


    lateinit var bookReference: FirebaseFirestore
    lateinit var bookList: MutableList<Bookdata>

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var binding: ActivityBookdetailBinding


    //arraylist holding comment
    private lateinit var commentArrayList: ArrayList<ModelComment>

    //adapter to set comment into recycleview
    private lateinit var adapterComment: AdapterComment

    //get bookId from intent
    private var bookId = ""

    //get data from firebase
    private var bookTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityBookdetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookId = intent.getStringExtra("bookId")!!

        /*val booksname = findViewById<TextView>(R.id.bookname)
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
        bookinfo.text = booksinfo*/

        showComments()


        binding.addCommnetBtn.setOnClickListener{

            //check if the user login or not when adding comment

            if(firebaseAuth.currentUser == null){
                Toast.makeText(this,"You're not logged in", Toast.LENGTH_SHORT).show()
            }
            else{
                addCommentDialog()
            }
        }
    }

    private fun showComments() {
        //init arraylist
        commentArrayList = ArrayList()

        //path to db, loading comment
        val ref = FirebaseDatabase.getInstance().getReference("Book")
        ref.child(bookId).child("Comment")
            .addValueEventListener(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    //clear list
                    commentArrayList.clear()
                    for (ds in snapshot.children){
                        //get data ss model
                        val model = ds.getValue(ModelComment::class.java)
                        //add to list
                        commentArrayList.add(model!!)
                    }
                    //setup adapter
                    adapterComment = AdapterComment(this@bookdetail, commentArrayList)
                    //set adapter to recycleview
                    binding.commentRv.adapter = adapterComment
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private var comment = ""

    private fun addCommentDialog(){

        //Inflate/bind view for dialog (add_comment.xml)

        val commentAddBinding = AddCommentBinding.inflate(LayoutInflater.from(this))

        val builder = AlertDialog.Builder(this,R.style.CustomDialog)
        builder.setView(commentAddBinding.root)

        val alertDialog = builder.create()
        alertDialog.show()

        commentAddBinding.backBtn.setOnClickListener{
            alertDialog.dismiss()
        }

        commentAddBinding.submitbtn.setOnClickListener{

            comment = commentAddBinding.commentEt.text.toString().trim()

            if(comment.isEmpty()){
                Toast.makeText(this,"Enter comment", Toast.LENGTH_SHORT).show()
            }
            else{
                alertDialog.dismiss()
                addComment()
            }
        }

    }

    private fun addComment(){
    // showing progress
        progressDialog.setMessage("Adding Comment")
        progressDialog.show()

        val timestamp = "${System.currentTimeMillis()}"

        //setting up data to add comment in database
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$timestamp"
        hashMap["bookId"] = "$bookId"
        hashMap["timestamp"] = "$timestamp"
        hashMap["comment"] = "$comment"
        hashMap["uid"] = "${firebaseAuth.uid}"

        //add data into the the database
        //path book > bookid > comment > commentId > commentdata

        val ref = FirebaseDatabase.getInstance().getReference("Book")
        ref.child(bookId).child("Comments").child(timestamp)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Comment added",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{ e->
                progressDialog.dismiss()
                Toast.makeText(this,"Failed to add comment", Toast.LENGTH_SHORT).show()
            }

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
