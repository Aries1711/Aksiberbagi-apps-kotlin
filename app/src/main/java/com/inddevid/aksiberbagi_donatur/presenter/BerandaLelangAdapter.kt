package com.inddevid.aksiberbagi_donatur.presenter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.BerandaLelang
import com.inddevid.aksiberbagi_donatur.services.Converter
import com.inddevid.aksiberbagi_donatur.services.Preferences
import com.inddevid.aksiberbagi_donatur.view.LelangActivity
import kotlinx.android.synthetic.main.beranda_card_lelang.view.*

class BerandaLelangAdapter (val arrayList: ArrayList<BerandaLelang>, val context: Context) :
    RecyclerView.Adapter<BerandaLelangAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(210, 140)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)
        fun bindItems(model: BerandaLelang){
            Glide.with(itemView.imageLelang.context).load(model.imgLelang).apply(options).into(itemView.imageLelang)
            itemView.progressBar.progress = model.progress!!
            var sisa:Int = model.progress!!
            itemView.textInsideprogressBar.text = "Tersisa $sisa"
            itemView.hargaLelang.text = Converter.rupiah(model.hargaLelang)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.beranda_card_lelang, parent, false)
        return BerandaLelangAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

        val sharedPreference: Preferences = Preferences(holder.itemView.context)
        holder.itemView.setOnClickListener {
            var model = arrayList[position]
            sharedPreference.save("idLelang", model.idLelang)
            sharedPreference.save("judulLelang", model.judulLelang)
            sharedPreference.save("imgLelang", model.imgLelang)
            sharedPreference.save("nominalLelang", Converter.rupiah(model.hargaLelang))
            sharedPreference.save("idLelangProgram", model.idLelangProgram)
            sharedPreference.save("judulLelangProgram", model.judulLelangProgram)
            val mIntent = Intent(context, LelangActivity::class.java)
            context.startActivity(mIntent)

        }
    }

}