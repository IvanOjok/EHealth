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
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase
import dmax.dialog.SpotsDialog
import java.util.*

class Register : AppCompatActivity() {
    private val prefsManager = PrefsManager.INSTANCE
    var dialog: android.app.AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        prefsManager.setContext(this.application)
        dialog = SpotsDialog.Builder().setContext(this).build()

        val create = findViewById<Button>(R.id.create)
        create.setOnClickListener {

            val f = findViewById<EditText>(R.id.first)
            val first = f.text.toString()

            val l = findViewById<EditText>(R.id.last)
            val last = l.text.toString()

            val createEmail = findViewById<EditText>(R.id.createEmail)
            val email = createEmail.text.toString()

            val c = findViewById<EditText>(R.id.course)
            val course = c.text.toString()

            val std = findViewById<EditText>(R.id.stdNo)
            val stdNo = std.text.toString()

            val pwd = findViewById<EditText>(R.id.password)
            val password = pwd.text.toString()

            val conf = findViewById<EditText>(R.id.confirmPassword)
            val confirmPassword = conf.text.toString()

            if (first.isNotEmpty() && last.isNotEmpty() && email.isNotEmpty() && course.isNotEmpty() && stdNo.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && password==confirmPassword){
                dialog!!.show()

                val db = FirebaseDatabase.getInstance()
                //val id = UUID.randomUUID().toString()

                val student = Student(stdNo,first,last,email,course,stdNo,password, "none")
                val ref = db.getReference("/students").child(stdNo).setValue(student)

                if (ref.isCanceled){
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show()
                }
                else{
                    prefsManager.onLogin(student)
                    dialog!!.dismiss()
                    startActivity(Intent(this, Home::class.java))
                    finish()
                }
            }
            else{
                Toast.makeText(this, "Kindly fill in the correct credentials", Toast.LENGTH_LONG).show()
            }
        }

        val b = findViewById<ImageView>(R.id.imageView2)
        b.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

    }
}