package com.inddevid.aksiberbagi_donatur.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.BerandaProgramPilihan
import kotlinx.android.synthetic.main.beranda_card_program_pilihan.view.*

class BerandaProgramPilihanAdapter (val arrayList: ArrayList<BerandaProgramPilihan>, val context: Context) :
    RecyclerView.Adapter<BerandaProgramPilihanAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

//        aturan untuk mengambil gambar etc resolusi dll
        private val options: RequestOptions = RequestOptions()
        .centerCrop()
        .override(275, 135)
        .placeholder(R.mipmap.ic_launcher_round)
        .error(R.mipmap.ic_launcher_round)
//        fungsi untuk mengikat model dengan view yang akan ditampilkan pada recyclerView

        fun bindItems(model: BerandaProgramPilihan){
            Glide.with(itemView.imageProgramPilihan.context).load(model.img).apply(options).into(itemView.imageProgramPilihan)
            itemView.titleProgramPilihan.text = model.titleChoose
            itemView.terkumpulCardPilihan.text = model.fund
            itemView.sisaDayCardPilihan.text = model.dayFund

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.beranda_card_program_pilihan, parent, false )
        return BerandaProgramPilihanAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])
    }
}