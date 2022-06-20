package com.example.work

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Process
import android.util.Patterns
import android.widget.ProgressBar
import android.widget.Toast
import com.example.work.databinding.ActivityForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPassword : AppCompatActivity() {

    //binding
    private lateinit var binding: ActivityForgetPasswordBinding

    //firebase authen
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase authen
        firebaseAuth = FirebaseAuth.getInstance()

       //binding submit button
        binding.submitbtn.setOnClickListener{
            validateData()
        }

        //handle backbutton click, go back
        binding.backBtn.setOnClickListener{
            onBackPressed()
        }

    }

    private var email = ""

    private fun validateData(){

        //get data
        email = binding.emailEt.text.toString().trim()

        //validate data
        if (email.isEmpty()){
            Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid email",Toast.LENGTH_SHORT).show()
        }
        else{
            recoverPassword()
        }

    }

    private fun recoverPassword(){

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Toast.makeText(this, "Instruction send to email", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this,"Failed to send", Toast.LENGTH_SHORT).show()
            }
    }
}