package com.example.work.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.work.data.Bookdata
import com.example.work.R
import com.example.work.databinding.BooklistBinding
import com.example.work.detail.bookdetail
import io.grpc.InternalChannelz.id


class BookAdapter : RecyclerView.Adapter<BookAdapter.MyViewHolder>
{
    private val bookdatalist: ArrayList<Bookdata>
    private var context: Context
    private lateinit var binding: BooklistBinding

    constructor(context: Context, bookdatalist: ArrayList<Bookdata>){
        this.context = context
        this.bookdatalist = bookdatalist
    }
    override fun onCreateViewHolder( parent: ViewGroup,viewType: Int ): BookAdapter.MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.booklist, parent,false)
        binding = BooklistBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(itemView)
        return MyViewHolder(binding.root)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val BookTitle = binding.booktitle
        val Author = binding.author
        val imagebook = binding.bookcover

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        //get data

        val data: Bookdata = bookdatalist[position]
        holder.BookTitle.text = data.name
        holder.Author.text = data.author
        val bookimageuri = data.imagebook
        holder.imagebook.setImageURI(bookimageuri)
        val bookId = data.bookId

        //handle click
        holder.itemView.setOnClickListener {
            val intent = Intent(context, bookdetail::class.java)
            intent.putExtra("Books", bookId)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int
    {
        return bookdatalist.size //return list size/ number of records
    }
}