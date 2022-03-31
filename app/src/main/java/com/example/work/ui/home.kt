package com.example.work.ui
import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentHostCallback
import com.example.work.R
import com.example.work.MainActivity
import com.example.work.databinding.FragmentHomeBinding
import com.example.work.ui.booklist.popular

class Home:Fragment(R.layout.fragment_home)
{
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeseeall.setOnClickListener(
            View.OnClickListener {
                requireActivity().run {
                    startActivity(Intent(this, popular::class.java))
                    return@OnClickListener
                }
            }
        )



    }



}