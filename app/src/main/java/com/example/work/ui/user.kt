package com.example.work.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.work.MainActivity
import com.example.work.R
import com.example.work.databinding.FragmentUserBinding
import com.example.work.login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class User:Fragment(R.layout.fragment_user)
{
    private lateinit var userName: EditText
    private lateinit var userEmail: EditText
    private lateinit var binding: FragmentUserBinding

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

        /*binding.logoutBtn.setOnClickListener{
            View.OnClickListener {
                requireActivity().run {

                    startActivity(Intent(context, login::class.java))
                    //return@OnClickListener
                }
            }
        }*/

    }

    private fun showAllUserData() {
        val userRef = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")





    }



}