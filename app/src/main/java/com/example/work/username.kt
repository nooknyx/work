package com.example.work

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


//this one is for sign up page
class username : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        val userforsign = findViewById<EditText>(R.id.usernamesign)
        val passforsign = findViewById<EditText>(R.id.passsign)
        val emailsign = findViewById<EditText>(R.id.emailsign)
        val passconsign = findViewById<EditText>(R.id.passconsign)
        val loginPg = findViewById<TextView>(R.id.loginHere)
        val signupsing = findViewById<ImageButton>(R.id.signsignbtn)

        signupsing.setOnClickListener{

            if (userforsign.text.toString().isEmpty() && passforsign.text.toString().isEmpty()
                && emailsign.text.toString().isEmpty() && passconsign.text.toString().isEmpty())
            {
                Toast.makeText(
                    applicationContext,
                    "Fill in the box", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (passforsign.text.toString().isEmpty())
            {
                Toast.makeText(
                    applicationContext,
                    "Pleas fill password", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (emailsign.text.toString().isEmpty())
            {
                Toast.makeText(
                    applicationContext,
                    "Pleas fill email", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (passconsign.text.toString().isEmpty())
            {
                Toast.makeText(
                    applicationContext,
                    "Pleas fill password again", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (passconsign.text.toString()!=passforsign.text.toString())
            {
                Toast.makeText(
                    applicationContext,
                    "Password do not match", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            //if input properly, will create account and send to firebase authenticate and send user back to login page
            else {
                auth.createUserWithEmailAndPassword(emailsign.text.toString(),passforsign.text.toString()).addOnSuccessListener {
                    Toast.makeText(applicationContext, "Sign Up Successful!", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this, login::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(applicationContext, "Sign Up Failed!", Toast.LENGTH_SHORT).show()
                }
            }

        }
        //if user already has account they can go back to login page from here
        loginPg.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }
    }

}