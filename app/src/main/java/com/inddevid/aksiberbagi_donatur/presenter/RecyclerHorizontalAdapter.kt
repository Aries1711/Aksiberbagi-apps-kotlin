package com.inddevid.aksiberbagi_donatur.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.CardHorizontalRecycler
import kotlinx.android.synthetic.main.card_horizontal_program.view.*

class RecyclerHorizontalAdapter(val arrayList: ArrayList<CardHorizontalRecycler>, val context: Context) :
    RecyclerView.Adapter<RecyclerHorizontalAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(200, 130)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

        fun bindItems(model: CardHorizontalRecycler){
            itemView.cardHorizontalTitle.text = model.title
            itemView.cardHorizontalRelawan.text = model.volunteer
            itemView.cardHorizontalTerkumpulRupiah.text = model.moneyFund
            itemView.cardHorizontalSisaHariAngka.text = model.dayFund
            // try glide image loading from url
            Glide.with(itemView.imageProgram.context).load(model.img).apply(options).into(itemView.imageProgram)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_horizontal_program, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])
    }
}