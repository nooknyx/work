package com.example.work.Adapter

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
    public var filterList: ArrayList<Bookdata>
    private var context: Context
    private lateinit var binding: BooklistBinding

    private var filter: FilterSearch? = null

    constructor(context: Context, bookdatalist: ArrayList<Bookdata>){
        this.context = context
        this.bookdatalist = bookdatalist
        this.filterList = bookdatalist
    }
    override fun onCreateViewHolder( parent: ViewGroup,viewType: Int ): BookAdapter.MyViewHolder
    {
        //val itemView = LayoutInflater.from(parent.context).inflate(R.layout.booklist, parent,false)
        binding = BooklistBinding.inflate(LayoutInflater.from(context), parent, false)
        //return MyViewHolder(itemView)
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
        holder.BookTitle.text = data.BookTitle
        holder.Author.text = data.Author
        //val bookimageuri = data.Image.toString()
        //holder.imagebook.setImageURI(bookimageuri.toUri())
        //val bookId = data.bookId
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

}