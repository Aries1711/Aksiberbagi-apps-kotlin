package com.aksiberbagi.donatur.presenter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.services.ApiService
import com.aksiberbagi.donatur.services.Preferences
import com.aksiberbagi.donatur.view.DonasiRutinActivity
import kotlinx.android.synthetic.main.dialog_donasi_rutin_list.view.*
import org.json.JSONObject


class DialogDonasiRutinList(val idDonasi: String) : DialogFragment() {
    private val TAG = "Dialog Hapus Donasi Rutin"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.dialog_donasi_rutin_list, container, false)
        val sharedPreference: Preferences = Preferences(requireContext())
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")
        rootView.btnHapusDonasiRutin.setOnClickListener{
            deleteDonasiRutin(retrivedToken, idDonasi, rootView)
        }
        rootView.btnCancelHapus.setOnClickListener {
            dismiss()
        }

        return rootView
    }

    private fun deleteDonasiRutin(tokenValue: String?, idRutin: String, view: View){
        ApiService.deleteDonasiRutin(tokenValue, idRutin).getAsJSONObject(object :
            JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                if (response?.getString("message").equals("Donasi rutin berhasil di hapus")) {
                    val toast = Toast.makeText(
                        requireContext(),
                        "Data Donasi rutin berhasil di hapus",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    startActivity(Intent(requireActivity(), DonasiRutinActivity::class.java))
                } else if (response?.getString("message").equals("Data Donasi rutin tidak valid")) {
                    val toast = Toast.makeText(
                        requireContext(),
                        "Maaf, data Donasi rutin tidak valid",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                }
            }

            override fun onError(anError: ANError?) {
                val toast = Toast.makeText(
                    requireContext(),
                    "Maaf, Cobalah beberapa saat lagi",
                    Toast.LENGTH_LONG
                )
                toast.show()
                Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
            }

        })
    }

}