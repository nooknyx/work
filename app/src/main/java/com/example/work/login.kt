package com.example.work

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.work.detail.AdminDashboard
import com.google.firebase.auth.FirebaseAuth

class login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val userlogin = findViewById<EditText>(R.id.loginuser)
        val passlogin = findViewById<EditText>(R.id.loginpass)


        //set up image button1234567890
        val signin = findViewById<Button>(R.id.loginloginbt)
        val signup = findViewById<Button>(R.id.signuploginbt)
        val guestlog = findViewById<TextView>(R.id.guestloginbt)

        val forgetpass = findViewById<TextView>(R.id.forgetlogin)

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
                val user = auth.uid
                if (user == "GAhbfN5PUfhLsgv2Uj8w6dZ0MWZ2")
                {
                    val intent = Intent(this, AdminDashboard::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent) }
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
            startActivity(Intent(this, ForgetPassword::class.java))
        }

        guestlog.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    //if the user already sign in, go to MainActivity
    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser != null){
            if(FirebaseAuth.getInstance().uid == "GAhbfN5PUfhLsgv2Uj8w6dZ0MWZ2"){
                val intent = Intent(this, AdminDashboard::class.java)
                startActivity(intent)
                finish()
            }
            else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }
}