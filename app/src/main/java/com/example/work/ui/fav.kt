package com.example.work.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.work.Adapter.AdapterFavourite
import com.example.work.Adapter.BookAdapter
import com.example.work.Model.ModelCategory
import com.example.work.R
import com.example.work.data.Bookdata
import com.example.work.databinding.FragmentFavBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class Favourite:Fragment(R.layout.fragment_fav)
{
    private lateinit var binding: FragmentFavBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var adapterFavourite: AdapterFavourite

    //arraylist holding books
    private lateinit var booksArrayList: ArrayList<Bookdata>
    private lateinit var favRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentFavBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        if(firebaseAuth.uid == null) {

        } else {
            favRecyclerView = binding.favRv
            favRecyclerView.layoutManager = LinearLayoutManager(context)
            favRecyclerView.setHasFixedSize(true)
            booksArrayList = ArrayList()
            booksArrayList.clear()
            loadFavouriteBooks()
        }



        return binding.root
    }


    private fun loadFavouriteBooks(){

        booksArrayList = ArrayList();

        val ref = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users")
                    ref.child(firebaseAuth.uid!!).child("Favourite")
                .addValueEventListener(object : ValueEventListener{

                    override fun onDataChange(snapshot: DataSnapshot) {
                        //clear list before adding data

                        for (ds in snapshot.children){
                            //get bookid
                            val bookId = "${ds.child("bookId").value}"

                            loadFavbooklist(bookId)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })


    }

    private fun loadFavbooklist(bookId: String){
        booksArrayList = ArrayList()
        val favbookref = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Books").orderByChild("bookId").equalTo(bookId)
        favbookref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (allBookSnapshot in snapshot.children) {
                        val allBook = allBookSnapshot.getValue(Bookdata::class.java)

                        booksArrayList.add(allBook!!)
                    }
                    activity?.let{
                        adapterFavourite = AdapterFavourite(it, booksArrayList)
                        binding.favRv.adapter = adapterFavourite
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }
}