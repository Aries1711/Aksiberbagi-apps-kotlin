package com.aksiberbagi.donatur.presenter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.model.CardDonasiSaya
import com.aksiberbagi.donatur.services.Converter
import com.aksiberbagi.donatur.services.Preferences
import com.aksiberbagi.donatur.view.DonasiDetailActivity
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
//            val titleDonasi = model.title
//            titleDonasi!!.chunked(6)
//            println(titleDonasi)
            itemView.cardTitleDonasiSaya.text = model.title
            itemView.paymentDonasiSaya.text = model.payment
            itemView.waktuDonasiSaya.text = model.timePay
            val nominalFormated = Converter.ribuan(model.moneyPay!!.toDouble())
            itemView.donasiRupiah.text = "Rp $nominalFormated"
            itemView.donasiSayaStatus.text = model.status
            if (model.status == "GAGAL" || model.status == "EXPIRED" ){
                itemView.donasiStatusframe.setBackgroundResource(R.drawable.rounded_donasi_download)
                itemView.donasiSayaStatus.setTextColor(Color.parseColor("#eb6b34"))
            }else{
                itemView.donasiStatusframe.setBackgroundResource(R.drawable.rounded_status_donasi)
                itemView.donasiSayaStatus.setTextColor(Color.parseColor("#15BBDA"))
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

        val sharedPreference: Preferences = Preferences(holder.itemView.context)
        holder.itemView.setOnClickListener{
            val model = arrayList[position]
            sharedPreference.save("idDonasiDetail", model.idDonasi)
            sharedPreference.save("detailDonasiWaktu", model.timePay)
            sharedPreference.save("detailDonasiPembayaran", model.payment)
            sharedPreference.save("detailDonasiNominal", model.moneyPay)
            sharedPreference.save("detailDonasiJenis", model.jenisDonasi)
            sharedPreference.save("detailDonasiStatus", model.status)
            val mIntent = Intent(context, DonasiDetailActivity::class.java)
            context.startActivity(mIntent)
        }
    }
}