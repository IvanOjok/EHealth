package com.example.mubsehealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class ConsultationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultations)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, Home::class.java))
        }

        val add = findViewById<ImageView>(R.id.addConsultation)
        add.setOnClickListener {
            startActivity(Intent(this, ConsultingDoctor::class.java))
        }
    }
}