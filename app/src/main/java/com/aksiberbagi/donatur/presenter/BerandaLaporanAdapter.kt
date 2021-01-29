package com.aksiberbagi.donatur.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.model.BerandaLaporan
import kotlinx.android.synthetic.main.beranda_card_laporan.view.*

class BerandaLaporanAdapter (val arrayList: ArrayList<BerandaLaporan>, val context:Context) :
    RecyclerView.Adapter<BerandaLaporanAdapter.ViewHolder>(){
    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        //aturan untuk mengambil gambar etc resolusi dll
        private val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(210, 140)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)
        //fungsi untuk mengikat model dengan view yang akan ditampilkan pada recyclerView
        fun bindItems(model: BerandaLaporan){
            Glide.with(itemView.imageLaporan.context).load(model.img).apply(options).into(itemView.imageLaporan)
            itemView.titleProgramLaporan.text = model.titleReport
            itemView.lokasiLaporan.text = model.location
            itemView.tanggalLaporan.text = model.dayReport
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.beranda_card_laporan, parent, false )
        return BerandaLaporanAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])
    }
}