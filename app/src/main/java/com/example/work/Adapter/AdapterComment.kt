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
import com.example.work.Listener
import com.example.work.MainActivity
import com.example.work.Model.Comments
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

class AdapterComment : RecyclerView.Adapter<AdapterComment.HolderComment> {

    //context
    val context: Context
    private var listener: Listener? = null

    //arraylist for hold comment
    var commentArrayList: ArrayList<Comments>

    //view binding for row_comment => Rowcommentbinding
    private lateinit var binding: RowCommentBinding

    private lateinit var firebaseAuth: FirebaseAuth


    //hold boolean for favourtie value
    private var isInMyBookmark = false

    //constructor
    constructor(_listener: Listener, context: Context, commentArrayList: ArrayList<Comments>) {
        this.listener = _listener
        this.context = context
        this.commentArrayList = commentArrayList

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderComment {
        //binding / inflate row_comment

        binding = RowCommentBinding.inflate(LayoutInflater.from(context), parent, false)

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
        val date = MainActivity.formatTimeStamp(timestamp.toLong())
        firebaseAuth = FirebaseAuth.getInstance()

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

        loadUserDetails(model, holder)

        holder.itemView.setOnClickListener {
            if (firebaseAuth.currentUser != null && firebaseAuth.uid == uid) {
                deleteCommentDialog(model, holder)

            }
        }

        commentArrayList[position].isBookmark?.let {
            binding.bookmarkbtn
                .setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    if (it) R.drawable.bookmarkbtn_yellow else R.drawable.bookmarkbtn,
                    0,
                    0
                )
        }

        binding.bookmarkbtn.setOnClickListener() {
            if (commentArrayList[position].isBookmark == true) {
                removeFromBookmark(commentId)
            } else {
                addToBookmark(model)
            }
        }

    }

    private fun addToBookmark(model: Comments) {
        this.listener?.onLoading(true)
        val timestamp = "${System.currentTimeMillis()}"

        //set up data to add in db
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = model.id
        hashMap["bookId"] = model.bookId
        hashMap["timestamp"] = "$timestamp"
        hashMap["comment"] = model.comment
        hashMap["uid"] = model.uid
        hashMap["userRating"] = "${model.userRating}".toDouble()

        //save to db
        val ref =
            FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users")
        ref.child(firebaseAuth.uid!!).child("Bookmark").child(model.id)
            .setValue(hashMap)
            .addOnSuccessListener {
                //add to fav
                this.listener?.onLoading(false)
                Log.d(bookdetail.TAG, "addToFavourite: Added to bookmark")
            }
            .addOnFailureListener {
                //failed to add
                this.listener?.onLoading(false)
                Log.d(bookdetail.TAG, "addToFavourite: Failed to add to bookmark")
                Toast.makeText(context, "Failed to add to bookmark", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeFromBookmark(commentId: String) {
        this.listener?.onLoading(true)
        Log.d(bookdetail.TAG, "removeFromBookmark: Removing from bookmark")

        //database ref
        val ref =
            FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users")
        ref.child(firebaseAuth.uid!!).child("Bookmark").child(commentId)
            .removeValue()
            .addOnSuccessListener {
                Log.d(bookdetail.TAG, "Removed from Bookmark")
                this.listener?.onLoading(false)
            }
            .addOnFailureListener { e ->
                this.listener?.onLoading(false)
                Log.d(bookdetail.TAG, "removeFromBookmark: Failed to remove")
                Toast.makeText(context, "Failed to remove from Bookmark", Toast.LENGTH_LONG).show()
            }
    }


    //hold the comment to delete
    private fun deleteCommentDialog(model: Comments, holder: AdapterComment.HolderComment) {

        //alert dialog
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Comment")
            .setMessage("Are you sure?")
            .setPositiveButton("Delete") { d, e ->

                val bookId = model.bookId
                val commentId = model.id


                val bref = FirebaseDatabase
                    .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("users")

                bref.addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (ds in snapshot.children) {
                            if (ds.child("Bookmark").hasChild(commentId)) {
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
                        Toast.makeText(context, "Comment is deleted", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        // failed to delete
                        Toast.makeText(context, "Failed to delete comment", Toast.LENGTH_SHORT)
                            .show()
                    }

                val uref = FirebaseDatabase
                    .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("users")

                uref.child(firebaseAuth.uid!!).child("Comments").child(commentId)
                    .removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Comment is deleted", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        // failed to delete
                        Toast.makeText(context, "Failed to delete comment", Toast.LENGTH_SHORT)
                            .show()
                    }

                //uref.child("Bookmark").child(commentId).removeValue()
                totalRatings(model.bookId)
                totalUserGiveRate(model.bookId)


            }
            .setNegativeButton("Cancel") { d, e ->
                d.dismiss()
            }
            .show()


    }


    private fun totalRatings(bookId: String) {
        var total = 0.0
        var count = 0
        var avgRatings = 0.0

        //path to db, get ratings

        val ref =
            FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Books")
        ref.child(bookId).child("Comments")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    for (ds in snapshot.children) {
                        val avgrate = ds.child("userRating").value.toString()
                        total = total.plus(avgrate.toDouble())
                        count = count.plus(1)
                        avgRatings = total.div(count)

                    }
                    val ref =
                        FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference("Books")

                    ref.child(bookId).child("AverageRatings").setValue(avgRatings)

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun totalUserGiveRate(bookId: String) {
        var total = 0
        var count = 0
        var allUserRateCount = 0

        //path to db, get ratings

        val ref = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Books")

        ref.child(bookId)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val totalUserGiveRate = "${snapshot.child("numUserRated").value}".toInt()
                    total = total.plus(totalUserGiveRate)
                    allUserRateCount = total.minus(1)

                    val ref =
                        FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference("Books")
                    ref.child(bookId).child("numUserRated").setValue(allUserRateCount.toLong())

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun loadUserDetails(model: Comments, holder: AdapterComment.HolderComment) {
        val uid = model.uid
        val ref = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")

        ref.child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //getting profile img and name

                    val name = "" + snapshot.child("username").value
                    val profileImage = "${snapshot.child("profileImage").value}"

                    //setting data
                    holder.nameTv.text = name

                    if(profileImage == "") {
                        holder.profileIv.setImageResource(R.drawable.ic_baseline_person_24)
                    } else {
                        Glide.with(context)
                            .load(profileImage)
                            //.placeholder(R.drawable.ic_baseline_person_24)
                            .into(holder.profileIv)
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    override fun getItemCount(): Int {
        return commentArrayList.size
    }

    inner class HolderComment(itemView: CardView) : RecyclerView.ViewHolder(itemView) {

        val profileIv: ImageView = binding.profileIv
        val nameTv: TextView = binding.nameTv
        val dateTv: TextView = binding.dateTv
        val commentTv: TextView = binding.commentTv
    }
}