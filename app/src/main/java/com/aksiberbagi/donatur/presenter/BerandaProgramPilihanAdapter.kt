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
import com.aksiberbagi.donatur.model.BerandaProgramPilihan
import com.aksiberbagi.donatur.services.Preferences
import com.aksiberbagi.donatur.view.ProgramDetailActivity
import kotlinx.android.synthetic.main.beranda_card_program_pilihan.view.*

class BerandaProgramPilihanAdapter (val arrayList: ArrayList<BerandaProgramPilihan>, val context: Context) :
    RecyclerView.Adapter<BerandaProgramPilihanAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

//        aturan untuk mengambil gambar etc resolusi dll
        private val options: RequestOptions = RequestOptions()
        .centerCrop()
        .override(280, 140)
        .placeholder(R.mipmap.ic_launcher_round)
        .error(R.mipmap.ic_launcher_round)
//        fungsi untuk mengikat model dengan view yang akan ditampilkan pada recyclerView

        fun bindItems(model: BerandaProgramPilihan){
            Glide.with(itemView.imageProgramPilihan.context).load(model.img).apply(options).into(itemView.imageProgramPilihan)
            itemView.titleProgramPilihan.text = model.titleChoose
            val donasi = model.fund
            itemView.terkumpulCardPilihan.text = "Rp $donasi"
            itemView.progressBar.progress = model.progress!!
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
        val sharedPreference: Preferences = Preferences(holder.itemView.context)
        holder.itemView.setOnClickListener {
            val model = arrayList[position]
            sharedPreference.save("idProgram", model.id)
            sharedPreference.save("img", model.img)
            sharedPreference.save("urlProgram", model.link)
            sharedPreference.save("judul", model.titleChoose)
            sharedPreference.save("capaian", model.fund)
            sharedPreference.save("sisaHari", model.dayFund)
            sharedPreference.save("tanggalMulai", model.dateStart)
            sharedPreference.save("progressProgram", model.progress)
            sharedPreference.save("targetProgram", model.targetNominal)
            sharedPreference.save("navigasi", model.poinAkses)
            val mIntent = Intent(context, ProgramDetailActivity::class.java)
            context.startActivity(mIntent)
        }
    }
}