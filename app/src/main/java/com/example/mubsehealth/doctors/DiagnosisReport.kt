package com.example.mubsehealth.doctors

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.mubsehealth.MyAccount
import com.example.mubsehealth.R
import com.example.mubsehealth.model.PrefsManager

class DiagnosisReport : AppCompatActivity() {
    private val prefsManager = PrefsManager.INSTANCE
    var stdID:String ?= null
    var dId:String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnosis_report)

        val dName = intent.getStringExtra("dName")
        val sNo = intent.getStringExtra("sNo")
        intent.getStringExtra("dId")
        val r  = intent.getStringExtra("result")
        val rec = intent.getStringExtra("recommendation")
        val p = intent.getStringExtra("prescription")

        val results = findViewById<TextView>(R.id.date)
        val recommendation = findViewById<TextView>(R.id.time)
        val prescription = findViewById<TextView>(R.id.purpose)
        val approve = findViewById<TextView>(R.id.approve)
        val sId = findViewById<TextView>(R.id.textView2)

        results.text = r
        recommendation.text = rec
        prescription.text = p
        approve.text = "By $dName"
        sId.text = sNo


        prefsManager.setContext(this.application)

       // dId = prefsManager.getDoctor().id

        val c = findViewById<ImageView>(R.id.back)

        if (prefsManager.isLoggedIn()){
            stdID = prefsManager.getStudent().id
            c.setOnClickListener {
                startActivity(Intent(this, MyAccount::class.java))
                finish()
            }
        }
        else{
            c.setOnClickListener {
                startActivity(Intent(this, ApprovedDiagnosis::class.java))
                finish()
            }
        }


    }
}