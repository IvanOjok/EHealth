package com.example.mubsehealth.doctors

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.mubsehealth.Login
import com.example.mubsehealth.R
import com.example.mubsehealth.model.PrefsManager

class DoctorAccountInfo : AppCompatActivity() {
    private val prefsManager = PrefsManager.INSTANCE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_account_info)

        prefsManager.setContext(this.application)

        val name = findViewById<TextView>(R.id.name)
        val stdNo = findViewById<TextView>(R.id.id)
        val course = findViewById<TextView>(R.id.course)
        val email = findViewById<TextView>(R.id.email)

        val student = prefsManager.getDoctor()
        name.text = "${student.firstName}  ${student.lastName}"
        stdNo.text = "ID: ${student.id}"
        course.text = "Course: ${student.phone}"
        email.text = "Email: ${student.email}"

        val logout = findViewById<Button>(R.id.logout)
        logout.setOnClickListener {
            prefsManager.onLogout()
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}