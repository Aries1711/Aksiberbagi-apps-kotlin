package com.inddevid.aksiberbagi_donatur.presenter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.ModelPembayaran
import com.inddevid.aksiberbagi_donatur.view.ProgramDetailActivity
import kotlinx.android.synthetic.main.pilih_pembayaran_row.view.*
import android.content.Intent as Intent1

class PilihPembayaranAdapter( val arrayList: ArrayList<ModelPembayaran>, val context: Context, val nominal:String, val spinner:String) :
    RecyclerView.Adapter<PilihPembayaranAdapter.ViewHolder> () {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItems(model: ModelPembayaran){
            Glide.with(itemView.imagePembayaran.context).load(model.img).into(itemView.imagePembayaran)
            itemView.titlePembayaran.text = model.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.pilih_pembayaran_row, parent, false)
        return PilihPembayaranAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

        holder.itemView.setOnClickListener {
            val model = arrayList[position]
            val id : String? = model.id
            val pTitle : String? = model.title
            val imgPilihan : String? = model.img
            val nominalSet: String = nominal
            val spinnerSet: String = spinner

            val mIntent = Intent1(context, ProgramDetailActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("dialogAktif", "true")
            mBundle.putString("pilihanPembayaran", pTitle)
            mBundle.putString("imagePilihan", imgPilihan)
            mBundle.putString("nominalDonasi", nominalSet)
            mBundle.putString("spinner", spinnerSet)
            mIntent.putExtras(mBundle)
            context.startActivity(mIntent)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}