package com.inddevid.aksiberbagi_donatur.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.PenggunaSettings

class PenggunaSettingsAdapter (var mCtx: Context, var resource:Int, var items:List<PenggunaSettings>):ArrayAdapter<PenggunaSettings>(mCtx, resource,items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater:LayoutInflater = LayoutInflater.from(mCtx)
        val view:View = layoutInflater.inflate(resource,null)

        val imageSet:ImageView = view.findViewById(R.id.imageSettingListUser)
        val titleSet:TextView = view.findViewById(R.id.titleSettingListUser)

        var mItem:PenggunaSettings = items[position]
        imageSet.setImageDrawable(mCtx.resources.getDrawable(mItem.img))
        titleSet.text = mItem.title

        return view
    }
}