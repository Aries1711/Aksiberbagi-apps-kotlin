package com.inddevid.aksiberbagi_donatur.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.CardDonasiSaya
import com.inddevid.aksiberbagi_donatur.services.Converter
import kotlinx.android.synthetic.main.card_donasi_saya.view.*

class RecyclerDonasiSayaAdapter (val arrayList: ArrayList<CardDonasiSaya>, val context: Context) :
    RecyclerView.Adapter<RecyclerDonasiSayaAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        private val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(210, 210)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

        fun bindItems(model: CardDonasiSaya){
            // try glide image loading from url
            val modelUrl = model.img
            val url = "https://aksiberbagi.com/storage/program/$modelUrl"
            Glide.with(itemView.imageDonasiSayaCard.context).load(url).apply(options).into(itemView.imageDonasiSayaCard)
            itemView.cardTitleDonasiSaya.text = model.title
            itemView.paymentDonasiSaya.text = model.payment
            itemView.waktuDonasiSaya.text = model.timePay
            val nominalFormated = Converter.ribuan(model.moneyPay!!.toDouble())
            itemView.donasiRupiah.text = "Rp $nominalFormated"
            itemView.donasiSayaStatus.text = model.status
            if (model.status != "SUKSES"){
                itemView.downloadPDf.visibility = View.GONE
            }else{
                itemView.downloadPDf.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_donasi_saya, parent, false)
        return RecyclerDonasiSayaAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])
    }
}