package com.example.mubsehealth

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import com.example.mubsehealth.doctors.DoctorReservation
import com.example.mubsehealth.model.Booking
import com.example.mubsehealth.model.PrefsManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dmax.dialog.SpotsDialog

class Home : AppCompatActivity() {
    lateinit var dialog: android.app.AlertDialog
    private val prefsManager = PrefsManager.INSTANCE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val sNo = prefsManager.getStudent().stdNo


        FirebaseDatabase.getInstance().getReference("/notifications").child(sNo!!).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for(i in snapshot.children){
                            val k = i.getValue(Booking::class.java)

                            val reqCode = 2
                            val intent = Intent(this@Home, Reservations::class.java)
                            showNotification(
                                this@Home,
                                "Your Reservation Response from Dr. ${k!!.dName}",
                                "Your reservation has been ${k.approval}",
                                intent,
                                reqCode
                            )

                        }

                        FirebaseDatabase.getInstance().getReference("/notifications").child(sNo).removeValue()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )




//        val b = findViewById<ImageView>(R.id.imageView2)
//        b.setOnClickListener {
//            startActivity(Intent(this, Login::class.java))
//            finish()
//        }

        val c = findViewById<CardView>(R.id.cardView)
        c.setOnClickListener {
            startActivity(Intent(this, Reservations::class.java))

        }

        val d = findViewById<CardView>(R.id.cardView2)
        d.setOnClickListener {
            startActivity(Intent(this, ConsultationsActivity::class.java))
            finish()
        }

        val doctors = findViewById<CardView>(R.id.doctors)
        doctors.setOnClickListener {
            startActivity(Intent(this, SelectDoctor::class.java))

        }

        val account = findViewById<CardView>(R.id.account)
        account.setOnClickListener {
            startActivity(Intent(this, MyAccount::class.java))

        }


    }

    fun showNotification(
        context: Context,
        title: String?,
        message: String?,
        intent: Intent?,
        reqCode: Int
    ) {
//        val sharedPreferenceManager: SharedPreferenceManager =
//            SharedPreferenceManager.getInstance(context)

        if (prefsManager.getStudent().stdNo != null){

            val pendingIntent =
                PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            val CHANNEL_ID = "${prefsManager.getStudent().stdNo}" // The id of the channel.
            val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
                context,
                CHANNEL_ID
            )
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name: CharSequence = "${prefsManager.getStudent().firstName}" // The user-visible name of the channel.
                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
                notificationManager.createNotificationChannel(mChannel)
            }
            notificationManager.notify(
                reqCode,
                notificationBuilder.build()
            ) // 0 is the request code, it should be unique id
            Log.d("showNotification", "showNotification: $reqCode")
        }
    }
}