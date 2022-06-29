package com.example.mubsehealth

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.FragmentActivity
import com.example.mubsehealth.model.Doctor
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dmax.dialog.SpotsDialog


class AddDoctor : AppCompatActivity() {

    //private val prefsManager = PrefsManager.INSTANCE

    private val CAPTURE_PERMISSION_CODE = 1000
   // private val IMAGE_CAPTURE_CODE = 1001
    val PICK_PERMISSION_CODE = 1002
    val IMAGE_PICK_CODE = 1003

    var image_uri: Uri? = null
    lateinit var b:ImageView
    var mAuth:FirebaseAuth ?= null
    lateinit var dialog: android.app.AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_doctor)

        mAuth = FirebaseAuth.getInstance()
        dialog = SpotsDialog.Builder().setContext(this).build()

        val back = findViewById<ImageView>(R.id.imageView2)
        back.setOnClickListener {
            startActivity(Intent(this, DoctorsActivity::class.java))
            finish()
        }

        val add = findViewById<MaterialButton>(R.id.add)
        add.setOnClickListener {
            val f = findViewById<EditText>(R.id.first)
            val first = f.text.toString()

            val l = findViewById<EditText>(R.id.last)
            val last = l.text.toString()

            val createEmail = findViewById<EditText>(R.id.createEmail)
            val email = createEmail.text.toString()

            val c = findViewById<EditText>(R.id.profession)
            val course = c.text.toString()

            val doc = findViewById<EditText>(R.id.dNo)
            val dNo = doc.text.toString()

            val ph = findViewById<EditText>(R.id.phone)
            val phone = ph.text.toString()

            if (dNo.isNotEmpty() && first.isNotEmpty() && last.isNotEmpty() && email.isNotEmpty() && course.isNotEmpty() && phone.isNotEmpty() && image_uri != null){
                dialog.show()
                uploadImage(dNo, first,last,email, course, dNo, phone)
                Log.d("upload", "${uploadImage(dNo, first,last,email, course, dNo, phone)}")
            }
            else{
                Toast.makeText(this, "fill in all details", Toast.LENGTH_SHORT).show()
            }


        }


        b = findViewById(R.id.img)
        b.setOnClickListener {
            pickImageFromGallery()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.CAMERA) ==
//                PackageManager.PERMISSION_DENIED ||
//                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
//                PackageManager.PERMISSION_DENIED
//            ) {
//                //permission not enabled, request it
//                val permission = arrayOf(
//                    Manifest.permission.CAMERA,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//                //show popup to request permissions
//                requestPermissions(permission, PICK_PERMISSION_CODE)
//
//            } else {
//                //permission already granted
//                //openCamera()
//                pickImageFromGallery()
//            }
//        } else {
//            //system os < marshmallow
//            //openCamera()
//            pickImageFromGallery()
//        }
     }
   }

//    fun openCamera() {
//        val values = ContentValues()
//        values.put(MediaStore.Images.Media.TITLE, "New Picture");
//        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
//        image_uri = applicationContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//        //Camera intent
//        val cameraIntent =  Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
//        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
//    }

    fun pickImageFromGallery(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions:Array<out String>, grantResults:IntArray) {
        //this method is called, when user presses Allow or Deny from Permission Request Popup
        when (requestCode) {
//            CAPTURE_PERMISSION_CODE -> {
//                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    openCamera()
//                } else {
//                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
//                }
//            }
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

//        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_CAPTURE_CODE){
//            //img.setImageURI(image_uri)
//
//            val img = findViewById<ImageView>(R.id.img)
//            val bitmap = MediaStore.Images.Media
//                .getBitmap(
//                    applicationContext.contentResolver,
//                    image_uri
//                )
//            img.setImageBitmap(bitmap)
////            img.borderWidth = R.dimen._2sdp
////            img.borderColor = resources.getColor(R.color.backPurple)
//        }
//        else
            if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            image_uri = data!!.data
            //img.setImageURI(image_uri)

            val bitmap = MediaStore.Images.Media
                .getBitmap(
                    contentResolver,
                    image_uri
                )
            b.setImageBitmap(bitmap)
//            b.borderWidth = R.dimen._2sdp
//            b.borderColor = resources.getColor(R.color.backPurple)
        }
    }


    // UploadImage method
    fun uploadImage(
        id: String,
        firstName: String,
        lastName: String,
        email: String,
        profession: String,
        doctorNo: String,
        phone: String,
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
                        profession,
                        doctorNo,
                        t.toString(),
                        phone)
                }
                sReference.downloadUrl

            }.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val imgUrl = task.result.toString()
                    dbStore( id,
                        firstName,
                        lastName,
                        email,
                        profession,
                        doctorNo,
                        imgUrl,
                        phone)
                }
                else{
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
            }

