package com.aksiberbagi.donatur.presenter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.model.BerandaLelang
import com.aksiberbagi.donatur.services.Converter
import com.aksiberbagi.donatur.services.Preferences
import com.aksiberbagi.donatur.view.LelangActivity
import kotlinx.android.synthetic.main.beranda_card_lelang.view.progressBar
import kotlinx.android.synthetic.main.card_lelang_list.view.*

class ListLelangAdapter(val arrayList: ArrayList<BerandaLelang>, val context: Context) :
    RecyclerView.Adapter<ListLelangAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(210, 170)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)
        fun bindItems(model: BerandaLelang){
            Glide.with(itemView.imageLelangList.context).load(model.imgLelang).apply(options).into(itemView.imageLelangList)
            itemView.namaLelang.text = model.judulLelang
            val stokNow = model.stokSekarang!!.toDouble()
            val stokEarly = model.stokAwal!!.toDouble()
            var terdonasi = model.stokAwal!! - model.stokSekarang!!
            model.terdonasi = terdonasi.toString()
            val progresRaw :Double  = stokNow / stokEarly * 100
            val progresReal = 100 - progresRaw
            itemView.progressBar.progress = progresReal.toInt()
            var sisa:Int = model.stokSekarang!!
            itemView.terdonasiItemText.text = "Tersisa $sisa"
            itemView.hargaLelangList.text = Converter.rupiah(model.hargaLelang)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_lelang_list, parent, false)
        return ListLelangAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

        val sharedPreference: Preferences = Preferences(holder.itemView.context)

        holder.itemView.donasiBtn.setOnClickListener{
            var model = arrayList[position]
            sharedPreference.save("idLelang", model.idLelang)
            sharedPreference.save("judulLelang", model.judulLelang)
            sharedPreference.save("imgLelang", model.imgLelang)
            sharedPreference.save("terdonasiLelang", model.terdonasi)
            sharedPreference.save("nominalLelang", Converter.rupiah(model.hargaLelang))
            sharedPreference.save("idLelangProgram", model.idLelangProgram)
            sharedPreference.save("judulLelangProgram", model.judulLelangProgram)
            val mIntent = Intent(context, LelangActivity::class.java)
            context.startActivity(mIntent)
        }

        holder.itemView.setOnClickListener {
            var model = arrayList[position]
            sharedPreference.save("idLelang", model.idLelang)
            sharedPreference.save("judulLelang", model.judulLelang)
            sharedPreference.save("imgLelang", model.imgLelang)
            sharedPreference.save("terdonasiLelang", model.terdonasi)
            sharedPreference.save("nominalLelang", Converter.rupiah(model.hargaLelang))
            sharedPreference.save("idLelangProgram", model.idLelangProgram)
            sharedPreference.save("judulLelangProgram", model.judulLelangProgram)
            val mIntent = Intent(context, LelangActivity::class.java)
            context.startActivity(mIntent)

        }
    }

}