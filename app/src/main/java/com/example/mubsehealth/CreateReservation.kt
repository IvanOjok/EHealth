package com.example.mubsehealth

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.mubsehealth.model.Booking
import com.example.mubsehealth.model.PrefsManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.database.FirebaseDatabase
import dmax.dialog.SpotsDialog
import java.util.*

class CreateReservation : AppCompatActivity() {

    private val prefsManager = PrefsManager.INSTANCE
    lateinit var dialog: android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_reservation)

        prefsManager.setContext(this.application)
        dialog = SpotsDialog.Builder().setContext(this).build()

        val image = findViewById<ImageView>(R.id.imageView2)
        image.setOnClickListener {
            startActivity(Intent(this, Reservations::class.java))
            finish()
        }


        val dName = intent.getStringExtra("name")
        val dPhone = intent.getStringExtra("dPhone")
        val dId = intent.getStringExtra("dId")

        val date = findViewById<TextView>(R.id.date)
        val time = findViewById<TextView>(R.id.time)
        val purpose = findViewById<EditText>(R.id.purpose)



        date.setOnClickListener {
            val cal = MaterialDatePicker.Builder.datePicker()
            cal.setTitleText("Select a Date")
            val calender = cal.build()
            calender.show(supportFragmentManager, "MATERIAL_DATE_PICKER")

            calender.addOnPositiveButtonClickListener(object : MaterialPickerOnPositiveButtonClickListener<Long>{
                override fun onPositiveButtonClick(selection: Long) {
                    date.setText(calender.headerText)
                }

            })

        }
//
        time.setOnClickListener {

            val t = MaterialTimePicker.Builder().setTitleText("Select time").setHour(12).setMinute(10)
                .setTimeFormat(TimeFormat.CLOCK_12H).build()


            val timer = t.show(supportFragmentManager, "TIME")
            t.addOnPositiveButtonClickListener {
                val phour = t.hour
                val pmin = t.minute

                val fTime = when {
                    phour > 12 -> {
                        if (pmin<10){
                            "${phour-12}:${pmin} pm"
                        }
                        else{
                            "${phour-12}:${pmin} pm"
                        }
                    }
                    phour == 12 -> {
                        if (pmin<10){
                            "${phour}:${pmin} pm"
                        }
                        else{
                            "${phour}:${pmin} pm"
                        }
                    }
                    phour == 0 -> {
                        if (pmin<10){
                            "${phour+12}:${pmin} am"
                        }
                        else{
                            "${phour+12}:${pmin} am"
                        }
                    }
                    else -> {
                        if (pmin<10){
                            "${phour}:${pmin} am"
                        }
                        else{
                            "${phour}:${pmin} am"
                        }
                    }
                }

                time.setText(fTime)
            }

        }


        val create = findViewById<Button>(R.id.create)
        create.setOnClickListener {

            val d = date.text.toString()
            val t = time.text.toString()
            val p = purpose.text.toString()

            if (d.isNotEmpty() && t.isNotEmpty() && p.isNotEmpty()){
                dialog.show()
                val db = FirebaseDatabase.getInstance()
                //val k = db.reference.key

                val k = UUID.randomUUID().toString()

                val id = prefsManager.getStudent().id
                Log.d("did", "$dName")
                Log.d("dphone", "$dPhone")
                val ref = db.getReference("/reservations").child(id!!).push().setValue(Booking(d,t,p,dName!!, dId!!, id, "Unapproved", "None"))
                val dRef = db.getReference("/reservations").child(dId).push().setValue(Booking(d,t,p,dName, dId, id, "Unapproved", "None"))

                if (ref.isCanceled){
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show()
                }
                else{
                    dialog.dismiss()
                    FirebaseDatabase.getInstance().getReference("/notifications").child(dId).push().setValue(Booking(d,t,p,dName, dId, id, "Unapproved", "None"))
                    AlertDialog.Builder(this).setMessage("Reservation created").setPositiveButton("OK", object :DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            startActivity(Intent(this@CreateReservation, Home::class.java))
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