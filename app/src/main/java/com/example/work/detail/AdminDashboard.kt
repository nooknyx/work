package com.example.work.detail

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.widget.*
import com.bumptech.glide.Glide
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import com.example.work.R
import com.example.work.databinding.ActivityAdminDashboardBinding
import com.example.work.login
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AdminDashboard : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    private lateinit var binding: ActivityAdminDashboardBinding

    private var imageUri: Uri?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        val BookTitle = findViewById<EditText>(R.id.booknameEt)
        val IdBook = findViewById<EditText>(R.id.bookidEt)
        val Author = findViewById<EditText>(R.id.authorEt)
        val Category = findViewById<EditText>(R.id.categoryEt)
        val Detail = findViewById<EditText>(R.id.detailEt)
        val Barcode = findViewById<EditText>(R.id.barcodeEt)

        backBtn.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, login::class.java))
            finish()
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Waiting...")
        progressDialog.setCanceledOnTouchOutside(false)

        val Submit = findViewById<Button>(R.id.submitbtn)
        val cleardata = findViewById<TextView>(R.id.cleardata)

        Submit.setOnClickListener{

            if (BookTitle.text.toString().isEmpty() && IdBook.text.toString().isEmpty()
                && Author.text.toString().isEmpty() && Category.text.toString().isEmpty()
                && Detail.text.toString().isEmpty())
            {
                Toast.makeText(
                    applicationContext,
                    "Fill in the box", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (BookTitle.text.toString().isEmpty())
            {
                Toast.makeText(
                    applicationContext,
                    "Pleas fill Book Title", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (IdBook.text.toString().isEmpty())
            {
                Toast.makeText(
                    applicationContext,
                    "Pleas fill Book id", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (Author.text.toString().isEmpty())
            {
                Toast.makeText(
                    applicationContext,
                    "Pleas fill Author", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (Category.text.toString().isEmpty())
            {
                Toast.makeText(
                    applicationContext,
                    "Pleas fill Category", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (Detail.text.toString().isEmpty())
            {
                Toast.makeText(
                    applicationContext,
                    "Pleas fill Book Summary", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (Barcode.text.toString().isEmpty() || Barcode.text.length < 13)
            {
                Toast.makeText(
                    applicationContext,
                    "Pleas fill Book barcode", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }


            //if input properly, will create new book and send to firebase
            else {
                progressDialog.setMessage("Creating New Book...")
                progressDialog.show()
                addBook()

            }
        }

        binding.bookpic.setOnClickListener{
            showImageAttachMenu()
        }

        cleardata.setOnClickListener {
            BookTitle.text = null
            IdBook.text = null
            Author.text = null
            Category.text = null
            Detail.text = null
            Barcode.text = null
            Toast.makeText(applicationContext,"All data is cleared.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun addBook(){
        // showing progress
        //progressDialog.setMessage("Adding Book")
        //progressDialog.show()
        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        val BookTitle = findViewById<EditText>(R.id.booknameEt)
        val IdBook = findViewById<EditText>(R.id.bookidEt)
        val Author = findViewById<EditText>(R.id.authorEt)
        val Category = findViewById<EditText>(R.id.categoryEt)
        val Detail = findViewById<EditText>(R.id.detailEt)
        val viewCount = 0
        val Barcode = findViewById<EditText>(R.id.barcodeEt)
        val timestamp = "${System.currentTimeMillis()}"
        val AverageRatings = 0.0
        val numUserRated = 0
        //setting up data to add comment in database

        val hashMap = HashMap<String, Any>()
        hashMap["Author"] = Author.text.toString().trim()
        hashMap["AverageRatings"] = AverageRatings.toFloat()
        hashMap["Barcode"] = Barcode.text.toString().toLong()
        hashMap["BookTitle"] = BookTitle.text.toString().trim()
        hashMap["bookId"] = IdBook.text.toString().trim()
        hashMap["bookSummary"] = Detail.text.toString().trim()
        hashMap["category"] = Category.text.toString().trim()
        hashMap["Image"] = ""
        hashMap["dateAdded"] = timestamp.toLong()
        hashMap["viewCount"] = viewCount.toLong()
        hashMap["numUserRated"] = numUserRated.toLong()
        //add data into the the database
        //path book > bookid > comment > commentId > commentdata

        val ref = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Books")

        ref.child(IdBook.text.toString().trim())
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Book added", Toast.LENGTH_SHORT).show()
                BookTitle.text = null
                IdBook.text = null
                Author.text = null
                Category.text = null
                Detail.text = null
                Barcode.text = null
            }
            .addOnFailureListener{ e->
                progressDialog.dismiss()
                Toast.makeText(this,"Failed to add book", Toast.LENGTH_SHORT).show()
            }

    }

    private fun updateBookcover(uploadImageUrl: String) {

        progressDialog.setMessage("Update Book cover...")

        //setup info to update to the db
        val hashMap: HashMap<String, Any> = HashMap()


        if(imageUri!=null){
            hashMap["profileImage"] = uploadImageUrl
        }

        //update to db
        val reference = FirebaseDatabase
            .getInstance("https://storytellerdb-2ff7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Books")
        reference.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                //profile update
                progressDialog.dismiss()
                Toast.makeText(this,"Bookcover update",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                //failed to upload image
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to update bookcover", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImage(){

        progressDialog.setMessage("Upload profile image")
        progressDialog.show()

        //image path and name
        val filePathAndName = "ProfileImages/"+firebaseAuth.uid

        //storage ref
        val reference = FirebaseStorage
            .getInstance("gs://storytellerdb-2ff7a.appspot.com")
            .getReference(filePathAndName)

        reference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot->
                //image uploaded, get url of uploaded image

                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadedImageUrl = "${uriTask.result}"

                updateBookcover(uploadedImageUrl)

            }
            .addOnFailureListener{ e->
                //failed to upload image
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()

            }
    }

    private fun showImageAttachMenu(){

        //show pop up with option, choosing camera/ gallery for profile image

        //setup pop up menu
        val popupMenu = PopupMenu(this,binding.bookpic)

        popupMenu.menu.add(Menu.NONE,0,0,"Gallery")
        popupMenu.show()

        //handle popup menu item click
        popupMenu.setOnMenuItemClickListener{item->

            //get id of clicked item
            val id = item.itemId
            if(id==0){
                pickImageGallery()

            }
            true
        }

    }


    private fun pickImageGallery(){

        //intent to pick image from gallery

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)

    }


    //used to handle result of gallery

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            //get uri image
            if(result.resultCode== Activity.RESULT_OK){
                val data = result.data
                imageUri = data!!.data

                //set to image view
                binding.bookpic.setImageURI(imageUri)
            }
            else{
                //cancel
                Toast.makeText(this,"Cancelled",Toast.LENGTH_SHORT).show()
            }
        }
    )

}