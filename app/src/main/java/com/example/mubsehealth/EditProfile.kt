package com.example.mubsehealth

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mubsehealth.model.Doctor
import com.example.mubsehealth.model.PrefsManager
import com.example.mubsehealth.model.Student
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import dmax.dialog.SpotsDialog

class EditProfile : AppCompatActivity() {
    private val prefsManager = PrefsManager.INSTANCE
    var dialog: android.app.AlertDialog? = null

    private val CAPTURE_PERMISSION_CODE = 1000
    // private val IMAGE_CAPTURE_CODE = 1001
    val PICK_PERMISSION_CODE = 1002
    val IMAGE_PICK_CODE = 1003

    var image_uri: Uri? = null
    lateinit var b:ImageView
    var mAuth: FirebaseAuth?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        prefsManager.setContext(this.application)
        dialog = SpotsDialog.Builder().setContext(this).build()

        mAuth = FirebaseAuth.getInstance()

        val f = findViewById<EditText>(R.id.first)
        f.setText(prefsManager.getStudent().firstName)

        val l = findViewById<EditText>(R.id.last)
        l.setText(prefsManager.getStudent().lastName)

        val createEmail = findViewById<EditText>(R.id.createEmail)
        createEmail.setText(prefsManager.getStudent().email)

        val c = findViewById<EditText>(R.id.course)
        c.setText(prefsManager.getStudent().course)


        val name = findViewById<TextView>(R.id.name)
        name.setText(prefsManager.getStudent().stdNo)


        b = findViewById(R.id.imageView7)

        if (prefsManager.getStudent().image != "none"){
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .error(R.drawable.ic_baseline_account_circle_24)

            Glide.with(this).load(prefsManager.getStudent().image).apply(options).into(b)

        }
        b.setOnClickListener {
            pickImageFromGallery()
        }

        val create = findViewById<Button>(R.id.create)
        create.setOnClickListener {
            val first = f.text.toString()

            val last = l.text.toString()

            val email = createEmail.text.toString()

            val course = c.text.toString()

            val password = prefsManager.getStudent().password
            val stdNo = prefsManager.getStudent().stdNo

            if (first.isNotEmpty() && last.isNotEmpty() && email.isNotEmpty() && course.isNotEmpty() && password!!.isNotEmpty()){
                dialog!!.show()

                //val id = UUID.randomUUID().toString()
                uploadImage(stdNo!!, first, last, email, course, stdNo, password)
            }
            else{
                Toast.makeText(this, "Kindly fill in the correct credentials", Toast.LENGTH_LONG).show()
            }
        }

        val r = findViewById<ImageView>(R.id.imageView2)
        r.setOnClickListener {
            startActivity(Intent(this, MyAccount::class.java))
            finish()
        }
    }

    fun pickImageFromGallery(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions:Array<out String>, grantResults:IntArray) {
        //this method is called, when user presses Allow or Deny from Permission Request Popup
        when (requestCode) {
            PICK_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            image_uri = data!!.data
            //img.setImageURI(image_uri)

            val bitmap = MediaStore.Images.Media
                .getBitmap(
                    contentResolver,
                    image_uri
                )
            b.setImageBitmap(bitmap)

        }
    }


    // UploadImage method
    fun uploadImage(
         id:String,
    firstName:String,
    lastName:String,
    email:String,
    course:String,
    stdNo:String,
    password:String
    ) {
        if (image_uri != null && mAuth!!.currentUser != null) {
            Log.d("image", image_uri.toString())

            //// && mAuth!!.currentUser != null

            // Defining the child of storageReference
            val sReference = FirebaseStorage.getInstance().getReference().child("/images").child(image_uri.toString())
            val q =  sReference.putFile(image_uri!!)
            val utask = q.continueWithTask{ task ->
                if (!task.isSuccessful){
                    task.exception.let {
                        throw it!!
                    }
                }
                else{
                    val t = sReference.downloadUrl
                    dbStore( id,
                        firstName,
                        lastName,
                        email,
                        course,
                        stdNo,
                        password,
                        t.toString())
                }
                sReference.downloadUrl

            }.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val imgUrl = task.result.toString()
                    dbStore( id,
                        firstName,
                        lastName,
                        email,
                        course,
                        stdNo,
                        password ,
                        imgUrl)
                }
                else{
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{

            Log.d("image", image_uri.toString())

            Log.d("anonymous", "ddd")
            signInAnonymously(id,
                firstName,
                lastName,
                email,
                course,
                stdNo,
                password)

        }
    }

    private fun signInAnonymously( id:String,
                                   firstName:String,
                                   lastName:String,
                                   email:String,
                                   course:String,
                                   stdNo:String,
                                   password:String,
    ) {

        mAuth!!.signInAnonymously().addOnSuccessListener(this, OnSuccessListener<AuthResult?> {
            // Defining the child of storageReference
            val sReference = FirebaseStorage.getInstance().getReference().child("/images").child(image_uri.toString())
            val q = sReference.putFile(image_uri!!)

            val utask = q.continueWithTask{ task ->
                if (!task.isSuccessful){
                    task.exception.let {
                        throw it!!
                    }
                }
                else{
                    val t = sReference.downloadUrl
                    dbStore( id,
                        firstName,
                        lastName,
                        email,
                        course,
                        stdNo,
                        password,
                        t.toString(),
                        )
                }
                sReference.downloadUrl

            }.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val imgUrl = task.result.toString()
                    dbStore( id,
                        firstName,
                        lastName,
                        email,
                        course,
                        stdNo,
                        password,
                        imgUrl.toString(),
                    )
                }
                else{
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
            }

        })
            .addOnFailureListener(this,
                OnFailureListener { exception ->
                    Log.e(
                        "STORage",
                        "signInAnonymously:FAILURE",
                        exception
                    )
                })

    }

    fun dbStore(id:String,
                firstName:String,
                lastName:String,
                email:String,
                course:String,
                stdNo:String,
                password:String,
                image:String){

        prefsManager.onLogout()

        val db =
            FirebaseDatabase.getInstance().getReference("/students").child(stdNo)
                .setValue(
                    Student(
                        id,
                        firstName,
                        lastName,
                        email,
                        course,
                        stdNo,
                        password,
                        image
                    )
                )
        if (db.isCanceled) {
            Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG)
                .show()
        } else {

            prefsManager.onLogin(
                Student(
                    id,
                    firstName,
                    lastName,
                    email,
                    course,
                    stdNo,
                    password,
                    image
                )
            )
            startActivity(Intent(this, MyAccount::class.java))
            finish()
        }
    }



}