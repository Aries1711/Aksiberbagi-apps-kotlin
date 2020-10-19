package com.inddevid.aksiberbagi_donatur.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.ModelPembayaran
import kotlinx.android.synthetic.main.pilih_pembayaran_row.view.*

class PilihPembayaranAdapter( val arrayList: ArrayList<ModelPembayaran>, val context: Context, val nominal:String) :
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
            val id : String = model.id
            val pTitle : String = model.title
            val nominalSet: String = nominal

//            val intent
//            val mIntent = Intent(this, ProgramDetailActivity::class.java)
//            val mBundle = Bundle()
//            mBundle.putString("dialogAktif", "true")
//            mBundle.putString("nominalDonasi", nominalSet)
//            mIntent.putExtras(mBundle)
//            startActivity(mIntent)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}