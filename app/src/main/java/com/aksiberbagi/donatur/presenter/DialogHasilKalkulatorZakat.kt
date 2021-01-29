package com.aksiberbagi.donatur.presenter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.services.Converter
import com.aksiberbagi.donatur.view.ZakatActivity
import kotlinx.android.synthetic.main.dialog_hasil_kalkulator_zakat.view.*

class DialogHasilKalkulatorZakat( val idZakat: String, val jenisZakat: String, val nominal: String , val statusZakat: String) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.dialog_hasil_kalkulator_zakat, container, false)

        //deklarasi value tampilan pada dialog kalkulator
        rootView.dialogText.text = "Hasil Kalkulator zakat $jenisZakat"
        rootView.dialogWajibText.text = statusZakat
        val formatedNominal = Converter.ribuan(nominal.toDouble())
        rootView.dialogHasilZakat.text = "Rp. $formatedNominal"


        rootView.btnCancel.setOnClickListener{
            val mIntent = Intent(requireContext(), ZakatActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("nominalZakatKalkulator", nominal)
            mBundle.putString("jenisZakatKalkulator", jenisZakat)
            mBundle.putString("idZakatKalkulator", idZakat )
            mBundle.putString("dialogPembayaran", "aktif" )
            mIntent.putExtras(mBundle)
            startActivity(mIntent)
        }
        rootView.btnEnter.setOnClickListener {
            dismiss()
        }

        return rootView
    }


}