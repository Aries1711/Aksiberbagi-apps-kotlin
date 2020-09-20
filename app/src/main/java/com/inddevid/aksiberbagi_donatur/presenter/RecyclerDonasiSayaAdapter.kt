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
import kotlinx.android.synthetic.main.card_donasi_saya.view.*

class RecyclerDonasiSayaAdapter (val arrayList: ArrayList<CardDonasiSaya>, val context: Context) :
    RecyclerView.Adapter<RecyclerDonasiSayaAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        private val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(200, 130)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

        fun bindItems(model: CardDonasiSaya){
            itemView.cardTitleDonasiSaya.text = model.title
            itemView.paymentDonasiSaya.text = model.payment
            itemView.waktuDonasiSaya.text = model.timePay
            itemView.donasiRupiah.text = model.moneyPay
            itemView.donasiSayaStatus.text = model.status
            // try glide image loading from url
            Glide.with(itemView.imageDonasiSayaCard.context).load(model.img).apply(options).into(itemView.imageDonasiSayaCard)
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