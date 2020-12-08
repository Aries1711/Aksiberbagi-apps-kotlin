package com.inddevid.aksiberbagi_donatur.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.ListDonasi
import com.inddevid.aksiberbagi_donatur.services.Converter
import kotlinx.android.synthetic.main.donasi_program_item.view.*

class ListDonasiAdapter (val arrayList: ArrayList<ListDonasi>, val context: Context) :
    RecyclerView.Adapter<ListDonasiAdapter.ViewHolder>(){
    class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        private val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(100, 100)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.drawable.pengguna)

        fun bindItems(model: ListDonasi){
            Glide.with(itemView.imageDonatur.context).load(model.imgDonatur).apply(options).into(itemView.imageDonatur)
            itemView.nameDonatur.text = model.name
            itemView.amountDonatur.text = Converter.rupiah(model.donate)
            if (model.pray == "" || model.pray == "null" || model.pray == null){
               hidden(itemView.prayDonatur)
            }else{
                itemView.prayDonatur.text = model.pray
            }
            itemView.timeDonatur.text = model.timeDonate
        }

        fun hidden(view: View){
            view.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDonasiAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.donasi_program_item, parent, false)
        return ListDonasiAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])
    }
}