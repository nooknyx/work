package com.example.work.Adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.work.MainActivity
import com.example.work.data.Bookdata
import com.example.work.databinding.BooklistBinding
import com.example.work.detail.bookdetail
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AdapterFavourite: RecyclerView.Adapter<AdapterFavourite.HolderBookFavourite> {

    //context
    private val context: Context

    //Arraylist for holding book
    private var bookArrayList: ArrayList<Bookdata>

    private lateinit var binding: BooklistBinding

    //constructor
    constructor(context: Context, bookArrayList: ArrayList<Bookdata>){

        this.context = context
        this.bookArrayList = bookArrayList
    }


    //view holder for managing UI view of booklist.xml

    inner class HolderBookFavourite(itemView: View) : RecyclerView.ViewHolder(itemView){

        var bookcover = binding.bookcover
        var booktitle = binding.booktitle
        var author = binding.author
        var viewCount = binding.viewcount
        var avgRating = binding.booklistRating

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderBookFavourite {
        binding = BooklistBinding.inflate(LayoutInflater.from(context),parent,false)

        return HolderBookFavourite(binding.root)
    }

    override fun onBindViewHolder(holder: HolderBookFavourite, position: Int) {
        //get data User > uid > Favourite
        val model = bookArrayList[position]

        loadBookDetails(model, holder)

        //handler click, open bookdetail page
        holder.itemView.setOnClickListener {
            val intent = Intent(context, bookdetail::class.java)
            intent.putExtra("bookId", model.bookId)
            context.startActivity(intent)
        }
    }

    private fun loadBookDetails(model: Bookdata, holder: AdapterFavourite.HolderBookFavourite) {
        val bookId = model.bookId

        val ref = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Books")
        ref.child(bookId!!)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get book data

                    val author = "${snapshot.child("Author").value}"
                    val booktitle = "${snapshot.child("BookTitle").value}"
                    val bookcover = "${snapshot.child("Image").value}"
                    val viewCount = "${snapshot.child("viewCount").value}"
                    val uid = "${snapshot.child("uid").value}"
                    val dateAdded = "${snapshot.child("dateAdded").value}".toLong()
                    val avgRatings = "${snapshot.child("AverageRatings").value}".toFloat()

                    //set data to model
                    model.isFavourite = true
                    model.BookTitle = booktitle
                    model.Author = author
                    model.dateAdded = dateAdded
                    model.AverageRatings = avgRatings.toDouble()
                    model.viewCount = viewCount.toLong()
                    model.Image = bookcover

                    //set holder
                    holder.booktitle.text = booktitle
                    holder.author.text = author
                    holder.avgRating.rating = avgRatings.toFloat()
                    holder.viewCount.text = viewCount
                    Glide.with(context).load(model.Image.toString()).into(holder.bookcover)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    override fun getItemCount(): Int {
        return bookArrayList.size
    }


}