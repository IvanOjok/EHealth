package com.example.mubsehealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mubsehealth.model.Chat
import com.example.mubsehealth.model.ChatAdapter
import com.example.mubsehealth.model.PrefsManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class ChatMessageActivity : AppCompatActivity() {

    private val prefsManager = PrefsManager.INSTANCE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_message)

        prefsManager.setContext(this.application)

        val name = intent.getStringExtra("name")
        val dName = findViewById<TextView>(R.id.dName)
        dName.text = name

        val dId = intent.getStringExtra("idNo")
        val uid = prefsManager.getStudent().id

        val rView = findViewById<RecyclerView>(R.id.chats)
        rView.layoutManager = LinearLayoutManager(this)
        //val o = FirebaseRecyclerOptions.Builder<Doctor>().setQuery(dbRef, Doctor::class.java).build()

        val p = ArrayList<Chat>()
        val dbRef = FirebaseDatabase.getInstance().getReference("/chats").child(uid!!).child(dId!!).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (y in snapshot.children){
                    Log.d("y", "$y")
                    val z = y.getValue(Chat::class.java)
                    Log.d("z", "${z!!.message}")
                    p.add(z)
                    Log.d("p", "$p")
                }
                val adapter = ChatAdapter(this@ChatMessageActivity, p)
                rView.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatMessageActivity, "An $error occured", Toast.LENGTH_LONG).show()
            }
        })

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, ConsultingDoctor::class.java))
            finish()
        }

        val message = findViewById<EditText>(R.id.message)
        val send = findViewById<ImageView>(R.id.send)

        if (message.text.toString() == ""){
            send.setBackgroundColor(resources.getColor(R.color.white))
        }
        else{
            send.setBackgroundColor(resources.getColor(R.color.blue))
        }

        send.setOnClickListener {
            if (message.text.toString() == ""){
                return@setOnClickListener
            }
            else{

                val db = FirebaseDatabase.getInstance()
                //val id = UUID.randomUUID().toString()

                val time = Calendar.getInstance().time.toString()

                val ref = db.getReference("/chats").child(uid).child(dId).push()
                val key = ref.key
                val chat = Chat(key, message.text.toString(), time, "Sent")
                val insert = ref.setValue(chat)

                val receiveRef =  db.getReference("/chats").child(dId).child(uid).child(key!!)
                val rChat = Chat(key, message.text.toString(), time, "Received")
                val rInsert = receiveRef.setValue(rChat)

                if (insert.isCanceled || rInsert.isCanceled){
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show()
                }
                else{
                    startActivity(getIntent())
                    finish()
                }

            }
        }

    }
}