package com.example.mubsehealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class DoctorInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_info)

        val dName = intent.getStringExtra("name")
        val dPhone = intent.getStringExtra("dPhone")
        val img = intent.getStringExtra("img",)
        val mail = intent.getStringExtra("email")
        val dNo = intent.getStringExtra("idNo")
        val prof = intent.getStringExtra("prof")

        val imgId = findViewById<ImageView>(R.id.imageView7)
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_account_circle_24)
            .error(R.drawable.ic_baseline_account_circle_24)

        Glide.with(this).load(img).apply(options).into(imgId)

        val name = findViewById<TextView>(R.id.name)
        name.text = dName

        val id = findViewById<TextView>(R.id.id)
        id.text = "ID: $dNo"

        val email = findViewById<TextView>(R.id.mail)
        email.text = "Email: $mail"

        val phone = findViewById<TextView>(R.id.phone)
        phone.text = "Phone: $dPhone"

        val dProf = findViewById<TextView>(R.id.profession)
        dProf.text = "Profession: $prof"
    }
}