package com.example.work.ui
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.fragment.app.Fragment
import com.example.work.Adapter.BookAdapter

import com.example.work.data.Bookdata
import com.example.work.databinding.FragmentSearchBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class Search: Fragment()
{
    private lateinit var binding: FragmentSearchBinding//view binding
    private lateinit var bookdatalist: ArrayList<Bookdata>
    private lateinit var bookAdapter: BookAdapter
    private lateinit var auth: FirebaseAuth//firebase auth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        super.onCreate(savedInstanceState)

        //Inflate thje layout for this fragment
        binding = FragmentSearchBinding.inflate(LayoutInflater.from(context), container, false)

        auth = FirebaseAuth.getInstance()
        loadAllBooks()



        binding.editTextTextPersonName.addTextChangedListener(object :TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {//called when user type something
                try {
                    if (binding.editTextTextPersonName.length() == 0) {
                        binding.booksRv.visibility = View.GONE
                    } else {
                        binding.booksRv.visibility = View.VISIBLE
                        bookAdapter.filter.filter(s)
                    }

                }
                catch (e: Exception){
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        if (binding.editTextTextPersonName.length() == 0) {
            binding.booksRv.visibility = View.GONE
        }

        //handle click, scan
        /*binding.searchScanbtn.setOnClickListener{
            View.OnClickListener {
                requireActivity().run {
                    startActivity(Intent(context, ScanActivity::class.java))
                    return@OnClickListener
                }
            }
        }*/

        return binding.root

    }

    private fun loadAllBooks(){
        //init list
        bookdatalist = ArrayList()
        val ref = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Books")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                bookdatalist.clear()//clear list before starting adding data into it
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
}