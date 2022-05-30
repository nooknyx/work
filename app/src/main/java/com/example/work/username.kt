package com.example.work

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserHandle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.work.data.UserData
import com.example.work.ui.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


//this one is for sign up page
class username : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var rootNode = Firebase.database
    private lateinit var progressDialog: ProgressDialog
    //var myRef = rootNode.getReference("users")

    //myRef.setValue("Hello, World!")
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

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Waiting...")
        progressDialog.setCanceledOnTouchOutside(false)

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

                progressDialog.setMessage("Creating User Account...")
                progressDialog.show()
                auth.createUserWithEmailAndPassword(emailsign.text.toString(),passforsign.text.toString()).addOnSuccessListener {
                    Toast.makeText(applicationContext, "Sign Up Successful!", Toast.LENGTH_SHORT)
                        .show()
                    updateUserInfo()
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

    private fun updateUserInfo() {

        progressDialog.setMessage("Saving User Data...")

        val userforsign = findViewById<EditText>(R.id.usernamesign)

        val emailsign = findViewById<EditText>(R.id.emailsign)

        val timestamp = System.currentTimeMillis()

        val uid = auth.uid
        val hashMap: HashMap<String, Any> = HashMap()

        hashMap["uid"] = uid.toString()
        hashMap["email"] = emailsign.text.toString().trim()
        hashMap["username"] = userforsign.text.toString().trim()
        hashMap["profileImage"] = ""
        hashMap["userType"] = "user"
        hashMap["timestamp"] = timestamp

        val myRef = FirebaseDatabase.getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")
        myRef.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Account Creation Successful!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,login::class.java))

                finish()
            }
            .addOnFailureListener{ e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Failed to create user data bacause of ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }


}