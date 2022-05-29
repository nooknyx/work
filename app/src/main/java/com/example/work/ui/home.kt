package com.example.work.ui
import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentHostCallback
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

class Home:Fragment(R.layout.fragment_home)
{

    private lateinit var binding: FragmentHomeBinding
    //private var db = Firebase.firestore
    //private lateinit var dbref : DatabaseReference
    private lateinit var popBookRecyclerView: RecyclerView
    private lateinit var popBookArrayList : ArrayList<Bookdata>



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)
        getpopBookData()
        popBookRecyclerView = binding.poplist
        popBookRecyclerView.layoutManager = LinearLayoutManager(context)
        popBookRecyclerView.setHasFixedSize(true)

        popBookArrayList = arrayListOf<Bookdata>()
        getpopBookData()

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


        /*val poplist = findViewById<RecyclerView>(R.id.poplist)
        val newlist = findViewById<RecyclerView>(R.id.newlist)
        val myRef = FirebaseDatabase.getInstance().getReference("Books")

        popBookRecyclerView = view.findViewById(R.id.poplist)
        popBookRecyclerView.layoutManager = LinearLayoutManager(context)
        popBookRecyclerView.setHasFixedSize(true)

        popBookArrayList = arrayListOf<Bookdata>()
        getpopBookData()*/

    }

    private fun getpopBookData() {
        val bRef = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Books")

        bRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (popbookSnapshot in snapshot.children){


                        val popBook = popbookSnapshot.getValue(Bookdata::class.java)
                        popBookArrayList.add(popBook!!)
                    }

                    popBookRecyclerView.adapter = BookAdapter(popBookArrayList)

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }

    private fun findViewById(){

    }


}