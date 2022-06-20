package com.example.work.ui
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.work.Adapter.BookAdapter
import com.example.work.R

import com.example.work.data.Bookdata
import com.example.work.databinding.FragmentSearchBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.integration.android.IntentIntegrator
import java.lang.Exception

class Search: Fragment(),View.OnClickListener
{
    private lateinit var binding: FragmentSearchBinding//view binding
    private lateinit var bookdatalist: ArrayList<Bookdata>
    private lateinit var bookAdapter: BookAdapter
    private lateinit var auth: FirebaseAuth//firebase auth
    private lateinit var intentIntegrator: IntentIntegrator

/*
    override fun onResume() {
        super.onResume()

        //set dropdown menu
        /*val category = resources.getStringArray(R.array.category)
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdpwn_item,category)
        binding.categorymenu.setAdapter(arrayAdapter)*/

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        super.onCreate(savedInstanceState)

        //Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(LayoutInflater.from(context), container, false)

        auth = FirebaseAuth.getInstance()

        val categoryArrayList = arrayListOf("Select Category","All","History","Philosophy","Psychology")
        val categoryAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,categoryArrayList)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.categorymenu.adapter = categoryAdapter
        binding.categorymenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(binding.categorymenu.selectedItemPosition == 0) {
                    binding.booksRv.visibility = View.GONE
                    binding.booksRv.adapter = null
                } else {
                    binding.booksRv.visibility = View.VISIBLE
                }
                if(binding.categorymenu.selectedItemPosition == 1) {
                    binding.booksRv.adapter = null
                    loadAllBooks()
                }
                if(binding.categorymenu.selectedItemPosition == 2) {
                    //binding.booksRv.visibility = View.VISIBLE
                    binding.booksRv.adapter = null
                    loadBooksCategory("History")
                }
                if(binding.categorymenu.selectedItemPosition == 3) {
                    //binding.booksRv.visibility = View.VISIBLE
                    binding.booksRv.adapter = null
                    loadBooksCategory("Philosophy")
                }
                if(binding.categorymenu.selectedItemPosition == 4) {
                    //binding.booksRv.visibility = View.VISIBLE
                    binding.booksRv.adapter = null
                    loadBooksCategory("Psychology")
                }
            }

        }


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
        binding.searchScanbtn.setOnClickListener{
            binding.booksRv.adapter = null
            loadAllBooks()
            intentIntegrator.setBeepEnabled(true).initiateScan()//when it detected, the device will sound beep.
        }

        intentIntegrator = IntentIntegrator.forSupportFragment(this@Search)
        return binding.root

    }

    private fun loadBooksCategory(Category: String) {
        bookdatalist = ArrayList()
        val bRef = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Books")
        bRef.orderByChild("category").equalTo(Category)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                        bookdatalist.clear()
                    if (snapshot.exists()){
                        for (categorybookSnapshot in snapshot.children){

                            val categoryBook = categorybookSnapshot.getValue(Bookdata::class.java)

                            bookdatalist.add(categoryBook!!)
                        }
                        activity?.let{
                            bookAdapter = BookAdapter(it, bookdatalist)
                            binding.booksRv.adapter = bookAdapter
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }


            })
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

                activity?.let{
                    //setup adapter
                    bookAdapter = BookAdapter(it, bookdatalist)
                    //set adapter to recyclerview
                    binding.booksRv.adapter = bookAdapter
                }


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.search_scanbtn -> {
                intentIntegrator.setBeepEnabled(true).initiateScan()//when it detected, the device will sound beep.
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if(intentResult!= null){
            if(intentResult.contents != null){
                binding.editTextTextPersonName.setText(intentResult.contents) //return value that QR read to
            }
            else{
                Toast.makeText(context, "Barcode Not Found",Toast.LENGTH_SHORT).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
        //pull back to search fragment to prevent sending to Main page
    }
}
