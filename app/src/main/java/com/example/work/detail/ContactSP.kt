package com.example.work.detail

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.work.MainActivity
import com.example.work.R
import com.example.work.databinding.ActivityBookdetailBinding
import com.example.work.databinding.ActivityContactSpBinding
import com.example.work.login

class ContactSP : AppCompatActivity() {

   //on binding
    private lateinit var binding: ActivityContactSpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactSpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //set back btn
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        val name = findViewById<EditText>(R.id.nameEt)
        val reMail = findViewById<EditText>(R.id.emailEt)
        val subject = findViewById<EditText>(R.id.topicEt)
        val body = findViewById<EditText>(R.id.detailEt)
        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        val btnsend = findViewById<Button>(R.id.submitbtn)
        reMail.isEnabled = false

        val recMail = arrayOf(reMail.text.toString())


        backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnsend.setOnClickListener {
            if (!reMail.toString().isEmpty() && !subject.toString().isEmpty() && !body.toString().isEmpty())
            {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.putExtra(Intent.EXTRA_EMAIL,recMail )
                intent.putExtra(Intent.EXTRA_SUBJECT, subject.text.toString())
                intent.putExtra(Intent.EXTRA_TEXT, body.text.toString())

                //intent.setType("message/rfc822")
                intent.setData(Uri.parse("mailto:"))

                if(intent.resolveActivity(packageManager) != null){
                    startActivity(intent)
                }else{
                    Toast.makeText(this,"The is no application that support this action", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this,"Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}