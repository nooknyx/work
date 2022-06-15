package com.example.work.Adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView

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

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderComment {
        //binding / inflate row_comment

        binding = RowCommentBinding.inflate(LayoutInflater.from(context),parent,false)

        return HolderComment(binding.root)

    }

    override fun onBindViewHolder(holder: HolderComment, position: Int) {

        //get data
        val model = commentArrayList[position]

        val id = model.id
        val bookId = model.bookId
        val comment = model.comment
        val uid = model.uid
        val timestamp = model.timestamp
        val commentid = model.id

        //timestamp format
        val date = MainActivity.formatTimeStamp(timestamp.toLong())

        //set data
        holder.dateTv.text = date
        holder.commentTv.text = comment

        //
        loadUserDetails(model, holder)

        holder.itemView.setOnClickListener{
            //user can delete their own comment
            if(firebaseAuth.currentUser != null && firebaseAuth.uid == uid){
                deleteCommentDialog(model, holder)
            }
        }
    }

    //for bookmark comment

    private fun checkIsBookmark(){

        Log.d(bookdetail.TAG, "checkIsFavourite :Checking if book is in fav or not")

        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(firebaseAuth.uid!!).child("Comments").child("id")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    isInMyBookmark = snapshot.exists()
                    if(isInMyBookmark){
                        //available in favourite
                        binding.bookmarkbtn
                            .setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.bookmarkbtn_yellow,0,0)
                        binding.bookmarkbtn.text = "Remove from bookmark"
                    }
                    else{
                        //not available
                        binding.bookmarkbtn
                            .setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.bookmarkbtn,0,0)
                        binding.bookmarkbtn.text = "Add to bookmark"

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    private fun addToBookmark(){

        val timestamp = System.currentTimeMillis()

        //set up data to add in db
        val hashMap = HashMap<String,Any>()
        hashMap["commentid"] = timestamp
        hashMap["timestamp"] = timestamp

        //save to db
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(firebaseAuth.uid!!).child("Comments").child("id")
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

    private fun removeFromBookmark(){

        Log.d(bookdetail.TAG,"removeFromBookmark: Removing from fav")

        //database ref
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(firebaseAuth.uid!!).child("Comments").child("id")
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

                val ref = FirebaseDatabase
                    .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Books")

                ref.child(model.bookId).child("comments").child(commentId)
                    .removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(context,"Comment is deleted", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener{
                        // failed to delete
                        Toast.makeText(context,"Failed to delete comment", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancle"){d,e->
                d.dismiss()
            }
            .show()
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

                    val name = "" + snapshot.child("name").value
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