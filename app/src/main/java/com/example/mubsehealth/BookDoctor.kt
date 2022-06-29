package com.example.mubsehealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mubsehealth.model.BookDoctorAdapter
import com.example.mubsehealth.model.Doctor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dmax.dialog.SpotsDialog

class BookDoctor : AppCompatActivity() {

    lateinit var dialog: android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_doctor)

        dialog = SpotsDialog.Builder().setContext(this).build()
        dialog.show()

        val rView = findViewById<RecyclerView>(R.id.recyclerView)
        rView.layoutManager = LinearLayoutManager(this)
        //val o = FirebaseRecyclerOptions.Builder<Doctor>().setQuery(dbRef, Doctor::class.java).build()

        val p = ArrayList<Doctor>()
        val dbRef = FirebaseDatabase.getInstance().getReference("/doctors").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dialog.dismiss()

                for (y in snapshot.children){
                    Log.d("y", "$y")
                    val z = y.getValue(Doctor::class.java)
                    Log.d("z", "${z!!.imgUrl}")
                    p.add(z)
                    Log.d("p", "$p")
                }
                val adapter = BookDoctorAdapter(this@BookDoctor, p)
                rView.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
                Toast.makeText(this@BookDoctor, "An $error occured", Toast.LENGTH_LONG).show()
            }

        })

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, Reservations::class.java))
            finish()
        }
    }
}