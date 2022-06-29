package com.example.mubsehealth.model

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mubsehealth.CreateReservation
import com.example.mubsehealth.R
import com.example.mubsehealth.doctors.FinalReservationActivity

class BookingAdapter(r: Context, options: ArrayList<Booking>) : RecyclerView.Adapter<BookingAdapter.bookingViewHolder>(){
    var c=r
    var y = options
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookingAdapter.bookingViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.booking_list, parent, false)
        return BookingAdapter.bookingViewHolder(inflate)
    }

    class bookingViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(context: Context, date:String, name:String){

            val n = itemView.findViewById<TextView>(R.id.name)
            n.text = name

            val d = itemView.findViewById<TextView>(R.id.date)
            d.text = date

        }

    }

    override fun onBindViewHolder(holder: bookingViewHolder, position: Int) {
        Log.d("y", "$y")
        Log.d("c", "$c")
        holder.onBind(c, y[position].date!! + y[position].time,  y[position].purpose!!)

        holder.itemView.setOnClickListener {
            val intent = Intent(c, FinalReservationActivity::class.java)
            intent.putExtra("dName", y[position].dName )
            intent.putExtra("sNo", y[position].sNo)
            intent.putExtra("dId", y[position].dId)
            intent.putExtra("purpose", y[position].purpose )
            intent.putExtra("time", y[position].time)
            intent.putExtra("date", y[position].date)
            intent.putExtra("message", y[position].message)
            intent.putExtra("approval", y[position].approval)
            c.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return y.size
    }
}