package com.aksiberbagi.donatur.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.model.BerandaSlideBanner

class BerandaSlideAdapter(private val berandaSlideBanner: List<BerandaSlideBanner>):
    RecyclerView.Adapter<BerandaSlideAdapter.BerandaSlideBannerViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BerandaSlideBannerViewHolder {
        return BerandaSlideBannerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slide_item_beranda,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return berandaSlideBanner.size
    }

    override fun onBindViewHolder(holder: BerandaSlideBannerViewHolder, position: Int) {
        holder.bind(berandaSlideBanner[position])
    }

    inner class BerandaSlideBannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageIcon = view.findViewById<ImageView>(R.id.imageSlideBanner)

        fun bind(BerandaSlideBanner: BerandaSlideBanner){
            Glide.with(imageIcon.context).load(BerandaSlideBanner.icon).into(imageIcon)
//            imageIcon.setImageResource(BerandaSlideBanner.icon)
        }
    }
}