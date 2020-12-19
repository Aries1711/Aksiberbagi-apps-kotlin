package com.inddevid.aksiberbagi_donatur.presenter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.view.DonasiRutinActivity
import kotlinx.android.synthetic.main.dialog_pengingat_donasi.view.*

class DialogPengingatDonasi (val dateDonasi: String) : DialogFragment() {
    private val TAG = "Dialog Hapus Donasi Rutin"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.dialog_pengingat_donasi, container, false)
        rootView.DialogText.text = "Atur pengingat donasi pada tanggal $dateDonasi"
        rootView.btnSetPengingatYes.setOnClickListener{
            if (dateDonasi.toInt() <= 28){
                setDonasiRutin(dateDonasi)
            }else{
                val toast = Toast.makeText(
                    requireContext(),
                    "Maaf, tanggal pengingat tidak boleh lebih dari tanggal 28 di setiap bulannya",
                    Toast.LENGTH_LONG
                )
                toast.show()
                dismiss()
            }
        }
        rootView.btnSetPengingatNo.setOnClickListener {
            dismiss()
        }

        return rootView
    }

    private fun setDonasiRutin( dateDonasi: String){
        val mIntent = Intent(requireContext(), DonasiRutinActivity::class.java)
        val mBundle = Bundle()
        mBundle.putString("tanggalPengingat", dateDonasi)
        mIntent.putExtras(mBundle)
        requireContext().startActivity(mIntent)
    }


}