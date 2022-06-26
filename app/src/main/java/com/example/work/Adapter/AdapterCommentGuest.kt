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

class AdapterCommentGuest: RecyclerView.Adapter<AdapterCommentGuest.HolderComment> {

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


        binding.bookmarkbtn.setOnClickListener(){
            //only login user can add favourite

            //checking if user is login or not
            if(firebaseAuth.currentUser == null){
                //not login, user can't do bookmark
                Toast.makeText(context,"You are not logged in",Toast.LENGTH_SHORT).show()
            }

        }



    }

    private fun loadUserDetails(model: ModelComment, holder: AdapterCommentGuest.HolderComment)
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