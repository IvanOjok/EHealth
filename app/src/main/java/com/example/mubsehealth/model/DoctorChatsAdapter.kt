package com.example.mubsehealth.model

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mubsehealth.R
import com.example.mubsehealth.doctors.DocChatMessages

class DoctorChatsAdapter(r: Context, options: ArrayList<String>) : RecyclerView.Adapter<DoctorChatsAdapter.ChatViewHolder>() {
    var c = r
    var y = options
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.doctors_list, parent, false)
        return ChatViewHolder(inflate)
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(context: Context, name: String) {

            val n = itemView.findViewById<TextView>(R.id.name)
            n.text = name

//            val d = itemView.findViewById<TextView>(R.id.date)
//            d.text = date


        }

    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        Log.d("y", "$y")
        Log.d("c", "$c")
        holder.onBind(c, y[position])

        holder.itemView.setOnClickListener {
            val intent = Intent(c, DocChatMessages::class.java)
            intent.putExtra("name", y[position])
            c.startActivity(intent)
        }

//        holder.itemView.setOnClickListener {
//            val intent = Intent(c, CreateReservation::class.java)
//            intent.putExtra("dId", y[position].id )
//            intent.putExtra("dPhone", y[position].phone)
//            c.startActivity(intent)
//        }

    }

    override fun getItemCount(): Int {
        return y.size
    }
}