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

class BookDoctorAdapter (r: Context, options: ArrayList<Doctor>) : RecyclerView.Adapter<BookDoctorAdapter.bookDoctorViewHolder>(){
    var c=r
    var y = options
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): bookDoctorViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.doctors_list, parent, false)
        return bookDoctorViewHolder(inflate)
    }

    class bookDoctorViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(context: Context, image:String, name:String){

            val i = itemView.findViewById<ImageView>(R.id.imageView3)

            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .error(R.drawable.ic_baseline_account_circle_24)

            Glide.with(context).load(image).apply(options).into(i)
            val n = itemView.findViewById<TextView>(R.id.name)
            n.text = name
        }

    }

    override fun onBindViewHolder(holder: bookDoctorViewHolder, position: Int) {
        Log.d("y", "$y")
        Log.d("c", "$c")
        holder.onBind(c, y[position].imgUrl!!, y[position].firstName!! + y[position].lastName!!)

        holder.itemView.setOnClickListener {
            val intent = Intent(c, CreateReservation::class.java)
            intent.putExtra("dId", y[position].id)
            intent.putExtra("name", y[position].firstName!! + y[position].lastName!!)
            intent.putExtra("dPhone", y[position].phone)

            intent.putExtra("img", y[position].imgUrl)
            intent.putExtra("email", y[position].email)
            intent.putExtra("prof", y[position].profession)
            intent.putExtra("idNo", y[position].doctorNo)
            c.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return y.size
    }
}