package com.example.work.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.work.Adapter.AdapterFavourite
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    booksArrayList.clear()

                    for (ds in snapshot.children){
                        //get bookid
                        val bookId = "${ds.child("bookId").value}"

                        //set model
                        val model = ModelCategory()
                        model.id = bookId

                        //add model to list
                        booksArrayList.add(model)

                    }

                    //setup adapter
                    adapterFavourite = AdapterFavourite(this@Favourite, booksArrayList)

                    //adapter for recycleview
                    binding.favRv.adapter = adapterFavourite

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }
}