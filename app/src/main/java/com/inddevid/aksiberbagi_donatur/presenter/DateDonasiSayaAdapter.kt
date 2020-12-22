package com.inddevid.aksiberbagi_donatur.presenter

import android.content.Context
import android.graphics.Color
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
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

            // pengkondisian view untuk tanggal yang telah terlewati dan ada donasi
            for(i in 0 until model.dayDonasi.size){
                if(model.date == model.dayDonasi[i]){
                    itemView.dateDonasiSaya.setBackgroundResource(R.drawable.date_donasi)
                    itemView.dateDonasiSaya.setTextColor(Color.parseColor("#F389EC"));
                    itemView.dayDonasiSaya.setTextColor(Color.parseColor("#F389EC"));
                }
            }

            // pengkondisian view untuk tanggal pada hari ini
            if( model.date == model.dateNow ){
                for(i in 0 until model.dayDonasi.size) {
                    if (model.date == model.dayDonasi[i]) {
                        itemView.frameADS.setBackgroundResource(R.drawable.rounded_linear_layout)
                        itemView.dateDonasiSaya.setBackgroundResource(R.drawable.date_donasi_today)
                        itemView.dateDonasiSaya.setTextColor(Color.parseColor("#ffffff"));
                        itemView.dayDonasiSaya.setTextColor(Color.parseColor("#ffffff"));
                    }
                }
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
        holder.itemView.setOnClickListener{
            val model = arrayList[position]
            if(model.date == model.dateNow){
                val toast = Toast.makeText(
                    holder.itemView.context,
                    "Maaf, data tanggal tidak valid",
                    Toast.LENGTH_LONG
                )
                toast.show()
            }else{
                //val activity = getActivity(holder.itemView.context)
                dialogPanel(model.date, holder.itemView.context)

            }
        }
    }

    private fun dialogPanel(date: String, context: Context){
        var dialog = DialogPengingatDonasi(date)
        val fragment = getFragmentManager(context)
        fragment!!.let { dialog.show(it, "dialogPengingat") }
    }

    fun getFragmentManager(context: Context?): FragmentManager? {
        return when (context) {
            is AppCompatActivity -> context.supportFragmentManager
            is ContextThemeWrapper -> getFragmentManager(context.baseContext)
            else -> null
        }
    }

}