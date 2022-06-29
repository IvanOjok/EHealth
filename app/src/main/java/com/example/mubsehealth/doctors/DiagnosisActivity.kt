package com.example.mubsehealth.doctors

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mubsehealth.Home
import com.example.mubsehealth.R
import com.example.mubsehealth.model.Diagnosis
import com.example.mubsehealth.model.PrefsManager
import com.google.firebase.database.FirebaseDatabase
import dmax.dialog.SpotsDialog
import java.util.*

class DiagnosisActivity : AppCompatActivity() {

    private val prefsManager = PrefsManager.INSTANCE
    lateinit var dialog: android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnosis)

        prefsManager.setContext(this.application)
        dialog = SpotsDialog.Builder().setContext(this).build()

        val sId = intent.getStringExtra("sNo")

        val c = findViewById<ImageView>(R.id.back)

        c.setOnClickListener {
            startActivity(Intent(this, DoctorReservation::class.java))
            finish()
        }

        val results = findViewById<EditText>(R.id.date)
        val recommendation = findViewById<EditText>(R.id.time)
        val prescription = findViewById<EditText>(R.id.purpose)


        val create = findViewById<Button>(R.id.create)
        create.setOnClickListener {

            val d = results.text.toString()
            val t = recommendation.text.toString()
            val p = prescription.text.toString()

            if (d.isNotEmpty() && t.isNotEmpty() && p.isNotEmpty()){
                dialog.show()
                val db = FirebaseDatabase.getInstance()
                //val k = db.reference.key

                val k = UUID.randomUUID().toString()

                val dId = prefsManager.getDoctor().id
                val dName = prefsManager.getDoctor().firstName + prefsManager.getDoctor().lastName

                val ref = db.getReference("/diagnosis").child(sId!!).push().setValue(Diagnosis(d,t,p, dId!!, dName, sId))
                val dRef = db.getReference("/diagnosis").child(dId).push().setValue(Diagnosis(d,t,p, dId, dName, sId))
                if (ref.isCanceled){
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show()
                }
                else{
                    dialog.dismiss()
                    AlertDialog.Builder(this).setMessage("Diagnosis approved").setPositiveButton("OK", object :
                        DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            startActivity(Intent(this@DiagnosisActivity, DoctorsHome::class.java))
                            finish()
                        }

                    }).show()
//                    startActivity(Intent(this, Home::class.java))
//                    finish()
                }
            }
            else{
                Toast.makeText(this, "Kindly fill in the correct credentials", Toast.LENGTH_LONG).show()

            }
        }


    }
}