package com.example.mubsehealth.doctors

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mubsehealth.R
import com.example.mubsehealth.model.Booking
import com.example.mubsehealth.model.BookingAdapter
import com.example.mubsehealth.model.PrefsManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dmax.dialog.SpotsDialog

class DoctorReservation : AppCompatActivity() {
    lateinit var dialog: android.app.AlertDialog
    private val prefsManager = PrefsManager.INSTANCE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_reservation)

        prefsManager.setContext(this.application)

        val d = prefsManager.getDoctor()

        val dname = d.firstName + d.lastName

        val id = prefsManager.getDoctor().id

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, DoctorsHome::class.java))
        }


        val rView = findViewById<RecyclerView>(R.id.recyclerView)
        rView.layoutManager = LinearLayoutManager(this)
        //val o = FirebaseRecyclerOptions.Builder<Doctor>().setQuery(dbRef, Doctor::class.java).build()

        dialog = SpotsDialog.Builder().setContext(this).build()

        dialog.show()

        val p = ArrayList<Booking>()
        val key = FirebaseDatabase.getInstance().getReference().child("/reservations").key
        Log.d("key", key!!)
        val dbRef = FirebaseDatabase.getInstance().getReference().child("/reservations").child(id!!).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dialog.dismiss()
                for (y in snapshot.children){

                    Log.d("y", "$y")
                    val z = y.getValue(Booking::class.java)
                    //Log.d("z", "${z!!.imgUrl}")

                    val index = p.size - 1
                    p.add(z!!)
                    //p.add(index,  z!!)
                    Log.d("p", "$p")
                }
                val adapter = BookingAdapter(this@DoctorReservation, p)
                rView.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
                Toast.makeText(this@DoctorReservation, "An $error occured", Toast.LENGTH_LONG).show()
            }

        })



    }
}