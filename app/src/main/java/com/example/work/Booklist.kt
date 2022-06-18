package com.example.work

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.work.Adapter.BookAdapter
import com.example.work.data.Bookdata
import com.example.work.databinding.BooklistBinding
import com.example.work.databinding.FragmentBooklistBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Booklist() : Fragment() {

    //view binding fragment_booklist.xml => FragmentBooklistBinding
    private lateinit var binding: FragmentBooklistBinding



    companion object{
        private const val TAG = "BOOKS_LIST_TAG"
        //receive data from activity to load book 
        public fun newInstance(categoryId: String, category: String, uid: String): Booklist {
            val fragment = Booklist()
            
            //put data to bundle intent
            val args = Bundle()
            args.putString("categoryId", categoryId)
            args.putString("category", category)
            args.putString("uid", uid)
            fragment.arguments = args
            return fragment
        }
    }

    private var categoryId = ""
    private var category = ""
    private var uid = ""
    
    private lateinit var booklistArrayList: ArrayList<Bookdata>
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        
        //get argument for passing new argument method
        val args = arguments
        if(args != null){
            categoryId = args.getString("categoryId")!!
            category = args.getString("category")!!
            uid = args.getString("uid")!!
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        
        // Inflate the layout for this fragment
        binding = FragmentBooklistBinding.inflate(LayoutInflater.from(context), container, false)


        
        //load data
        Log.d(TAG, "onCreateView: Category: $category")
        
        if(category == "All"){
            //load all book
            loadAllBooks()
        }
        else if (category == "Most Viewed"){
            //load most view book
            loadMostViewedBooks("viewCount")
        }
        else if (category == "Top Rating"){
            //load top rating book
            loadMostRatingBooks("AverageRatings")
        }
        else if (category == "New Books"){
            loadNewBooks("dateAdded")
        }
        else if (category == "Psychology" ){
            loadPsychology("category")
        }

        //
        
        return binding.root
    }

    private fun loadAllBooks() {
        //init list
        booklistArrayList = ArrayList()
        val bRef = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Books")
        bRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (booklistArrayList.count() > 0 )
                {
                    booklistArrayList.clear()
                }
                if (snapshot.exists()) {
                    for (allBookSnapshot in snapshot.children) {
                        val allBook = allBookSnapshot.getValue(Bookdata::class.java)


                        booklistArrayList.add(allBook!!)
                    }
                    activity?.let{
                        adapter = BookAdapter(it, booklistArrayList)
                        binding.booksRv.adapter = adapter
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun loadNewBooks(query: String) {
        booklistArrayList = ArrayList()
        val bRef = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Books")
        bRef.orderByChild("dateAdded").limitToLast(10)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (booklistArrayList.count() > 0 )
                    {
                        booklistArrayList.clear()
                    }
                    if (snapshot.exists()){
                        for (newbookSnapshot in snapshot.children){

                            val newBook = newbookSnapshot.getValue(Bookdata::class.java)


                            booklistArrayList.add(newBook!!)
                        }

                        activity?.let{
                            adapter = BookAdapter(it, booklistArrayList)
                            binding.booksRv.adapter = adapter
                        }


                    }
                    booklistArrayList.reverse()
                }

                override fun onCancelled(error: DatabaseError) {

                }


            })
    }

    private fun loadMostRatingBooks(query: String) {
        booklistArrayList = ArrayList()
        val bRef = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Books")
        bRef.orderByChild("AverageRatings").limitToLast(10)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (booklistArrayList.count() > 0 )
                    {
                        booklistArrayList.clear()
                    }
                    if (snapshot.exists()){
                        for (ratebookSnapshot in snapshot.children){

                            val newBook = ratebookSnapshot.getValue(Bookdata::class.java)


                            booklistArrayList.add(newBook!!)
                        }
                        activity?.let{
                            adapter = BookAdapter(it, booklistArrayList)
                            binding.booksRv.adapter = adapter
                        }

                    }
                    booklistArrayList.reverse()
                }

                override fun onCancelled(error: DatabaseError) {

                }


            })
    }

    private fun loadMostViewedBooks(query: String) {
        booklistArrayList = ArrayList()
        val bRef = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Books")
        bRef.orderByChild("viewCount").limitToLast(10)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (booklistArrayList.count() > 0 )
                    {
                        booklistArrayList.clear()
                    }
                    if (snapshot.exists()){
                        for (popbookSnapshot in snapshot.children){

                            if(popbookSnapshot.value != null)
                            {
                                val popBook = popbookSnapshot.getValue(Bookdata::class.java)
                                booklistArrayList.add(popBook!!)
                            }
                        }
                        //only update ui if the context exist
                        activity?.let{
                            adapter = BookAdapter(it, booklistArrayList)
                            binding.booksRv.adapter = adapter
                        }


                    }
                    booklistArrayList.reverse()
                }

                override fun onCancelled(error: DatabaseError) {
                }


            })
    }

    private fun loadPsychology(query: String) {
        booklistArrayList = ArrayList()
        val bRef = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Books")
        bRef.orderByChild("category").equalTo("Psychology")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (booklistArrayList.count() > 0 )
                    {
                        booklistArrayList.clear()
                    }
                    if (snapshot.exists()){
                        for (psybookSnapshot in snapshot.children){

                            val psyBook = psybookSnapshot.getValue(Bookdata::class.java)


                            booklistArrayList.add(psyBook!!)
                        }
                        activity?.let{
                            adapter = BookAdapter(it, booklistArrayList)
                            binding.booksRv.adapter = adapter
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }


            })

    }

}