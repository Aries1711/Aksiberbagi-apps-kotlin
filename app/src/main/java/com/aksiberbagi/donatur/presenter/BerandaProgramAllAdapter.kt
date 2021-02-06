package com.aksiberbagi.donatur.presenter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.model.BerandaProgramAll
import com.aksiberbagi.donatur.services.Preferences
import com.aksiberbagi.donatur.view.ProgramDetailActivity
import com.androchef.happytimer.utils.extensions.invisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
            itemView.progressBar.progress = model.progresProgram!!
            val donasi = model.fund
            itemView.fundProgramAll.text = "Rp $donasi"
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

        if (position == 14){
            holder.itemView.programAllLineList.invisible()
        }

        val sharedPreference: Preferences = Preferences(holder.itemView.context)
        holder.itemView.setOnClickListener {
            val model = arrayList[position]
            sharedPreference.save("idProgram", model.id)
            sharedPreference.save("img", model.img)
            sharedPreference.save("urlProgram", model.link)
            sharedPreference.save("judul", model.title)
            sharedPreference.save("penggalang", model.volunteer)
            sharedPreference.save("capaian", model.fund.toString())
            sharedPreference.save("sisaHari", model.dayFund)
            sharedPreference.save("tanggalMulai", model.startFund)
            sharedPreference.save("progressProgram", model.progresProgram)
            sharedPreference.save("targetProgram", model.targetNominal)
            sharedPreference.save("navigasi", model.poinAkses)
            val mIntent = Intent(context, ProgramDetailActivity::class.java)
            context.startActivity(mIntent)
        }
    }
}