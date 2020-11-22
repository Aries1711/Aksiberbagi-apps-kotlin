package com.inddevid.aksiberbagi_donatur.presenter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.ModelPembayaran
import com.inddevid.aksiberbagi_donatur.services.Preferences
import com.inddevid.aksiberbagi_donatur.view.ProgramDetailActivity
import kotlinx.android.synthetic.main.pilih_pembayaran_row.view.*

class PilihPembayaranAdapter( val arrayList: ArrayList<ModelPembayaran>, val context: Context, val nominal:String, val spinner:String, var tipeBank: String) :
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
                val sharedPreference: Preferences = Preferences(holder.itemView.context)
                val model = arrayList[position]
                val id: String? = model.id
                val pTitle: String? = model.title
                val imgPilihan: String? = model.img
                var nominalSet: String = nominal
                val tipeSet: String = tipeBank
                val spinnerSet: String = spinner

                if(nominalSet == ""){
                    nominalSet = "0"
                }
                if (nominalSet.toInt() in 1000..9999 && tipeBank == "Transfer") {
                    val toast = Toast.makeText(
                        holder.itemView.context,
                        "Mohon Maaf Metode pembayaran tidak Valid",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                } else {
                    sharedPreference.save("idPembayaran", id)

                    val mIntent = Intent(context, ProgramDetailActivity::class.java)
                    val mBundle = Bundle()
                    mBundle.putString("dialogAktif", "true")
                    mBundle.putString("idPilihan", id)
                    mBundle.putString("pilihanPembayaran", pTitle)
                    mBundle.putString("imagePilihan", imgPilihan)
                    mBundle.putString("tipeBank", tipeSet)
                    mBundle.putString("nominalDonasi", nominalSet)
                    mBundle.putString("spinner", spinnerSet)
                    mIntent.putExtras(mBundle)
                    context.startActivity(mIntent)
                }

            }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}