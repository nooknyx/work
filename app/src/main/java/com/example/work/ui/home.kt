package com.example.work.ui
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.work.Adapter.BookAdapter
import com.example.work.Booklist
import com.example.work.R
import com.example.work.MainActivity
import com.example.work.Model.ModelCategory
import com.example.work.data.Bookdata
import com.example.work.databinding.ActivityBookdetailBinding
import com.example.work.databinding.BooklistBinding
import com.example.work.databinding.FragmentHomeBinding

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
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var categoryArrayList: ArrayList<ModelCategory>


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
        //popBookRecyclerView = binding.viewPager
        /*popBookRecyclerView.layoutManager = LinearLayoutManager(context)
        popBookRecyclerView.setHasFixedSize(true)
        popBookArrayList = arrayListOf<Bookdata>()*/


        //new book list
        //newBookRecyclerView = binding.newlist
        /*newBookRecyclerView.layoutManager = LinearLayoutManager(context)
        newBookRecyclerView.setHasFixedSize(true)
        newBookArrayList = arrayListOf<Bookdata>()*/

        setupViewPagerAdapter(binding.viewPager)
        binding.tablayout.setupViewPagerAdapter(binding.viewPager)
        return binding.root

    }

    //init list


    private fun setupViewPagerAdapter(viewPager: ViewPager){

        viewPagerAdapter = ViewPagerAdapter(
            childFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, requireContext())

        categoryArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                //load static category
                val modelAll = ModelCategory("01", "All", 1, "")
                val modelMostView = ModelCategory("01", "Most Viewed", 1, "")
                val modelNewBook = ModelCategory("01", "New Books", 1, "")
                val modelTopRating = ModelCategory("01", "Top Rating", 1, "")

                //added to lo list
                categoryArrayList.add(modelAll)
                categoryArrayList.add(modelMostView)
                categoryArrayList.add(modelNewBook)
                categoryArrayList.add(modelTopRating)

                viewPagerAdapter.addFragment(
                    Booklist.newInstance(
                        "${modelAll.id}",
                        "${modelAll.category}",
                        "${modelAll.uid}"
                    ), modelAll.category
                )

                viewPagerAdapter.addFragment(
                    Booklist.newInstance(
                        "${modelMostView.id}",
                        "${modelMostView.category}",
                        "${modelMostView.uid}"
                    ), modelMostView.category
                )

                viewPagerAdapter.addFragment(
                    Booklist.newInstance(
                        "${modelNewBook.id}",
                        "${modelNewBook.category}",
                        "${modelNewBook.uid}"
                    ), modelNewBook.category
                )

                viewPagerAdapter.addFragment(
                    Booklist.newInstance(
                        "${modelTopRating.id}",
                        "${modelTopRating.category}",
                        "${modelTopRating.uid}"
                    ), modelTopRating.category
                )

                //refresh list
                viewPagerAdapter.notifyDataSetChanged()

                //load from firebase db
                for (ds in snapshot.children){

                    //get data in model

                    val model = ds.getValue(ModelCategory::class.java)

                    //add to list
                    categoryArrayList.add(model!!)

                    //add to viewPagerAdapter
                    viewPagerAdapter.addFragment(
                        Booklist.newInstance(
                            "${model.id}",
                            "${model.category}",
                            "${model.uid}"
                        ), model.category
                    )
                    //refresh list
                    viewPagerAdapter.notifyDataSetChanged()
                }

            }
            override fun onCancelled(error: DatabaseError) {

            }

        })


        //set up adapter to viewpage
        viewPager.adapter = viewPagerAdapter

    }

    class ViewPagerAdapter(fragmentManager: FragmentManager, behavior: Int, context: Context): FragmentPagerAdapter(fragmentManager, behavior){

        //hold list of fragment
        private val fragmentList: ArrayList<Booklist> = ArrayList();
        //List of title of category
        private val fragmentTitleList: ArrayList<String> = ArrayList();
        private val context: Context

        init {
            this.context = context
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentTitleList[position]
        }

        public fun addFragment(fragment: Booklist, title: String){

            //add fragment that will passed as a parameter in fragmentList
            fragmentList.add(fragment)
            //add title that will be passed as a parameter
            fragmentTitleList.add(title)
        }

    }

}