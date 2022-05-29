package com.example.work.ui
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.work.Adapter.BookAdapter
import com.example.work.R
import com.example.work.MainActivity
import com.example.work.data.Bookdata
import com.example.work.databinding.ActivityBookdetailBinding
import com.example.work.databinding.FragmentHomeBinding
import com.example.work.ui.booklist.popular
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class Home:Fragment(R.layout.fragment_home)
{

    private lateinit var binding: FragmentHomeBinding
    //private var db = Firebase.firestore
    //recyclerview for popular books
    private lateinit var popBookRecyclerView: RecyclerView
    private lateinit var popBookArrayList : ArrayList<Bookdata>
    private lateinit var bookAdapter: BookAdapter

    //recyclerview for new books
    private lateinit var newBookRecyclerView: RecyclerView
    private lateinit var newBookArrayList : ArrayList<Bookdata>

    //private var category = ""
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null){
            category = args.getString("category")!!
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)
        //popular book list
        popBookRecyclerView = binding.poplist
        popBookRecyclerView.layoutManager = LinearLayoutManager(context)
        popBookRecyclerView.setHasFixedSize(true)
        popBookArrayList = arrayListOf<Bookdata>()
        getpopBookData()
        //new book list
        newBookRecyclerView = binding.newlist
        newBookRecyclerView.layoutManager = LinearLayoutManager(context)
        newBookRecyclerView.setHasFixedSize(true)
        newBookArrayList = arrayListOf<Bookdata>()
        getnewBookData()

        return binding.root

/*
        val popimage = view.findViewById<ImageView>(R.id.imageView3)
        val newimage = view.findViewById<ImageView>(R.id.imageView4)
        popimage.visibility = View.VISIBLE
        newimage.visibility = View.VISIBLE

 */

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.poplist.setOnClickListener(
            View.OnClickListener {
                requireActivity().run {
                    startActivity(Intent(this, ActivityBookdetailBinding::class.java))
                    return@OnClickListener
                }
            }
        )

        binding.newlist.setOnClickListener(
            View.OnClickListener {
                requireActivity().run {
                    startActivity(Intent(this, ActivityBookdetailBinding::class.java))
                    return@OnClickListener
                }
            }
        )

        binding.homeseeall.setOnClickListener(
            View.OnClickListener {
                requireActivity().run {
                    startActivity(Intent(this, popular::class.java))
                    return@OnClickListener
                }
            }
        )

        binding.newseeall.setOnClickListener(
            View.OnClickListener {
                requireActivity().run {
                    startActivity(Intent(this, popular::class.java))
                    return@OnClickListener
                }
            }
        )

        /*
        val poplist = findViewById<RecyclerView>(R.id.poplist)
        val newlist = findViewById<RecyclerView>(R.id.newlist)
        val myRef = FirebaseDatabase.getInstance().getReference("Books")

        popBookRecyclerView = view.findViewById(R.id.poplist)
        popBookRecyclerView.layoutManager = LinearLayoutManager(context)
        popBookRecyclerView.setHasFixedSize(true)

        popBookArrayList = arrayListOf<Bookdata>()
        getpopBookData()*/


    }

    private fun getpopBookData() {

        popBookArrayList = ArrayList()
        val bRef = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Books")
        bRef.orderByChild("viewCount").limitToLast(10)
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                popBookArrayList.clear()
                if (snapshot.exists()){
                    for (popbookSnapshot in snapshot.children){


                        val popBook = popbookSnapshot.getValue(Bookdata::class.java)
                        //val bookimage = "${popbookSnapshot.child("Image").value}"

                        popBookArrayList.add(popBook!!)
                    }
                    bookAdapter = BookAdapter(context!!, popBookArrayList)
                    binding.poplist.adapter = bookAdapter
                    //popBookRecyclerView.adapter = BookAdapter(popBookArrayList)

                }
                popBookArrayList.reverse()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }


    private fun getnewBookData() {

        newBookArrayList = ArrayList()
        val bRef = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Books")
        bRef.orderByChild("dateAdded").limitToLast(10)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    newBookArrayList.clear()
                    if (snapshot.exists()){
                        for (newbookSnapshot in snapshot.children){


                            val newBook = newbookSnapshot.getValue(Bookdata::class.java)
                            //val bookimage = "${popbookSnapshot.child("Image").value}"

                            newBookArrayList.add(newBook!!)
                        }
                        bookAdapter = BookAdapter(context!!, newBookArrayList)
                        binding.newlist.adapter = bookAdapter
                        //popBookRecyclerView.adapter = BookAdapter(popBookArrayList)

                    }
                    newBookArrayList.reverse()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })
    }


}