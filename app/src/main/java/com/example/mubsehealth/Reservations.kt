package com.example.mubsehealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mubsehealth.model.Booking
import com.example.mubsehealth.model.BookingAdapter
import com.example.mubsehealth.model.PrefsManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dmax.dialog.SpotsDialog

class Reservations : AppCompatActivity() {

    private val prefsManager = PrefsManager.INSTANCE
    lateinit var dialog: android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservations)

        prefsManager.setContext(this.application)

        val stdID = prefsManager.getStudent().id

        val rView = findViewById<RecyclerView>(R.id.recyclerView)
        rView.layoutManager = LinearLayoutManager(this)
        //val o = FirebaseRecyclerOptions.Builder<Doctor>().setQuery(dbRef, Doctor::class.java).build()

        dialog = SpotsDialog.Builder().setContext(this).build()

        dialog.show()

        val p = ArrayList<Booking>()
        val dbRef = FirebaseDatabase.getInstance().getReference("/reservations").child(stdID!!).addValueEventListener(object :
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
                val adapter = BookingAdapter(this@Reservations, p)
                rView.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
                Toast.makeText(this@Reservations, "An $error occured", Toast.LENGTH_LONG).show()
            }

        })

        val addReservation = findViewById<ImageView>(R.id.addReservation)
        addReservation.setOnClickListener {
            startActivity(Intent(this, BookDoctor::class.java))
        }

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, Home::class.java))
        }
    }
}