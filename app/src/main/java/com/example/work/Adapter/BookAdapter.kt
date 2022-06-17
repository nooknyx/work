package com.example.work.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.work.FilterSearch
import com.example.work.MainActivity
import com.example.work.data.Bookdata
import com.example.work.R
import com.example.work.databinding.BooklistBinding
import com.example.work.detail.bookdetail
import io.grpc.InternalChannelz.id


class BookAdapter : RecyclerView.Adapter<BookAdapter.MyViewHolder>, Filterable
{
    public var bookdatalist: ArrayList<Bookdata> //to access in filter class, make it public
    private var filterList: ArrayList<Bookdata>// to hold filtered
    private var context: Context
    private lateinit var binding: BooklistBinding //binding the row of book

    private var filter: FilterSearch? = null

    constructor(context: Context, bookdatalist: ArrayList<Bookdata>){
        this.context = context
        this.bookdatalist = bookdatalist
        this.filterList = bookdatalist
    }
    override fun onCreateViewHolder( parent: ViewGroup,viewType: Int ): BookAdapter.MyViewHolder
    {
        binding = BooklistBinding.inflate(LayoutInflater.from(context), parent, false)//call booklist.xml
        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        //get data
        val data: Bookdata = bookdatalist[position]

        //set data
        holder.BookTitle.text = data.BookTitle
        holder.Author.text = data.Author
        Glide.with(context).load(data.Image.toString()).into(holder.imagebook)
        //val bookId = data.bookId
        holder.bookRating.rating = data.AverageRatings!!.toFloat()
        holder.viewCount.text = data.viewCount.toString()

        //handle click
        holder.itemView.setOnClickListener {
            val intent = Intent(context, bookdetail::class.java)
            intent.putExtra("bookId", data.bookId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int
    {
        return bookdatalist.size //return list size/ number of records
    }

    override fun getFilter(): Filter {

        if (filter == null){
            filter = FilterSearch(filterList, this)
        }
        return filter as FilterSearch
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)//for row from booklist.xml
    {
        var BookTitle = binding.booktitle
        var Author = binding.author
        var viewCount = binding.viewcount
        var imagebook = binding.bookcover
        var bookRating = binding.booklistRating
    }
}