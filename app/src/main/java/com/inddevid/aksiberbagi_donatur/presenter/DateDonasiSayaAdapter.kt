package com.inddevid.aksiberbagi_donatur.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.DateDonasiSaya
import kotlinx.android.synthetic.main.donasi_saya_week_date.view.*

class DateDonasiSayaAdapter (val arrayList: ArrayList<DateDonasiSaya>, val context:Context) :
    RecyclerView.Adapter<DateDonasiSayaAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView){
        fun bindItems(model:DateDonasiSaya){
            itemView.dayDonasiSaya.text = model.day
            itemView.dateDonasiSaya.text = model.date
            if( model.date == "23"){
                itemView.frameADS.setBackgroundResource(R.drawable.rounded_linear_layout)
                itemView.dateDonasiSaya.setTextColor(Color.parseColor("#ffffff"));
                itemView.dayDonasiSaya.setTextColor(Color.parseColor("#ffffff"));
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.donasi_saya_week_date, parent, false)
        return DateDonasiSayaAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])
    }
}