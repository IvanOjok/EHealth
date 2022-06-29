package com.example.mubsehealth.doctors

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import com.example.mubsehealth.MainActivity
import com.example.mubsehealth.R
import com.example.mubsehealth.model.Booking
import com.example.mubsehealth.model.PrefsManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dmax.dialog.SpotsDialog


class DoctorsHome : AppCompatActivity() {
    lateinit var dialog: android.app.AlertDialog
    private val prefsManager = PrefsManager.INSTANCE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctors_home)

        val dId = prefsManager.getDoctor().id
        FirebaseDatabase.getInstance().getReference("/notifications").child(dId!!).addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for(i in snapshot.children){
                            val k = i.getValue(Booking::class.java)

//                            val notif =
//                                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//                            val notify: Notification =  Notification.Builder(applicationContext).setContentTitle("Reservation")
//                                    .setContentText(k!!.purpose).setContentTitle(k.sNo)
//                                    .setSmallIcon(R.drawable.logo).build()
//
//                            notify.flags = notify.flags or Notification.FLAG_AUTO_CANCEL
//                            notif.notify(0, notify)


                            val reqCode = 1
                            val intent = Intent(this@DoctorsHome, DoctorReservation::class.java)
                            showNotification(
                                this@DoctorsHome,
                                "A Reservation has been created by ${k!!.sNo}",
                                "Purpose: ${k.purpose}",
                                intent,
                                reqCode
                            )

                        }

                        FirebaseDatabase.getInstance().getReference("/notifications").child(dId).removeValue()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )

        val c = findViewById<CardView>(R.id.cardView)
        c.setOnClickListener {
            startActivity(Intent(this, DoctorReservation::class.java))

        }

        val d = findViewById<CardView>(R.id.cardView2)
        d.setOnClickListener {
            startActivity(Intent(this, DoctorChats::class.java))
            finish()
        }

        val doctors = findViewById<CardView>(R.id.doctors)
        doctors.setOnClickListener {
            startActivity(Intent(this, ApprovedDiagnosis::class.java))

        }


        val account = findViewById<CardView>(R.id.account)
        account.setOnClickListener {
            startActivity(Intent(this, DoctorAccountInfo::class.java))

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

        if (prefsManager.getDoctor().doctorNo != null){

        val pendingIntent =
            PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val CHANNEL_ID = "${prefsManager.getDoctor().doctorNo}" // The id of the channel.
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
            val name: CharSequence = "${prefsManager.getDoctor().firstName}" // The user-visible name of the channel.
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