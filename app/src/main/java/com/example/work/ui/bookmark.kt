package com.example.work.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.work.Adapter.AdapterComment
import com.example.work.Adapter.AdapterFavourite
import com.example.work.Model.ModelComment
import com.example.work.R
import com.example.work.data.Bookdata
import com.example.work.databinding.FragmentBookmarkBinding
import com.example.work.databinding.FragmentFavBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Bookmark : Fragment(R.layout.fragment_bookmark) {
    private lateinit var binding: FragmentBookmarkBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var adapterBookmarkComment: AdapterComment

    //arraylist holding books
    private lateinit var commentArrayList: ArrayList<ModelComment>
    private lateinit var bookmarkRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentBookmarkBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()

        bookmarkRecyclerView = binding.commentRv
        bookmarkRecyclerView.layoutManager = LinearLayoutManager(context)
        bookmarkRecyclerView.setHasFixedSize(true)
        commentArrayList = ArrayList()
        loadBookmark()

        return binding.root
    }

    private fun loadBookmark(){

        commentArrayList = ArrayList();

        val ref = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")
        ref.child(firebaseAuth.uid!!).child("Bookmark")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    //clear list before adding data

                    for (ds in snapshot.children){
                        //get bookid
                        val allComment = ds.getValue(ModelComment::class.java)
                        commentArrayList.add(allComment!!)

                    }
                    
                    activity?.let{
                        adapterBookmarkComment = AdapterComment(it, commentArrayList)
                        binding.commentRv.adapter = adapterBookmarkComment
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


    }
/*
    private fun loadBookmark(){

        commentArrayList = ArrayList();

        val ref = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")
        ref.child(firebaseAuth.uid!!).child("Bookmark")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    //clear list before adding data

                    for (ds in snapshot.children){
                        //get bookid
                        val commentId = "${ds.child("commentid").value}"

                        loadBookmarklist(commentId)
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


    }

    private fun loadBookmarklist(commentId: String){
        commentArrayList = ArrayList()
        val favbookref = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Books")
        favbookref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (commentArrayList.count() > 0 )
                {
                    commentArrayList.clear()
                }
                if (snapshot.exists()) {
                    for (allBookSnapshot in snapshot.children) {

                        if(allBookSnapshot.child("Comments").hasChild(commentId))
                        {
                            val allComment = allBookSnapshot.getValue(ModelComment::class.java)
                            commentArrayList.add(allComment!!)
                        }
                        /*val allComment = allBookSnapshot.getValue(ModelComment::class.java)
                        commentArrayList.add(allComment!!)*/

                    }
                    activity?.let{
                        adapterBookmarkComment = AdapterComment(it, commentArrayList)
                        binding.commentRv.adapter = adapterBookmarkComment
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }

    */
}