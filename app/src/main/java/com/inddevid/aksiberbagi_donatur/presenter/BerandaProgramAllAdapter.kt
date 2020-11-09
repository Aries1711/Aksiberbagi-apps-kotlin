package com.inddevid.aksiberbagi_donatur.presenter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androchef.happytimer.utils.extensions.invisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.BerandaProgramAll
import com.inddevid.aksiberbagi_donatur.services.Converter
import com.inddevid.aksiberbagi_donatur.services.Preferences
import com.inddevid.aksiberbagi_donatur.view.ProgramDetailActivity
import kotlinx.android.synthetic.main.beranda_card_program_all.view.*

class BerandaProgramAllAdapter (val arrayList: ArrayList<BerandaProgramAll>, val context:Context) :
    RecyclerView.Adapter<BerandaProgramAllAdapter.ViewHolder> (){

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(220, 175)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

        fun bindItems(model: BerandaProgramAll){
            Glide.with(itemView.imageProgramAll.context).load(model.img).apply(options).into(itemView.imageProgramAll)
            itemView.titleProgramAll.text = model.title
            itemView.volunteerProgamAll.text = model.volunteer
            itemView.fundProgramAll.text = Converter.rupiah(model.fund)
//            itemView.dayProgramAll.text = model.dayFund
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BerandaProgramAllAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.beranda_card_program_all, parent, false)
        return BerandaProgramAllAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: BerandaProgramAllAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])
        if (position == 4){
            holder.itemView.programAllLineList.invisible()
        }

        val sharedPreference: Preferences = Preferences(holder.itemView.context)
        holder.itemView.setOnClickListener {
            val model = arrayList[position]
            sharedPreference.save("idProgram", model.id)
            sharedPreference.save("img", model.img)
            sharedPreference.save("judul", model.title)
            sharedPreference.save("capaian", model.fund.toString())
            sharedPreference.save("sisaHari", model.dayFund)
            sharedPreference.save("tanggalMulai", model.startFund)
            sharedPreference.save("progresProgram", model.progresProgram)
            sharedPreference.save("targetProgram", model.targetNominal)
            val mIntent = Intent(context, ProgramDetailActivity::class.java)
            context.startActivity(mIntent)
        }
    }
}