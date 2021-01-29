package com.aksiberbagi.donatur.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.model.ModelPembayaran
import com.aksiberbagi.donatur.presenter.PilihPembayaranAdapter
import com.aksiberbagi.donatur.services.ApiError
import com.aksiberbagi.donatur.services.ApiService
import com.aksiberbagi.donatur.services.Preferences
import org.json.JSONException
import org.json.JSONObject

class PilihPembayaranActivity : AppCompatActivity() {
    private val arrayEwallet = ArrayList<ModelPembayaran>()
    private val arrayTranfer = ArrayList<ModelPembayaran>()
    private val TAG = "Pilih Pembayaran Activity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pilih_pembayaran_activity)

        val toolbar: Toolbar = findViewById(R.id.upAppbarMetodePembayaran)
        toolbar.title = "Metode Pembayaran"
        toolbar.setTitleTextColor(Color.WHITE)
        val sharedPreference: Preferences = Preferences(this)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")
        var valueNominal: String? = intent.getStringExtra("nominal")
        var spinnerPilihan: String? = intent.getStringExtra("spinnerValue")
        var layoutEwallet: FrameLayout = findViewById(R.id.layoutEwallet)
        var layoutTransfer: FrameLayout = findViewById(R.id.layoutTransfer)


        var nominalSet: String = ""
        var spinnerSet: String = ""

        if(spinnerPilihan == "Masukkan Nominal Lain"){
            if (valueNominal == ""){
                nominalSet = "0"
                spinnerSet = spinnerPilihan.toString()
            }else{
                valueNominal = valueNominal!!.replace(".", "")
                if(valueNominal!!.toInt() in 0..999 ){
                    layoutEwallet.setBackgroundColor(Color.parseColor("#c5c7c7"))
                    layoutEwallet.bringToFront()
                    layoutTransfer.setBackgroundColor(Color.parseColor("#c5c7c7"))
                    layoutTransfer.bringToFront()
                }else if(valueNominal!!.toInt() in 1000..9999){
                    layoutTransfer.setBackgroundColor(Color.parseColor("#c5c7c7"))
                    layoutTransfer.bringToFront()
                }
            }
        }
        nominalSet = valueNominal.toString()
        spinnerSet = spinnerPilihan.toString()

        getKoneksi(retrivedToken, this, nominalSet, spinnerSet)


        toolbar.setNavigationOnClickListener{
            val mIntent = Intent(this, ProgramDetailActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("dialogAktif", "true")
            mBundle.putString("nominalDonasi", nominalSet)
            mBundle.putString("spinner", spinnerPilihan)
            mIntent.putExtras(mBundle)
            startActivity(mIntent)
        }

    }


    private fun getKoneksi(
        tokenValue: String?,
        context: PilihPembayaranActivity,
        nominal: String,
        spinner: String
    ){
        val header: String? = tokenValue
        ApiService.getKoneksi(header).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                getPilihPembayaran(tokenValue, context, nominal, spinner)
            }

            override fun onError(anError: ANError?) {
                val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                if(anError?.errorDetail!!.equals("connectionError")){
                    val toast = Toast.makeText(
                        this@PilihPembayaranActivity,
                        "Ada masalah dengan Koneksi Internet Anda",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    return
                }else{
                    refreshToken(tokenValue,context,nominal, spinner)
                }
            }

        })
    }

    private fun refreshToken(
        tokenValue: String?,
        context: PilihPembayaranActivity,
        nominal: String,
        spinner: String
    ){
        val header : String? = tokenValue
        val sharedPreference: Preferences = Preferences(this)
        try {
            ApiService.postRefreshToken(header).getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        if (response?.getString("message").equals("Refresh berhasil")) {
                            val token: String? = response?.getString("token")
                            //save token
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(token,context,nominal,spinner)
                            }
                        } else if (response?.getString("message")
                                .equals("Token expired berhasil di refresh")
                        ) {
                            val token: String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(token,context,nominal,spinner)
                            }
                        } else {
                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                    val intent = Intent(
                                        this@PilihPembayaranActivity,
                                        IntroActivity::class.java
                                    )
                                    startActivity(intent)
                                }, 2500)
                            }
                        }

                    } catch (e: JSONException) {
                        val toast = Toast.makeText(
                            this@PilihPembayaranActivity,
                            "Invalid Json",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                }

                override fun onError(anError: ANError?) {
                    Looper.myLooper()?.let {
                        Handler(it).postDelayed({
                            val intent = Intent(
                                this@PilihPembayaranActivity,
                                IntroActivity::class.java
                            )
                            startActivity(intent)
                        }, 2500)
                    }
                    Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                    Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                    Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
                }

            })
        }catch (e: JSONException){
            val toast = Toast.makeText(
                this,
                "Kesalahan Header",
                Toast.LENGTH_LONG
            )
            toast.show()
        }
    }

    private fun getPilihPembayaran(tokenValue: String?, context: PilihPembayaranActivity, nominal: String, spinner: String){

        ApiService.getPembayaran(tokenValue).getAsJSONObject(object : JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                val jsonObject = response?.getJSONObject("data")
                val jsonArrayEwallet = jsonObject?.getJSONArray("eWallet")
                val jsonArrayBank = jsonObject?.getJSONArray("bank")
                if (jsonArrayEwallet?.length()!! > 0) {
                    for (i in 0 until jsonArrayEwallet.length()) {
                        val item = jsonArrayEwallet.getJSONObject(i)
                        val idPembayaran = item?.getString("tblbank_id")
                        val namaPembayaran = item?.getString("tblbank_nama")
                        val gambarPembayaran = item?.getString("logo_url")
                        val tipePembayaran = "Ewallet"
                        arrayEwallet.add(ModelPembayaran(idPembayaran,namaPembayaran,gambarPembayaran))
                        val myAdapterEwallet = PilihPembayaranAdapter(arrayEwallet,this@PilihPembayaranActivity, nominal, spinner,tipePembayaran)
                        var mainMenuEwallet = context.findViewById<RecyclerView>(R.id.recyclerEwallet)
                        mainMenuEwallet.layoutManager = LinearLayoutManager(this@PilihPembayaranActivity)
                        mainMenuEwallet.adapter = myAdapterEwallet
                    }
                }

                if (jsonArrayBank?.length()!! > 0) {
                    for (i in 0 until jsonArrayBank.length()) {
                        val item = jsonArrayBank.getJSONObject(i)
                        val idPembayaran = item?.getString("tblbank_id")
                        val namaPembayaran = item?.getString("tblbank_nama")
                        val gambarPembayaran = item?.getString("logo_url")
                        val tipePembayaran = "Transfer"
                        arrayTranfer.add(ModelPembayaran(idPembayaran,namaPembayaran,gambarPembayaran))
                        val myAdapterTransfer = PilihPembayaranAdapter(arrayTranfer, this@PilihPembayaranActivity, nominal , spinner,tipePembayaran)
                        var mainMenuTranfer = findViewById<RecyclerView>(R.id.recyclerTransfer)
                        mainMenuTranfer.layoutManager = LinearLayoutManager(this@PilihPembayaranActivity)
                        mainMenuTranfer.adapter = myAdapterTransfer
                    }
                }
            }

            override fun onError(anError: ANError?) {
                TODO("Not yet implemented")
            }

        })
    }
}