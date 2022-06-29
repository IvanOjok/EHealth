package com.example.mubsehealth.model

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mubsehealth.R

class ChatAdapter(context: Context, options: ArrayList<Chat>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var c = context
    var y = options

//    constructor(context: Context, options: ArrayList<Chat>) : this() {
//       this.c = context
//       this.y = options
//
//    }

    var type = 1
    var received = 2

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        var inflate: View ?= null

//        if (chat != null){
//            val status = chat.status
//            if (status == "Sent"){
//               inflate = LayoutInflater.from(parent.context).inflate(R.layout.to_message_layout, parent, false)
//            }
//            else if (status == "received"){
             //   inflate = LayoutInflater.from(parent.context).inflate(R.layout.from_message_layout, parent, false)
     //       }
       // }

        if (viewType == type){
            return MessageOutVH(LayoutInflater.from(c).inflate(R.layout.to_message_layout, parent, false))
        }
       return MessageInVH(LayoutInflater.from(c).inflate(R.layout.from_message_layout, parent, false))
    }

   inner class MessageInVH( itemView: View): RecyclerView.ViewHolder(itemView){
        fun onBind(position: Int){
            val n = itemView.findViewById<TextView>(R.id.message)
            n.text = y[position].message

            val d = itemView.findViewById<TextView>(R.id.time)
            d.text = y[position].time

        }
    }

   inner class MessageOutVH( itemView: View): RecyclerView.ViewHolder(itemView){
        fun onBind(position: Int){

            //val c = ChatAdapter().y!!.get(position)
            val n = itemView.findViewById<TextView>(R.id.to_message)
            n.text = y[position].message

            val d = itemView.findViewById<TextView>(R.id.to_time)
            d.text = y[position].time

        }
    }

    class ChatViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(context: Context, message:String, time:String, position: Int){

            val n = itemView.findViewById<TextView>(R.id.message)
            n.text = message

            val d = itemView.findViewById<TextView>(R.id.time)
            d.text = time

        }

    }

//    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
//        Log.d("y", "$y")
//        Log.d("c", "$c")
//        holder.onBind(c, y[position].message!! , y[position].time!!)
//
//        if (y[position].status == "sent"){
//
//        }
//
////        holder.itemView.setOnClickListener {
////            val intent = Intent(c, CreateReservation::class.java)
////            intent.putExtra("dId", y[position].id )b
////            intent.putExtra("dPhone", y[position].phone)
////            c.startActivity(intent)
////        }
//
//    }

    override fun getItemCount(): Int {
        return y.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val list = y.get(position)
        if (list.status == "Sent"){
            val h:MessageOutVH ?= null
            Log.d("h", h.toString())
            Log.d("message", list.message!!)
            Log.d("time", list.time!!)

             (holder as MessageOutVH).onBind(position)
        }
        else if (list.status == "Received"){
            val h:MessageInVH ?= null
            Log.d("message", list.message!!)
            Log.d("time", list.time!!)
            (holder as MessageInVH).onBind(position)

        }
    }

    override fun getItemViewType(position: Int): Int {
        val z = y[position].status
        var viewType: Int = 0
        if (z == "Received"){
            viewType = 0
        }
        else{
            viewType = 1
        }
        return viewType
    }
}