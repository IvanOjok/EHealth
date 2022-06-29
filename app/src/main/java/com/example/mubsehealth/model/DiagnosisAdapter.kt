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
import com.example.mubsehealth.doctors.DiagnosisReport

class DiagnosisAdapter (r: Context, options: ArrayList<Diagnosis>) : RecyclerView.Adapter<DiagnosisAdapter.diagnosisViewHolder>(){
    var c=r
    var y = options
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): diagnosisViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.booking_list, parent, false)
        return diagnosisViewHolder(inflate)
    }

    class diagnosisViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(context: Context, result:String, name:String){

            val n = itemView.findViewById<TextView>(R.id.name)
            n.text = result

            val d = itemView.findViewById<TextView>(R.id.date)
            d.text = "Approved By $name"


        }

    }

    override fun onBindViewHolder(holder: diagnosisViewHolder, position: Int) {
        Log.d("y", "$y")
        Log.d("c", "$c")
        holder.onBind(c, y[position].result!!,  y[position].dName!!)

        holder.itemView.setOnClickListener {
            val intent = Intent(c, DiagnosisReport::class.java)
            intent.putExtra("dName", y[position].dName )
            intent.putExtra("sNo", y[position].sNo)
            intent.putExtra("dId", y[position].dId)
            intent.putExtra("result", y[position].result )
            intent.putExtra("recommendation", y[position].recommendation)
            intent.putExtra("prescription", y[position].prescription)

            c.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return y.size
    }
}