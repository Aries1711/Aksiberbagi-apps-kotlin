package com.inddevid.aksiberbagi_donatur.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.BerandaProgramAll
import kotlinx.android.synthetic.main.beranda_card_program_all.view.*

class BerandaProgramAllAdapter (val arrayList: ArrayList<BerandaProgramAll>, val context:Context) :
    RecyclerView.Adapter<BerandaProgramAllAdapter.ViewHolder> (){

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(220, 157)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

        fun bindItems(model: BerandaProgramAll){
            Glide.with(itemView.imageProgramAll.context).load(model.img).apply(options).into(itemView.imageProgramAll)
            itemView.titleProgramAll.text = model.title
            itemView.summaryProgramAll.text = model.summary
            itemView.volunteerProgamAll.text = model.volunteer
            itemView.fundProgramAll.text = model.fund
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
    }
}