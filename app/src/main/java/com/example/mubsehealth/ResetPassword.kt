package com.example.mubsehealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.mubsehealth.model.PrefsManager
import com.example.mubsehealth.model.Student
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dmax.dialog.SpotsDialog

class ResetPassword : AppCompatActivity() {


    private val prefsManager = PrefsManager.INSTANCE
    lateinit var dialog: android.app.AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        prefsManager.setContext(this.application)
        dialog = SpotsDialog.Builder().setContext(this).build()

        val b = findViewById<ImageView>(R.id.imageView2)
        b.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        val log = findViewById<Button>(R.id.reset)
        log.setOnClickListener {


            val id = findViewById<EditText>(R.id.stdNo)
            val stdNo = id.text.toString()

            val pwd = findViewById<EditText>(R.id.password)
            val password = pwd.text.toString()

            if(stdNo.isNotEmpty() && password.isNotEmpty()){
                dialog.show()

                    val db = FirebaseDatabase.getInstance()
                   val ref =  db.getReference().child("/students").child(stdNo)
                       ref.addValueEventListener(object :
                        ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {

                            if(p0.exists()){
                                val std = p0.getValue(Student::class.java)
                                val sId = std!!.id
                                val first = std.firstName
                                val last = std.lastName
                                val course = std.course
                                val email = std.email
                                val stdNumber = std.stdNo

                                val update = ref.child("password").setValue(password)
                                if (update.isCanceled){
                                    Toast.makeText(this@ResetPassword, "An error occurred", Toast.LENGTH_LONG).show()
                                }
                                else{
                                    val student = Student(sId!!, first!!, last!!, email!!, course!!, stdNumber!!, password, "none")
                                    prefsManager.onLogin(student)
                                    startActivity(Intent(this@ResetPassword, Home::class.java))
                                    finish()

                                }
                            }
                            else{
                                Toast.makeText(this@ResetPassword, "No Account found", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onCancelled(p0: DatabaseError) {
                            Toast.makeText(this@ResetPassword, "An error occurred $p0", Toast.LENGTH_LONG).show()
                        }

                    })

            }
            else{
                Toast.makeText(this, "Fill in the correct Details", Toast.LENGTH_LONG).show()
            }
        }
    }
}