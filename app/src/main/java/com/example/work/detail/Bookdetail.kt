package com.example.work.detail

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.work.Adapter.AdapterComment
import com.example.work.MainActivity
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

    //view binding
    private lateinit var binding: ActivityBookdetailBinding

    //arraylist holding comment
    private lateinit var commentArrayList: ArrayList<ModelComment>

    //adapter to set comment into recycleview
    private lateinit var adapterComment: AdapterComment

    //hold boolean for favourtie value
    private var isInMyFavourite = false

    //get bookId from intent
    private var bookId = ""

    //get data from firebase
    private var bookTitle = ""

    //getting user


    private companion object{
        //TAG
        const val TAG = "BOOK_DETAIL_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityBookdetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //init firebase authen
        firebaseAuth = FirebaseAuth.getInstance()

        if(firebaseAuth.currentUser!=null){
            //check for faveourtie for logged in user
            checkIsFavourite()
        }

        //get bookid from intent
        bookId = intent.getStringExtra("bookId")!!

        //get view count + getting view count whenever user access this page
        incrementBookViewCount(bookId)
        //load book detail
        loadBookDetails()

        //handle backbutton click, go back
        binding.backBtn.setOnClickListener{
            onBackPressed()
        }

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

        //handle click, add/remove favourite
        binding.favebtn.setOnClickListener(){
            //only login user can add favourite

            //checking if user is login or not
            if(firebaseAuth.currentUser == null){
                //not login, user can't do fav
                Toast.makeText(this,"You are not logged in",Toast.LENGTH_SHORT).show()
            }
            else{
                // login user can fave function
                if(isInMyFavourite){
                    //already in remove
                    removeFromFavourite()
                }
                else{
                    //add to fav
                    addToFavourite()
                }

            }

        }


    }

   private fun loadBookDetails(){

       //Books > bookId > Detail
       val ref = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Books")
       ref.child(bookId)
           .addListenerForSingleValueEvent(object: ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                   //get data
                   val Author = "${snapshot.child("Author").value}"
                   val BookTitle = "${snapshot.child("BookTitle").value}"
                   val Image = "${snapshot.child("Image").value}"
                   val viewCount = "${snapshot.child("viewCount").value}"
                   val dateAdded = "${snapshot.child("dateAdded").value}".toLong()
                   val avgRatings = "${snapshot.child("ratings").child("AverageRatings").value}"
                   //set data
                   binding.authors.text = Author
                   binding.bookname.text = BookTitle
                   binding.bookdetailRating.rating = avgRatings.toFloat()
                   Glide.with(this@bookdetail).load(Image.toString()).into(binding.bookcovers)
                   val date = MainActivity.formatTimeStampT(dateAdded)
                   binding.dateadded.text = date
                   binding.viewcount.text = viewCount
                   //set bookcover
                   //binding.bookcovers.setImageURI(Image.toUri())
               }

               override fun onCancelled(error: DatabaseError) {
                   TODO("Not yet implemented")
               }

           })
   }

    private fun showComments() {
        //init arraylist
        commentArrayList = ArrayList()

        //path to db, loading comment
        val ref = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Books")
        ref.child(bookId).child("Comments")
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
        //progressDialog.setMessage("Adding Comment")
        //progressDialog.show()

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

        val ref = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Books")
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
        val uref = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")
        uref.child(firebaseAuth.uid!!).child("Comments").child(timestamp)
            .setValue(hashMap)
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

    private fun checkIsFavourite(){

        Log.d(TAG, "checkIsFavourite :Checking if book is in fav or not")

        val ref = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")
        ref.child(firebaseAuth.uid!!).child("Favourites").child(bookId)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    isInMyFavourite = snapshot.exists()
                    if(isInMyFavourite){
                        //available in favourite
                        binding.favebtn.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.favourite_red,0,0)
                        binding.favebtn.text = "Remove from favourite"
                    }
                    else{
                        //not available
                        binding.favebtn.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.favourite_white,0,0)
                        binding.favebtn.text = "Add to favourite"

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    private fun addToFavourite(){

        val timestamp = System.currentTimeMillis()

        //set up data to add in db
        val hashMap = HashMap<String,Any>()
        hashMap["bookId"] = bookId
        hashMap["timestamp"] = timestamp

        //save to db
        val ref = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")
        ref.child(firebaseAuth.uid!!).child("Favourite").child(bookId)
            .setValue(hashMap)
            .addOnSuccessListener {
                //add to fav
                Log.d(TAG, "addToFavourite: Added to fav")
            }
            .addOnFailureListener{
                //failed to add
                Log.d(TAG, "addToFavourite: Failed to add to fav")
                Toast.makeText(this,"Failed to add to fave",Toast.LENGTH_SHORT).show()
            }


    }

    private fun removeFromFavourite(){

        Log.d(TAG,"removeFromFavourite: Removing from fav")

        //database ref
        val ref = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")
        ref.child(firebaseAuth.uid!!).child("Favourites").child(bookId)
            .removeValue()
            .addOnSuccessListener {
                Log.d(TAG,"Removed from favourite")
            }
            .addOnFailureListener{  e->
                Log.d(TAG, "removeFromFavourite: Failed to remove")
                Toast.makeText(this, "Failed to remove from favourite", Toast.LENGTH_LONG).show()
            }
    }

    fun incrementBookViewCount(bookId: String)
    {
        val ref = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Books")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get view, check if there is no view assign, will assign new view here
                    var viewCount = "${snapshot.child("viewCount").value}"
                    if (viewCount=="" || viewCount == null){
                        viewCount = "0"
                    }
                    //increment the view count here
                    val incViewCount = viewCount.toLong() + 1
                    //pass value to db
                    val hashMap = HashMap<String, Any>()
                    hashMap["viewCount"] = incViewCount

                    val dbRef = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Books")
                    dbRef.child(bookId).updateChildren(hashMap)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

}
