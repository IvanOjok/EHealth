package com.example.mubsehealth.doctors

import android.app.AlertDialog
import android.app.AlertDialog.THEME_HOLO_LIGHT
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.mubsehealth.Home
import com.example.mubsehealth.Login
import com.example.mubsehealth.R
import com.example.mubsehealth.Reservations
import com.example.mubsehealth.model.Booking
import com.example.mubsehealth.model.PrefsManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FinalReservationActivity : AppCompatActivity() {
    private val prefsManager = PrefsManager.INSTANCE
    var stdID:String ?= null
    var dId:String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_reservation)

        val dName = intent.getStringExtra("dName")
        val docId = intent.getStringExtra("dId")
        val sNo = intent.getStringExtra("sNo")
        val purpose = intent.getStringExtra("purpose")
        val time = intent.getStringExtra("time")
        val date = intent.getStringExtra("date")
        val message = intent.getStringExtra("message")
        val approval = intent.getStringExtra("approval")


        val name = findViewById<TextView>(R.id.name)
        val timed = findViewById<TextView>(R.id.id)
        val purposed = findViewById<TextView>(R.id.course)
        val messaged = findViewById<TextView>(R.id.message)
        val approved = findViewById<TextView>(R.id.approve)


        val c = findViewById<ImageView>(R.id.back)
        val logout = findViewById<Button>(R.id.logout)

        prefsManager.setContext(this.application)

        if (prefsManager.isLoggedIn())    {
            stdID = prefsManager.getStudent().id

          logout.setText("Delete Reservation")

            name.text = dName
            timed.text = "$date $time"
            purposed.text = purpose
            messaged.text = "Doctor's message $message"
            approved.text = approval

            c.setOnClickListener {
                startActivity(Intent(this, Home::class.java))
                finish()
            }

            logout.setOnClickListener {

                val ref = FirebaseDatabase.getInstance().getReference("/reservations").child(stdID!!)
                    ref.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        for (y in snapshot.children){

                            Log.d("y", "$y")
                            val z = y.getValue(Booking::class.java)
                            //Log.d("z", "${z!!.imgUrl}")
                            if (z!!.purpose == purpose){
                                y.ref.removeValue()
                            }
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@FinalReservationActivity, "$error occured", Toast.LENGTH_SHORT).show()
                    }

                })


                val dref = FirebaseDatabase.getInstance().getReference("/reservations").child(docId!!)
                dref.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        for (l in snapshot.children){

                            Log.d("y", "$l")
                            val z = l.getValue(Booking::class.java)
                            //Log.d("z", "${z!!.imgUrl}")
                            if (z!!.purpose == purpose){
                                l.ref.removeValue()
                            }
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@FinalReservationActivity, "$error occured", Toast.LENGTH_SHORT).show()
                    }

                })

                androidx.appcompat.app.AlertDialog.Builder(this).setMessage("Reservation deleted").setPositiveButton("OK", object :DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        startActivity(Intent(this@FinalReservationActivity, Home::class.java))
                        finish()
                    }

                }).show()

            }
        }
        else {

            name.text = sNo
            timed.text = "$date $time"
            purposed.text = purpose
            messaged.text = "Doctor's message: $message"
            approved.text = approval

            c.setOnClickListener {
                startActivity(Intent(this, DoctorsHome::class.java))
                finish()
            }


            if (approval == "Approved") {
                logout.setText("Make Diagnosis")

                logout.setOnClickListener {
                    val stdNo = sNo
                    val intent = Intent(this, DiagnosisActivity::class.java)
                    intent.putExtra("sNo", stdNo)
                    startActivity(intent)
                }
            }
            else {

                logout.setText("Approve Reservation")

                logout.setOnClickListener {
                    val editText = EditText(this)
                    editText.setTextColor(resources.getColor(R.color.black))
                     AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setTitle("MUBS E-HEALTH")
                        .setMessage("Approve/Disapprove Reservation based on Your schedules and availability")
                        .setView(editText)
                        .setPositiveButton("Approve", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {

                                val et = editText.text.toString()
                                if (et.isNotEmpty()) {
                                    FirebaseDatabase.getInstance().getReference()
                                        .child("/reservations")
                                        .child(docId!!).addValueEventListener(object :
                                            ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                for (y in snapshot.children) {
                                                    val z = y.getValue(Booking::class.java)


                                                    if (z!!.dId == docId && z.sNo == sNo && z.purpose == purpose && z.time == time) {
                                                        val k = y.key
                                                        FirebaseDatabase.getInstance()
                                                            .getReference()
                                                            .child("/reservations").child(docId)
                                                            .updateChildren(
                                                                mapOf(
                                                                    Pair(
                                                                        k!!,
                                                                        Booking(
                                                                            date!!,
                                                                            time!!,
                                                                            purpose!!,
                                                                            dName!!,
                                                                            docId,
                                                                            sNo!!,
                                                                            "Approved",
                                                                            et
                                                                        )
                                                                    )
                                                                )
                                                            )


                                                        FirebaseDatabase.getInstance().getReference("/notifications").child(sNo!!).push().setValue(Booking(
                                                            date!!,
                                                            time!!,
                                                            purpose!!,
                                                            dName!!,
                                                            docId,
                                                            sNo!!,
                                                            "Approved",
                                                            et
                                                        ))
                                                    }

                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {

                                            }

                                        })

                                    FirebaseDatabase.getInstance().getReference()
                                        .child("/reservations")
                                        .child(sNo!!).addValueEventListener(object :
                                            ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                for (y in snapshot.children) {
                                                    val z = y.getValue(Booking::class.java)


                                                    if (z!!.dId == docId && z.sNo == sNo && z.purpose == purpose && z.time == time) {
                                                        val k = y.key
                                                        FirebaseDatabase.getInstance()
                                                            .getReference()
                                                            .child("/reservations").child(sNo)
                                                            .updateChildren(
                                                                mapOf(
                                                                    Pair(
                                                                        k!!,
                                                                        Booking(
                                                                            date!!,
                                                                            time!!,
                                                                            purpose!!,
                                                                            dName!!,
                                                                            docId,
                                                                            sNo,
                                                                            "Approved",
                                                                            et
                                                                        )
                                                                    )
                                                                )
                                                            )


                                                    }

                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {

                                            }

                                        })
                                } else {
                                    FirebaseDatabase.getInstance().getReference()
                                        .child("/reservations")
                                        .child(docId!!).addValueEventListener(object :
                                            ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                for (y in snapshot.children) {
                                                    val z = y.getValue(Booking::class.java)


                                                    if (z!!.dId == docId && z.sNo == sNo && z.purpose == purpose && z.time == time) {
                                                        val k = y.key
                                                        FirebaseDatabase.getInstance()
                                                            .getReference()
                                                            .child("/reservations").child(docId)
                                                            .updateChildren(
                                                                mapOf(
                                                                    Pair(
                                                                        k!!,
                                                                        Booking(
                                                                            date!!,
                                                                            time!!,
                                                                            purpose!!,
                                                                            dName!!,
                                                                            docId,
                                                                            sNo!!,
                                                                            "Approved",
                                                                            message!!
                                                                        )
                                                                    )
                                                                )
                                                            )


                                                        FirebaseDatabase.getInstance().getReference("/notifications").child(sNo!!).push().setValue(Booking(
                                                            date!!,
                                                            time!!,
                                                            purpose!!,
                                                            dName!!,
                                                            docId,
                                                            sNo!!,
                                                            "Approved",
                                                            message
                                                        ))
                                                    }

                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {

                                            }

                                        })

                                    FirebaseDatabase.getInstance().getReference()
                                        .child("/reservations")
                                        .child(sNo!!).addValueEventListener(object :
                                            ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                for (y in snapshot.children) {
                                                    val z = y.getValue(Booking::class.java)


                                                    if (z!!.dId == docId && z.sNo == sNo && z.purpose == purpose && z.time == time) {
                                                        val k = y.key
                                                        FirebaseDatabase.getInstance()
                                                            .getReference()
                                                            .child("/reservations").child(sNo)
                                                            .updateChildren(
                                                                mapOf(
                                                                    Pair(
                                                                        k!!,
                                                                        Booking(
                                                                            date!!,
                                                                            time!!,
                                                                            purpose!!,
                                                                            dName!!,
                                                                            docId,
                                                                            sNo,
                                                                            "Approved",
                                                                            message!!
                                                                        )
                                                                    )
                                                                )
                                                            )

                                                    }

                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {

                                            }

                                        })

                                }
                            }

                        }).setNegativeButton("Disallow", object : DialogInterface.OnClickListener{
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                val et = editText.text.toString()
                                if (et.isNotEmpty()) {
                                    FirebaseDatabase.getInstance().getReference()
                                        .child("/reservations")
                                        .child(docId!!).addValueEventListener(object :
                                            ValueEventListener   {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                for (y in snapshot.children) {
                                                    val z = y.getValue(Booking::class.java)


                                                    if (z!!.dId == docId && z.sNo == sNo && z.purpose == purpose && z.time == time) {
                                                        val k = y.key
                                                        FirebaseDatabase.getInstance()
                                                            .getReference()
                                                            .child("/reservations").child(docId)
                                                            .updateChildren(
                                                                mapOf(
                                                                    Pair(
                                                                        k!!,
                                                                        Booking(
                                                                            date!!,
                                                                            time!!,
                                                                            purpose!!,
                                                                            dName!!,
                                                                            docId,
                                                                            sNo!!,
                                                                            "Rejected",
                                                                            et
                                                                        )
                                                                    )
                                                                )
                                                            )

                                                        FirebaseDatabase.getInstance().getReference("/notifications").child(sNo!!).push().setValue(Booking(
                                                            date!!,
                                                            time!!,
                                                            purpose!!,
                                                            dName!!,
                                                            docId,
                                                            sNo!!,
                                                            "Rejected",
                                                            et
                                                        ))
                                                    }

                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {

                                            }

                                        })

                                    FirebaseDatabase.getInstance().getReference()
                                        .child("/reservations")
                                        .child(sNo!!).addValueEventListener(object :
                                            ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                for (y in snapshot.children) {
                                                    val z = y.getValue(Booking::class.java)


                                                    if (z!!.dId == docId && z.sNo == sNo && z.purpose == purpose && z.time == time) {
                                                        val k = y.key
                                                        FirebaseDatabase.getInstance()
                                                            .getReference()
                                                            .child("/reservations").child(sNo)
                                                            .updateChildren(
                                                                mapOf(
                                                                    Pair(
                                                                        k!!,
                                                                        Booking(
                                                                            date!!,
                                                                            time!!,
                                                                            purpose!!,
                                                                            dName!!,
                                                                            docId,
                                                                            sNo,
                                                                            "Rejected",
                                                                            et
                                                                        )
                                                                    )
                                                                )
                                                            )

                                                    }

                                                }

                                                startActivity(Intent(this@FinalReservationActivity, DoctorsHome::class.java))
                                                finish()
                                            }

                                            override fun onCancelled(error: DatabaseError) {

                                            }

                                        })
                                }
                                else {
                                    FirebaseDatabase.getInstance().getReference()
                                        .child("/reservations")
                                        .child(docId!!).addValueEventListener(object :
                                            ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                for (y in snapshot.children) {
                                                    val z = y.getValue(Booking::class.java)


                                                    if (z!!.dId == docId && z.sNo == sNo && z.purpose == purpose && z.time == time) {
                                                        val k = y.key
                                                        FirebaseDatabase.getInstance()
                                                            .getReference()
                                                            .child("/reservations").child(docId)
                                                            .updateChildren(
                                                                mapOf(
                                                                    Pair(
                                                                        k!!,
                                                                        Booking(
                                                                            date!!,
                                                                            time!!,
                                                                            purpose!!,
                                                                            dName!!,
                                                                            docId,
                                                                            sNo!!,
                                                                            "Rejected",
                                                                            message!!
                                                                        )
                                                                    )
                                                                )
                                                            )

                                                        FirebaseDatabase.getInstance().getReference("/notifications").child(sNo!!).push().setValue(Booking(
                                                            date!!,
                                                            time!!,
                                                            purpose!!,
                                                            dName!!,
                                                            docId,
                                                            sNo!!,
                                                            "Rejected",
                                                            et
                                                        ))
                                                    }

                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {

                                            }

                                        })

                                    FirebaseDatabase.getInstance().getReference()
                                        .child("/reservations")
                                        .child(sNo!!).addValueEventListener(object :
                                            ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                for (y in snapshot.children) {
                                                    val z = y.getValue(Booking::class.java)


                                                    if (z!!.dId == docId && z.sNo == sNo && z.purpose == purpose && z.time == time) {
                                                        val k = y.key
                                                        FirebaseDatabase.getInstance()
                                                            .getReference()
                                                            .child("/reservations").child(sNo)
                                                            .updateChildren(
                                                                mapOf(
                                                                    Pair(
                                                                        k!!,
                                                                        Booking(
                                                                            date!!,
                                                                            time!!,
                                                                            purpose!!,
                                                                            dName!!,
                                                                            docId,
                                                                            sNo,
                                                                            "Approved",
                                                                            message!!
                                                                        )
                                                                    )
                                                                )
                                                            )
                                                    }

                                                }

                                                startActivity(Intent(this@FinalReservationActivity, DoctorsHome::class.java))
                                                finish()
                                            }

                                            override fun onCancelled(error: DatabaseError) {

                                            }

                                        })

                                }
                            }

                        }).show()

                }
            }
        }
    }
}