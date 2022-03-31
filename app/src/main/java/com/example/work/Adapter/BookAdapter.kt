package com.example.work.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.work.data.bookdata
import com.example.work.R


class BookAdapter(private val bookdatalist: ArrayList<bookdata> )
        : RecyclerView.Adapter<BookAdapter.MyViewHolder>()

{

    override fun onCreateViewHolder( parent: ViewGroup,viewType: Int ): BookAdapter.MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.booklist, parent,false)

        return MyViewHolder(itemView)
    }

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val name: TextView = itemView.findViewById(R.id.booktitle)
        val author : TextView = itemView.findViewById(R.id.author)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val data: bookdata = bookdatalist[position]
        holder.name.text = data.name
        holder.author.text = data.author

    }

    override fun getItemCount(): Int
    {
        return bookdatalist.size
    }
}