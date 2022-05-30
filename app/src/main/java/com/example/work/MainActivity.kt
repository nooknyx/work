package com.example.work

import android.content.Intent
import android.net.wifi.hotspot2.pps.HomeSp
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.example.work.ui.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.sql.Timestamp
import java.util.*
import kotlin.collections.HashMap
import com.example.work.ui.Search as Search


class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var homefragment : Home
    lateinit var searchfragment : Search
    lateinit var favfragment : Favourite
    lateinit var markfragment : Bookmark
    lateinit var userfragment : User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val homefragment = Home()
        val searchfragment = Search()
        val favfragment = Favourite()
        val markfragment = Bookmark()
        val userfragment = User()

        setCurrentFragment(homefragment)

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.menu_home -> setCurrentFragment(homefragment)
                R.id.menu_search -> setCurrentFragment(searchfragment)
                R.id.menu_fav -> setCurrentFragment(favfragment)
                R.id.menu_mark -> setCurrentFragment(markfragment)
                R.id.menu_user -> setCurrentFragment(userfragment)

            }
            true
        }
    }
    private fun setCurrentFragment(fragment:Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainFragment, fragment)
            commit()

        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object
    {
        fun formatTimeStamp(timestamp: Long) : String{
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = timestamp
            //format in dd/mm/yyyy
            return DateFormat.format("dd/MM/yyyy",cal).toString()
        }
    }




}
