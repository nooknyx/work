package com.example.work.ui
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.work.Adapter.BookAdapter
import com.example.work.R
import com.example.work.data.Bookdata
import com.example.work.databinding.FragmentSearchBinding
import com.example.work.databinding.SearchBookBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class Search: Fragment
{

    //view binding
    private lateinit var binding: FragmentSearchBinding


    public companion object{
        private const val  TAG = "BOOKS_TAG"

        //receive data from activity to load books e.g bookId
        public fun newInstance(bookId: String, author: String, name: String): Search{
            val fragment = Search()
            //put data to bundle
            val args = Bundle()
            args.putString("bookId",bookId)
            args.putString("author",author)
            args.putString("name",name)
            fragment.arguments = args
            return fragment
        }
    }

    private var bookId = ""
    private var author = ""
    private var name = ""

    private lateinit var bookdatalist: ArrayList<Bookdata>
    private lateinit var bookAdapter: BookAdapter

    constructor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //get arguments that we passed int newInstance method
        val args = arguments
        if (args != null){
            bookId = args.getString("bookId")!!
            author = args.getString("author")!!
            name = args.getString("name")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        //Inflate thje layout for this fragment
        binding = FragmentSearchBinding.inflate(LayoutInflater.from(context), container, false)

        // search
        binding.editTextTextPersonName.addTextChangedListener{ object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    bookAdapter.filter.filter(s)
                }
                catch (e: Exception){
                    Log.d(TAG, "onTextChanged: SEARCH EXCEPTION: ${e.message}")
                }
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }
        }}

        return binding.root

    }


    private fun loadIdBooks(){
        //init list
        bookdatalist = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.orderByChild("bookId").equalTo(bookId)
            .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear list before starting adding data into it
                bookdatalist.clear()
                for (ds in snapshot.children){
                    //get data
                    val model = ds.getValue(Bookdata::class.java)
                    //add to list
                    bookdatalist.add(model!!)
                }
                //setup adapter
                bookAdapter = BookAdapter(context!!, bookdatalist)
                //set adapter to recyclerview
                binding.booksRv.adapter = bookAdapter
            }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun loadAllBooks(){
        //init list
        bookdatalist = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear list before starting adding data into it
                bookdatalist.clear()
                for (ds in snapshot.children){
                    //get data
                    val model = ds.getValue(Bookdata::class.java)
                    //add to list
                    bookdatalist.add(model!!)
                }
                //setup adapter
                bookAdapter = BookAdapter(context!!, bookdatalist)
                //set adapter to recyclerview
                binding.booksRv.adapter = bookAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    /*private fun searchbookpopup(){

    //inflate/ bind view for searched book dialog (search_book.xml)

        val searchBinding = SearchBookBinding.inflate(LayoutInflater.from(this))

        val builder = AlertDialog.Builder(this,R.style.CustomDialog)
        builder.setView(searchBinding.root)

        val alertDialog = builder.create()
        alertDialog.show()

        searchBinding.searchBackbtn.setOnClickListener{
            alertDialog.dismiss()
        }
    }*/
}