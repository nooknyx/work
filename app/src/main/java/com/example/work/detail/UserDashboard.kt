package com.example.work.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.bumptech.glide.Glide
import com.example.work.Adapter.AdapterComment
import com.example.work.Model.ModelComment
import com.example.work.R
import com.example.work.databinding.ActivityUserDashboardBinding
import com.example.work.databinding.FragmentUserBinding
import com.example.work.login
import com.example.work.username
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserDashboard : AppCompatActivity() {

    private lateinit var userName: EditText
    private lateinit var userEmail: EditText

    //view binding
    private lateinit var binding: ActivityUserDashboardBinding
    private lateinit var firebaseAuth: FirebaseAuth

    //arraylist holding comment
    private lateinit var commentArrayList: ArrayList<ModelComment>

    //adapter to set comment into recycleview
    private lateinit var adapterComment: AdapterComment

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityUserDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set back btn
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.userUsername
        binding.userProfile
        binding.userEmail

        firebaseAuth = FirebaseAuth.getInstance()
        val userid = firebaseAuth.uid
        loadUserInfo(userid.toString())

        //click to logout
        if (firebaseAuth.currentUser != null) {
            binding.logoutBtn.setOnClickListener {
                firebaseAuth.signOut()
                val intent = Intent(this, login::class.java)
                startActivity(intent)
            }
        }

        else if (firebaseAuth.currentUser!!.isAnonymous)
        {
            binding.userUsername.text = "Guest"
            binding.userEmail.text = ""
        }

        showuserComments()

        //edit button setting (click to go to edit page)
        binding.userEditbtn.setOnClickListener()
        {
            val intent = Intent(this, EditUser::class.java)
            startActivity(intent)
        }

    }

    private fun loadUserInfo(userid: String){

        //db references to load user info
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(userid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user data
                    val email = "${snapshot.child("email").value}"
                    val username = "${snapshot.child("username").value}"
                    val profileImage = "${snapshot.child("profileImage").value}"
                    val userType = "${snapshot.child("userType").value}"

                    //set data
                    binding.userUsername.text = username
                    binding.userEmail.text = email

                    //set image
                    try {
                        Glide.with(this@UserDashboard)
                            .load(profileImage)
                            .placeholder(R.drawable.user_person)
                            .into(binding.userProfile)
                    }
                    catch (e: Exception){

                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    private fun showuserComments() {
        //init arraylist
        commentArrayList = ArrayList()

        //path to db, loading comment
        val ref = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")
        ref.child(firebaseAuth.uid!!).child("Comments")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    //clear list
                    commentArrayList.clear()
                    for (ds in snapshot.children){
                        //get data ss model
                        val model = ds.getValue(ModelComment::class.java)
                        //add to list
                        commentArrayList.add(model!!)
                    }
                    activity?.let{
                        //setup adapter
                        adapterComment = AdapterComment(context!!, commentArrayList)
                        //set adapter to recycleview
                        binding.userComment.adapter = adapterComment
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}