package com.example.work.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.work.MainActivity
import com.example.work.R
import com.example.work.databinding.FragmentUserBinding
import com.example.work.login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class User:Fragment(R.layout.fragment_user)
{
    private lateinit var userName: EditText
    private lateinit var userEmail: EditText

    //view binding
    private lateinit var binding: FragmentUserBinding
    private lateinit var firebaseAuth: FirebaseAuth



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View?
    {
        binding = FragmentUserBinding.inflate(layoutInflater)
        return binding.root

        binding.userUsername
        binding.userProfile
        binding.userEmail
        //showAllUserData();
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.userUsername
        binding.userProfile
        binding.userEmail


        firebaseAuth = FirebaseAuth.getInstance()
        val userid = firebaseAuth.uid
        if (firebaseAuth.currentUser != null) {
            binding.logoutBtn.setOnClickListener {
                firebaseAuth.signOut()
                startActivity(Intent(activity, login::class.java))
                activity?.finish()
            }
        } else if (firebaseAuth.currentUser!!.isAnonymous)
        {
            binding.userUsername.text = "Guest"
            binding.userEmail.text = ""
        }


        binding.userEditbtn.setOnClickListener(){

        }

    }

    private fun loadUserInfo(){

        //db references to load user info
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object : ValueEventListener{
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
                        Glide.with(this@User)
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

    private fun showAllUserData() {
        val userRef = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")

    }

}