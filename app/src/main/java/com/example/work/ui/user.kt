package com.example.work.ui
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.work.R
import com.example.work.databinding.FragmentUserBinding
import com.google.android.material.textfield.TextInputLayout

class User:Fragment(R.layout.fragment_user)
{

    private lateinit var binding: FragmentUserBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View?
    {
        binding = FragmentUserBinding.inflate(layoutInflater)
        return binding.root
        //showAllUserData();


    }

    private fun showAllUserData() {
        /*Intent intent = getIntent();
        String user_username = intent.GetStringExtra("username")
        String user_email = intent.GetStringExtra("Email")*/
    }


}