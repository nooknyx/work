package com.example.work

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val userlogin = findViewById<EditText>(R.id.loginuser)
        val passlogin = findViewById<EditText>(R.id.loginpass)


        //set up image button1234567890
        val signin = findViewById<ImageButton>(R.id.loginloginbt)
        val signup = findViewById<ImageButton>(R.id.signuploginbt)


        val forgetpass = findViewById<ImageButton>(R.id.forgetlogin)

        //get firebase authentication
        auth = FirebaseAuth.getInstance()

        //setting login button
        signin.setOnClickListener{

            //validate username
            if (userlogin.text.toString().isEmpty() && passlogin.text.toString().isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Enter username or password", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (userlogin.text.toString().isEmpty()){
                Toast.makeText(
                    applicationContext, "Enter Username", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (passlogin.text.toString().isEmpty()){
                Toast.makeText(
                    applicationContext, "Enter Password", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }


            //authentication the user login
            auth.signInWithEmailAndPassword(userlogin.text.toString(),passlogin.text.toString()).addOnSuccessListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Failed to Login!", Toast.LENGTH_SHORT).show()
            }
        }

        //setting sign up button
        signup.setOnClickListener{
            val intent = Intent(this, username::class.java)
            startActivity(intent)
        }

        forgetpass.setOnClickListener{
            Toast.makeText(
                applicationContext, "Check your email", Toast.LENGTH_SHORT).show()
        }

    }
    //if the user already sign in, go to MainActivity
    /*override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }*/

}