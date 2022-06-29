package com.example.mubsehealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mubsehealth.model.Doctor
import com.example.mubsehealth.model.DoctorAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import dmax.dialog.SpotsDialog

class DoctorsActivity : AppCompatActivity() {
    lateinit var dialog: android.app.AlertDialog
    override fun onCreate(savedInstanceState: Bundle? ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctors)

        dialog = SpotsDialog.Builder().setContext(this).build()
        dialog.show()

        val rView = findViewById<RecyclerView>(R.id.recyclerView)
        rView.layoutManager = LinearLayoutManager(this)
        //val o = FirebaseRecyclerOptions.Builder<Doctor>().setQuery(dbRef, Doctor::class.java).build()

        val p = ArrayList<Doctor>()
       val dbRef = FirebaseDatabase.getInstance().getReference("/doctors").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                dialog.dismiss()
                for (y in snapshot.children){
                    Log.d("y", "$y")
                    val z = y.getValue(Doctor::class.java)
                    Log.d("z", "${z!!.imgUrl}")
                    p.add(z)
                    Log.d("p", "$p")
                }
                val adapter = DoctorAdapter(this@DoctorsActivity, p)
                rView.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
                Toast.makeText(this@DoctorsActivity, "An $error occured", Toast.LENGTH_LONG).show()
            }

        })


        val b = findViewById<ImageView>(R.id.addDoctor)
        b.setOnClickListener {
            startActivity(Intent(this, AddDoctor::class.java))
            finish()
        }

        val c = findViewById<ImageView>(R.id.back)
        c.setOnClickListener {
            startActivity(Intent(this, Admin::class.java))
            finish()
        }
    }
}