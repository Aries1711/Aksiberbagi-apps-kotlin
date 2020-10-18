package com.inddevid.aksiberbagi_donatur.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.ModelPembayaran

class PilihPembayaranAdapter (var mCtx: Context, var resource: Int, var items:List<ModelPembayaran>) :
    ArrayAdapter<ModelPembayaran>(mCtx,resource,items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(resource,null)

        val imageSet: ImageView = view.findViewById(R.id.imageSettingListUser)
        val titleSet: TextView = view.findViewById(R.id.titleSettingListUser)

        //glide options
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(180, 70)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)


        var mItem: ModelPembayaran = items[position]
        Glide.with(imageSet.context).load(mItem.img).into(imageSet)
//        imageSet.setImageDrawable(mCtx.resources.getDrawable(mItem.img))
        titleSet.text = mItem.title

        return view
    }
}