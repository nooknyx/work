package com.example.work.detail

import android.app.Activity
import android.app.ProgressDialog
import android.app.usage.UsageEvents.Event.NONE
import android.content.ContentValues
import android.content.Intent
import android.graphics.Insets.NONE
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.work.MainActivity
import com.example.work.R
import com.example.work.databinding.EdituserBinding
import com.example.work.databinding.FragmentUserBinding
import com.example.work.ui.User
import com.example.work.ui.booklist.allbook
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class EditUser: AppCompatActivity()
{
    //view binding
    private lateinit var binding: EdituserBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //image uri choice
    private var imageUri: Uri?=null

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = EdituserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase authen
        firebaseAuth = FirebaseAuth.getInstance()
        loadUserInfo()
        val userfragment = User()
        //handle click go back

        binding.backBtn.setOnClickListener{
            //onBackPressed()
            //startActivity(Intent(this, User::class.java))
            //this.finish()

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragmentset", "1")
            this.startActivity(intent)

            //startActivity(Intent(this, MainActivity::class.java))
            //intent.putExtra("fragmentset", "1")
        }

        //binding click, pick image from camera/gallery
        binding.userProfile.setOnClickListener{
            showImageAttachMenu()
        }

        //handle click, begin update profile
        binding.updatebtn.setOnClickListener{
            validateData()
        }

    }
    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainFragment, fragment)
            commit()

        }


    private var name = ""
    private fun validateData() {
        //get data
        name = binding.nameEt.text.toString().trim()

        //validate data
        if(name.isEmpty()){
            Toast.makeText(this,"Enter username",Toast.LENGTH_SHORT).show()
        }
        else{
            //name is entered

            if(imageUri == null){
                //update without image
                updateProfile("")

            }
            else{
                //update with image
                uploadImage()
            }
        }

    }

    private fun uploadImage(){

        progressDialog.setMessage("Upload profile image")
        progressDialog.show()

        //image path and name
        val filePathAndName = "ProfileImages/"+firebaseAuth.uid

        //storage ref
        val reference = FirebaseStorage.getInstance().getReference(filePathAndName)
        reference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot->
                //image uploaded, get url of uploaded image

                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadedImageUrl = "${uriTask.result}"

                updateProfile(uploadedImageUrl)

            }
            .addOnFailureListener{ e->
                //failed to upload image
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()

            }
    }

    private fun updateProfile(uploadImageUrl: String) {
            progressDialog.setMessage("Update Profile...")

        //setup info to update to the db
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["username"] = "$name"
        if(imageUri!=null){
            hashMap["profileImage"] = uploadImageUrl
        }

        //update to db
        val reference = FirebaseDatabase.getInstance().getReference("users")
        reference.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                //profile update
                progressDialog.dismiss()
                Toast.makeText(this,"Profile update",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                //failed to upload image
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUserInfo(){

        //db references to load user info
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user data
                    val email = "${snapshot.child("email").value}"
                    val username = "${snapshot.child("username").value}"
                    val profileImage = "${snapshot.child("profileImage").value}"
                    val userType = "${snapshot.child("userType").value}"

                    //set data
                    binding.nameEt.setText(username)

                    //set image
                    try {
                        Glide.with(this@EditUser)
                            .load(profileImage)
                            .placeholder(R.drawable.user_person)
                            .into(binding.userProfile)
                    }
                    catch (e: Exception){

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


    }

    private fun showImageAttachMenu(){

        //show pop up with option, choosing camera/ gallery for profile image


        //setup pop up menu
        val popupMenu = PopupMenu(this,binding.userProfile)

        popupMenu.menu.add(Menu.NONE,0,0,"Camera")
        popupMenu.menu.add(Menu.NONE,0,0,"Gallery")
        popupMenu.show()

        //handle popup menu item click

        popupMenu.setOnMenuItemClickListener{item->

            //get id of clicked item
            val id = item.itemId
            if(id==0){
                //camera click
                pickImageCamera()
            }

            else{
                //gallery click
                pickImageGallery()
            }

            true
        }

    }

    private fun pickImageCamera(){

        //intent to pick image form camera

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp_Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Description")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        cameraActivityResultLauncher.launch(intent)


    }

    private fun pickImageGallery(){

        //intent to pick image from gallery

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)

    }

    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> {result ->
            //get uri image
            if(result.resultCode== Activity.RESULT_OK){
                val data = result.data
                //imageUri = data!!.data >> already have image in imageuri

                //set to image view
                binding.userProfile.setImageURI(imageUri)
            }
            else{
                //cancel
                Toast.makeText(this,"Cancelled",Toast.LENGTH_SHORT).show()
            }
        }
    )

    //used to handle result of gallery

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> {result ->
            //get uri image
            if(result.resultCode== Activity.RESULT_OK){
                val data = result.data
                imageUri = data!!.data

                //set to image view
                binding.userProfile.setImageURI(imageUri)
            }
            else{
                //cancel
                Toast.makeText(this,"Cancelled",Toast.LENGTH_SHORT).show()
            }
        }
    )





}
