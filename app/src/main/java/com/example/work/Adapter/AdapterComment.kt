package com.example.work.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.work.MainActivity
import com.example.work.R
import com.example.work.Model.ModelComment
import com.example.work.databinding.RowCommentBinding
import com.example.work.detail.bookdetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class AdapterComment: RecyclerView.Adapter<AdapterComment.HolderComment> {

    //context
    val context: Context

    //arraylist for hold comment
    val commentArrayList: ArrayList<ModelComment>

    //view binding for row_comment => Rowcommentbinding
    private lateinit var binding: RowCommentBinding

    private lateinit var firebaseAuth: FirebaseAuth


    //hold boolean for favourtie value
    private var isInMyBookmark = false

    //constructor
    constructor(context: Context, commentArrayList: ArrayList<ModelComment>){

        this.context = context
        this.commentArrayList = commentArrayList

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderComment {
        //binding / inflate row_comment

        binding = RowCommentBinding.inflate(LayoutInflater.from(context),parent,false)

        return HolderComment(binding.root)

    }


    override fun onBindViewHolder(holder: HolderComment, position: Int) {

        //get data
        val model = commentArrayList[position]

        val bookId = model.bookId
        val comment = model.comment
        val uid = model.uid
        val timestamp = model.timestamp
        val commentId = model.id
        val userRating = model.userRating

        val selectedposition = -1
        //timestamp format
        val date = MainActivity.formatTimeStamp(timestamp.toLong())
        //init firebase
        firebaseAuth = FirebaseAuth.getInstance()


        //isInMyBookmark = false
        //set data
        holder.dateTv.text = date
        holder.commentTv.text = comment
        if (userRating == 0.0) {
            binding.commentRating.visibility = View.GONE
        } else {
            binding.commentRating.rating = userRating!!.toFloat()
        }

        if (comment == "") {
            binding.bookmarkbtn.visibility = View.GONE
        } else {
            binding.bookmarkbtn.visibility = View.VISIBLE
        }
        //
        loadUserDetails(model, holder)

        holder.itemView.setOnClickListener{
            //user can delete their own comment
            if(firebaseAuth.currentUser != null && firebaseAuth.uid == uid){
                deleteCommentDialog(model, holder)

            }
        }



        binding.bookmarkbtn.setOnClickListener(){
            //only login user can add favourite

            //checking if user is login or not
            if(firebaseAuth.currentUser == null){

                //not login, user can't do bookmark
                Toast.makeText(context,"You are not logged in",Toast.LENGTH_SHORT).show()
            }
            else{
                // login user can fave function
                if(isInMyBookmark){
                    //already in remove
                    removeFromBookmark(commentId)

                }
                else{
                    //add to fav

                    addToBookmark(model)

                }

            }

        }

        if(firebaseAuth.currentUser != null){
            //check for bookmark for logged in user
            checkIsBookmark(model)
        }

    }

    //for bookmark comment

    private fun checkIsBookmark(model: ModelComment){

        Log.d(bookdetail.TAG, "checkIsBookmark :Checking if book is in bookmark or not")

        val ref = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users").child(firebaseAuth.uid!!).child("Bookmark")



        ref.orderByChild("id").equalTo(model.id)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    isInMyBookmark = snapshot.exists()
                    if(isInMyBookmark){
                        //available in favourite

                        binding.bookmarkbtn
                            .setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.bookmarkbtn_yellow,0,0)
                        //binding.bookmarkbtn.text = "Remove from bookmark"
                    }
                    else{
                        //not available
                        binding.bookmarkbtn
                            .setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.bookmarkbtn,0,0)
                        //binding.bookmarkbtn.text = "Add to bookmark"
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    private fun addToBookmark(model: ModelComment){

        val timestamp = "${System.currentTimeMillis()}"

        //set up data to add in db
        val hashMap = HashMap<String,Any>()
        hashMap["id"] = model.id
        hashMap["bookId"] = model.bookId
        hashMap["timestamp"] = "$timestamp"
        hashMap["comment"] = model.comment
        hashMap["uid"] = model.uid
        hashMap["userRating"] = "${model.userRating}".toDouble()

        //save to db
        val ref = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")
        ref.child(firebaseAuth.uid!!).child("Bookmark").child(model.id)
            .setValue(hashMap)
            .addOnSuccessListener {
                //add to fav
                Log.d(bookdetail.TAG, "addToFavourite: Added to bookmark")
            }
            .addOnFailureListener{
                //failed to add
                Log.d(bookdetail.TAG, "addToFavourite: Failed to add to bookmark")
                Toast.makeText(context,"Failed to add to bookmark",Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeFromBookmark(commentId: String){

        Log.d(bookdetail.TAG,"removeFromBookmark: Removing from bookmark")

        //database ref
        val ref = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")
        ref.child(firebaseAuth.uid!!).child("Bookmark").child(commentId)
            .removeValue()
            .addOnSuccessListener {
                Log.d(bookdetail.TAG,"Removed from Bookmark")
            }
            .addOnFailureListener{  e->
                Log.d(bookdetail.TAG, "removeFromBookmark: Failed to remove")
                Toast.makeText(context, "Failed to remove from Bookmark", Toast.LENGTH_LONG).show()
            }
    }


    //hold the comment to delete
    private fun deleteCommentDialog(model: ModelComment, holder: AdapterComment.HolderComment) {

        //alert dialog
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Comment")
            .setMessage("Are you sure?")
            .setPositiveButton("Delete"){ d,e->

                val bookId = model.bookId
                val commentId = model.id



                val bref  = FirebaseDatabase
                    .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("users")

                bref.addValueEventListener(object : ValueEventListener{

                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (ds in snapshot.children)
                        {
                            if(ds.child("Bookmark").hasChild(commentId))
                            {
                                ds.ref.child("Bookmark").child(commentId).removeValue()
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })

                val ref = FirebaseDatabase
                    .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Books")

                ref.child(model.bookId).child("Comments").child(commentId)
                    .removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(context,"Comment is deleted", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener{
                        // failed to delete
                        Toast.makeText(context,"Failed to delete comment", Toast.LENGTH_SHORT).show()
                    }

                val uref = FirebaseDatabase
                    .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("users")

                uref.child(firebaseAuth.uid!!).child("Comments").child(commentId)
                    .removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(context,"Comment is deleted", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener{
                        // failed to delete
                        Toast.makeText(context,"Failed to delete comment", Toast.LENGTH_SHORT).show()
                    }

                //uref.child("Bookmark").child(commentId).removeValue()
                    totalRatings(model.bookId)
                    totalUserGiveRate(model.bookId)


            }
            .setNegativeButton("Cancel"){d,e->
                d.dismiss()
            }
            .show()


    }


    private fun totalRatings(bookId: String){
        var total = 0.0
        var count = 0
        var avgRatings = 0.0

        //path to db, get ratings

        val ref = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Books")
        ref.child(bookId).child("Comments")
            .addValueEventListener(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {

                    for (ds in snapshot.children){
                        val avgrate = ds.child("userRating").value.toString()
                        total = total.plus(avgrate.toDouble())
                        count = count.plus(1)
                        avgRatings = total.div(count)

                    }
                    val ref = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Books")

                    ref.child(bookId).child("AverageRatings").setValue(avgRatings)

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun totalUserGiveRate(bookId: String){
        var total = 0
        var count = 0
        var allUserRateCount = 0

        //path to db, get ratings

        val ref = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Books")

        ref.child(bookId)
            .addListenerForSingleValueEvent(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {

                    val totalUserGiveRate = "${snapshot.child("numUserRated").value}".toInt()
                    total = total.plus(totalUserGiveRate)
                    allUserRateCount = total.minus(1)

                    val ref = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Books")
                    ref.child(bookId).child("numUserRated").setValue(allUserRateCount.toLong())

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun loadUserDetails(model: ModelComment, holder: AdapterComment.HolderComment)
    {
        val uid = model.uid
        val ref = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")

        ref.child(uid)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //getting profile img and name

                    val name = "" + snapshot.child("username").value
                    val profileImage = "${snapshot.child("profileImage").value}"

                    //setting data
                    holder.nameTv.text = name

                    try{
                        Glide.with(context)
                            .load(profileImage)
                            //.placeholder(R.drawable.ic_baseline_person_24)
                            .into(holder.profileIv)
                    }
                    catch (e: Exception){
                        //if there is no image
                        holder.profileIv.setImageResource(R.drawable.ic_baseline_person_24)
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    override fun getItemCount(): Int {
        return commentArrayList.size
    }

    inner class HolderComment(itemView: CardView): RecyclerView.ViewHolder(itemView){

        val profileIv: ImageView = binding.profileIv
        val nameTv: TextView = binding.nameTv
        val dateTv: TextView = binding.dateTv
        val commentTv: TextView = binding.commentTv
    }
}