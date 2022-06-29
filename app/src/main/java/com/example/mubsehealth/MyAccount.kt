package com.example.mubsehealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mubsehealth.model.Diagnosis
import com.example.mubsehealth.model.DiagnosisAdapter
import com.example.mubsehealth.model.PrefsManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import dmax.dialog.SpotsDialog

class MyAccount : AppCompatActivity() {

    private val prefsManager = PrefsManager.INSTANCE
    lateinit var dialog: android.app.AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)

        prefsManager.setContext(this.application)

        val name = findViewById<TextView>(R.id.name)
        val stdNo = findViewById<TextView>(R.id.id)
        val course = findViewById<TextView>(R.id.course)
        val email = findViewById<TextView>(R.id.email)
        val imageView7 = findViewById<ImageView>(R.id.imageView7)

        val student = prefsManager.getStudent()
        name.text = "${student.firstName}  ${student.lastName}"
        //stdNo.text = "ID: ${student.id}"
        course.text = "Course: ${student.course}"
        email.text = "Email: ${student.email}"


        if (prefsManager.getStudent().image != "none") {

            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .error(R.drawable.ic_baseline_account_circle_24)

            Glide.with(this).load(prefsManager.getStudent().image).apply(options).into(imageView7)
        }
        val logout = findViewById<Button>(R.id.logout)
        logout.setOnClickListener {
            prefsManager.onLogout()
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        stdNo.setOnClickListener {
            startActivity(Intent(this, EditProfile::class.java))
        }

        val sID = prefsManager.getStudent().id

        val rView = findViewById<RecyclerView>(R.id.recyclerView)
        rView.layoutManager = LinearLayoutManager(this)

        dialog = SpotsDialog.Builder().setContext(this).build()

        dialog.show()


        val p = ArrayList<Diagnosis>()
        val dbRef = FirebaseDatabase.getInstance().getReference("/diagnosis").child(sID!!).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dialog.dismiss()
                for (y in snapshot.children){

                    Log.d("y", "$y")
                    val z = y.getValue(Diagnosis::class.java)
                    //Log.d("z", "${z!!.imgUrl}")

                    val index = p.size - 1
                    p.add(z!!)
                    //p.add(index,  z!!)
                    Log.d("p", "$p")
                }
                val adapter = DiagnosisAdapter(this@MyAccount, p)
                rView.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
                Toast.makeText(this@MyAccount, "An $error occured", Toast.LENGTH_LONG).show()
            }

        })


    }
}