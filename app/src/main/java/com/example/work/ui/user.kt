package com.example.work.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.work.R
import com.example.work.databinding.FragmentUserBinding

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
    }
}