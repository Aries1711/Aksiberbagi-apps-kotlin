package com.inddevid.aksiberbagi_donatur.presenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.inddevid.aksiberbagi_donatur.R
import kotlinx.android.synthetic.main.dialog_donasi_rutin_list.view.*

class DialogDonasiRutinList(val idDonasi: String) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.dialog_donasi_rutin_list, container, false)

        rootView.btnHapusDonasiRutin.setOnClickListener{
            //
        }
        rootView.btnCancelHapus.setOnClickListener {
            dismiss()
        }

        return rootView
    }




}