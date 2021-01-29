package com.aksiberbagi.donatur.presenter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.view.ProfilActivity
import kotlinx.android.synthetic.main.dialog_batal_profil.view.*

class CustomDialogBatal : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.dialog_batal_profil, container, false)

        rootView.btnCancel.setOnClickListener{
            startActivity(Intent(requireContext(), ProfilActivity::class.java))
        }
        rootView.btnEnter.setOnClickListener {
            dismiss()
        }

        return rootView
    }


}