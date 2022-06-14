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

        //
        
        return binding.root
    }

    private fun loadNewBooks(query: String) {
        booklistArrayList = ArrayList()
        val bRef = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Books")
        bRef.orderByChild("dateAdded").limitToLast(10)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    booklistArrayList.clear()
                    if (snapshot.exists()){
                        for (newbookSnapshot in snapshot.children){

                            val newBook = newbookSnapshot.getValue(Bookdata::class.java)
                            //val bookimage = "${popbookSnapshot.child("Image").value}"

                            booklistArrayList.add(newBook!!)
                        }
                        adapter = BookAdapter(context!!, booklistArrayList)
                        //binding.newlist.adapter = bookAdapter
                        //popBookRecyclerView.adapter = BookAdapter(popBookArrayList)

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
                    booklistArrayList.clear()
                    if (snapshot.exists()){
                        for (ratebookSnapshot in snapshot.children){


                            val newBook = ratebookSnapshot.getValue(Bookdata::class.java)
                            //val bookimage = "${popbookSnapshot.child("Image").value}"

                            booklistArrayList.add(newBook!!)
                        }
                        adapter = BookAdapter(context!!, booklistArrayList)
                        //binding.newlist.adapter = bookAdapter
                        //popBookRecyclerView.adapter = BookAdapter(popBookArrayList)

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
                    booklistArrayList.clear()
                    if (snapshot.exists()){
                        for (popbookSnapshot in snapshot.children){


                            val popBook = popbookSnapshot.getValue(Bookdata::class.java)
                            //val bookimage = "${popbookSnapshot.child("Image").value}"

                            booklistArrayList.add(popBook!!)
                        }
                        adapter = BookAdapter(context!!, booklistArrayList)

                        //binding.poplist.adapter = bookAdapter
                        //popBookRecyclerView.adapter = BookAdapter(popBookArrayList)

                    }
                    booklistArrayList.reverse()
                }

                override fun onCancelled(error: DatabaseError) {
                }


            })
    }

}