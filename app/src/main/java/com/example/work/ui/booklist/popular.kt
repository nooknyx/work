package com.example.work.ui.booklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.work.data.Bookdata
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.work.Adapter.BookAdapter
import com.example.work.R
import com.example.work.databinding.ActivityPopularBinding
import com.google.firebase.firestore.*

class popular : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var popularlist : ArrayList<Bookdata>
    private lateinit var popularadapter : BookAdapter
    private lateinit var db : FirebaseFirestore

    //view binding
    private lateinit var binding: ActivityPopularBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopularBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.poppage)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        popularlist = arrayListOf()
        popularadapter = BookAdapter(this, popularlist )
        recyclerView.adapter = popularadapter

        binding.backBtn.setOnClickListener{
            onBackPressed()
        }

        //handle backbutton click, go back
        binding.backBtn.setOnClickListener{
            onBackPressed()
        }

        EventChangeListener()
    }

    private fun EventChangeListener()
    {
        db = FirebaseFirestore.getInstance()
        db.collection("booktest").
        addSnapshotListener(object : EventListener<QuerySnapshot>
        {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?)
            {
                if (error!=null)
                {
                    Log.e("Firestore error", error.message.toString())
                    return
                }

                for (dc : DocumentChange in value?.documentChanges!!)
                {
                    if(dc.type == DocumentChange.Type.ADDED)
                    {
                        popularlist.add(dc.document.toObject(Bookdata::class.java))
                    }
                }
                popularadapter.notifyDataSetChanged()
            }
        }
        )



    }

}