//                .addOnSuccessListener {
//                object : OnSuccessListener<UploadTask.TaskSnapshot> {
//                    override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
//                        Log.d("P0", p0.toString())
//                        Toast.makeText(this@AddDoctor, "Image Uploaded", Toast.LENGTH_LONG).show()
//                        val imgUrl = p0!!.uploadSessionUri.toString()
//                        Log.d("imgUrl", imgUrl)
//
//                        dbStore(id,
//                            firstName,
//                            lastName,
//                            email,
//                            profession,
//                            doctorNo,
//                            imgUrl,
//                            phone)
//
//                    }
//
//
//                }
//            }
//                .addOnFailureListener {
//                    object : OnFailureListener {
//                        override fun onFailure(p0: java.lang.Exception) {
//                            Toast.makeText(
//                                this@AddDoctor,
//                                "An error occurred uploading",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//
//                    }
//                }
        }
        else{

            Log.d("image", image_uri.toString())

            Log.d("anonymous", "ddd")
            signInAnonymously(id,
                firstName,
                lastName,
                email,
                profession,
                doctorNo,
                phone)

        }
    }

    private fun signInAnonymously( id:String,
                                   firstName:String,
                                   lastName:String,
                                   email:String,
                                   profession:String,
                                   doctorNo:String,
                                   phone:String) {

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
                               profession,
                               doctorNo,
                               t.toString(),
                               phone)
                       }
                       sReference.downloadUrl

                   }.addOnCompleteListener { task ->
                       if (task.isSuccessful){
                           val imgUrl = task.result.toString()
                           dbStore( id,
                               firstName,
                               lastName,
                               email,
                               profession,
                               doctorNo,
                               imgUrl,
                               phone)
                       }
                       else{
                           Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                       }
                   }



//                .addOnSuccessListener {
//                OnSuccessListener<UploadTask.TaskSnapshot> { p0 ->
//
//                    dialog.dismiss()
//           Toast.makeText(this, "Image Uploaded", Toast.LENGTH_LONG).show()
//                    Log.d("P0", p0.toString())
//                    val imgUrl = p0!!.uploadSessionUri.toString()
//                    Log.d("imgUrl", imgUrl)
//
//                    dbStore( id,
//                        firstName,
//                        lastName,
//                        email,
//                        profession,
//                        doctorNo,
//                        imgUrl,
//                        phone)
//
//                }
//            }
//                .addOnFailureListener {
//                    object : OnFailureListener {
//                        override fun onFailure(p0: java.lang.Exception) {
//                            Toast.makeText(
//                                this@AddDoctor,
//                                "An error occurred uploading",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//
//                    }
//                }
//                       .addOnCompleteListener{
//                           OnCompleteListener<UploadTask.TaskSnapshot> { p0 ->
//
//                               dialog.dismiss()
//                               Toast.makeText(this, "Image Uploaded", Toast.LENGTH_LONG).show()
//                               Log.d("P0", p0.toString())
//                               val imgUrl = p0.result.uploadSessionUri.toString()
//                               Log.d("imgUrl", imgUrl)
//
//                               dbStore( id,
//                                   firstName,
//                                   lastName,
//                                   email,
//                                   profession,
//                                   doctorNo,
//                                   imgUrl,
//                                   phone)
//
//                           }
//
//                       }



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
                 profession:String,
                 doctorNo:String,
                 imgUrl:String,
                 phone:String){
        val db =
            FirebaseDatabase.getInstance().getReference("/doctors").child(doctorNo)
                .setValue(
                    Doctor(
                        id,
                        firstName,
                        lastName,
                        email,
                        profession,
                        doctorNo,
                        imgUrl,
                        phone
                    )
                )
        if (db.isCanceled) {
            Toast.makeText(this@AddDoctor, "An error occurred", Toast.LENGTH_LONG)
                .show()
        } else {
            startActivity(Intent(this@AddDoctor, DoctorsActivity::class.java))
            finish()
        }
    }



}