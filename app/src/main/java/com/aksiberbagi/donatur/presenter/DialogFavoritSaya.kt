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
import com.aksiberbagi.donatur.view.DashboardActivity
import kotlinx.android.synthetic.main.dialog_donasi_rutin_list.view.*
import org.json.JSONObject

class DialogFavoritSaya (val idDonasi: String) : DialogFragment() {
    private val TAG = "Dialog Hapus favorit"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.dialog_favorit_saya, container, false)
        val sharedPreference: Preferences = Preferences(requireContext())
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")
        rootView.btnHapusDonasiRutin.setOnClickListener{
            deleteFavoritSaya(retrivedToken, idDonasi, rootView)
        }
        rootView.btnCancelHapus.setOnClickListener {
            dismiss()
        }

        return rootView
    }

    private fun deleteFavoritSaya(tokenValue: String?, idRutin: String, view: View){
        ApiService.deleteFavorit(tokenValue, idRutin).getAsJSONObject(object :
            JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                if(response?.getString("message").equals("Program favorit saya berhasil dihapus")){
                    val toast = Toast.makeText(
                        requireContext(),
                        "Program favorit saya berhasil dihapus",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    val mIntent = Intent(requireContext(), DashboardActivity::class.java)
                    val mBundle = Bundle()
                    mBundle.putString("favoritAktif", "true")
                    mIntent.putExtras(mBundle)
                    startActivity(mIntent)
                }else if(response?.getString("message").equals("Data tidak ditemukan")){
                    val toast = Toast.makeText(
                        requireContext(),
                        "Maaf, data program tidak valid",
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