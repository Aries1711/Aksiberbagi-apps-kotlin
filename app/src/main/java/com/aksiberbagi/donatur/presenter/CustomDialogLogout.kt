package com.aksiberbagi.donatur.presenter

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.aksiberbagi.donatur.view.IntroActivity
import kotlinx.android.synthetic.main.dialog_logout.view.*
import org.json.JSONObject

class CustomDialogLogout : DialogFragment() {

    private val TAG = "Fragment Logout"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.dialog_logout, container, false)

        val sharedPreference: Preferences = Preferences(requireContext())
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")

        rootView.logoutCancel.setOnClickListener{
            dismiss()
        }
        rootView.logoutBtn.setOnClickListener {
            postLogout(retrivedToken)
        }

        return rootView
    }

    private fun postLogout(tokenValue: String?){
        val sharedPreference: Preferences = Preferences(requireContext())
        ApiService.postLogout(tokenValue).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                sharedPreference.save("TOKEN", "")
                sharedPreference.save("penggunaNAMA", "")
                sharedPreference.save("penggunaWA", "")
                sharedPreference.save("penggunaAlamat", "")
                sharedPreference.save("penggunaProfesi", "")
                sharedPreference.save("penggunaEmail", "" )
                val token = sharedPreference.getValueString("TOKEN")
                Looper.myLooper()?.let {
                    Handler(it).postDelayed({
                        val intent = Intent(requireContext(), IntroActivity::class.java)
                        startActivity(intent)
                    }, 2500)
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