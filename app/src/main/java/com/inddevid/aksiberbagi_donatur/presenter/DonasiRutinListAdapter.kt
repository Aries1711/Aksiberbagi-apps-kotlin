package com.inddevid.aksiberbagi_donatur.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.ModelDonasiRutin
import com.inddevid.aksiberbagi_donatur.services.Preferences
import kotlinx.android.synthetic.main.card_donasi_rutin.view.*
import kotlinx.android.synthetic.main.card_donasi_saya.view.*
import kotlinx.android.synthetic.main.card_donasi_saya.view.donasiStatusframe

class DonasiRutinListAdapter(val arrayList: ArrayList<ModelDonasiRutin>, val context: Context) :
    RecyclerView.Adapter<DonasiRutinListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(210, 210)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

        fun bindItems(model: ModelDonasiRutin){
            // try glide image loading from url
            val modelUrl = model.imgProgram
            val url = "https://aksiberbagi.com/storage/program/$modelUrl"
            Glide.with(itemView.imageProgram.context).load(url).apply(options).into(itemView.imageProgram)
            itemView.judulProgram.text = model.judulProgram
            itemView.donasiRutinStatus.text = model.statusDonasi
            if (model.statusDonasi == "tidak aktif" || model.statusDonasi == "ditolak" ){
                itemView.donasiStatusframe.setBackgroundResource(R.drawable.rounded_donasi_download)
                itemView.donasiSayaStatus.setTextColor(Color.parseColor("#eb6b34"))
            }else{
                itemView.donasiStatusframe.setBackgroundResource(R.drawable.rounded_status_donasi)
                itemView.donasiSayaStatus.setTextColor(Color.parseColor("#15BBDA"))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_donasi_rutin, parent, false)
        return DonasiRutinListAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

        val sharedPreference: Preferences = Preferences(holder.itemView.context)
        holder.itemView.setOnClickListener{
            val model = arrayList[position]

        }
    }
}