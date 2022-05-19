package com.example.work.Adapter

import android.app.AlertDialog
import android.content.Context
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

    private fun deleteCommentDialog(model: ModelComment, holder: AdapterComment.HolderComment) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Comment")
            .setMessage("Are you sure?")
            .setPositiveButton("Delete"){ d,e->
                val bookId = model.bookId
                val commentId = model.id

                val ref = FirebaseDatabase.getInstance().getReference("Book")
                ref.child(model.bookId).child("comments").child(commentId)
                    .removeValue()
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener{
                        Toast.makeText(context,"Failed to delete comment", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancle"){d,e->
                d.dismiss()
            }
    }

    private fun loadUserDetails(model: ModelComment, holder: AdapterComment.HolderComment)
    {
        val uid = model.uid
        val ref = FirebaseDatabase.getInstance().getReference("Users")
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
                            .placeholder(R.drawable.ic_baseline_person_24)
                            .into(holder.profileIv)
                    }
                    catch (e: Exception){
                        //if there is no image
                        holder.profileIv.setImageResource(R.drawable.ic_baseline_person_24)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
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