package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.ModelPembayaran
import com.inddevid.aksiberbagi_donatur.presenter.PilihPembayaranAdapter
import com.inddevid.aksiberbagi_donatur.services.ApiService
import com.inddevid.aksiberbagi_donatur.services.Preferences
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

//        val toast = Toast.makeText(this, spinnerPilihan, Toast.LENGTH_LONG)
//        toast.show()

        var nominalSet: String = ""
        var spinnerSet: String = ""

        if (valueNominal == ""){
            nominalSet = "0"
            spinnerSet = spinnerPilihan.toString()
        }else{
            nominalSet = valueNominal.toString()
            spinnerSet = spinnerPilihan.toString()
        }

        getKoneksi(retrivedToken, this)


        toolbar.setNavigationOnClickListener{
            val mIntent = Intent(this, ProgramDetailActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("dialogAktif", "true")
            mBundle.putString("nominalDonasi", nominalSet)
            mBundle.putString("spinner", spinnerPilihan)
            mIntent.putExtras(mBundle)
            startActivity(mIntent)
        }


        arrayEwallet.add(ModelPembayaran("1","Gopay","https://aksiberbagi.com/storage/bank/gopay-new.png"))
        arrayEwallet.add(ModelPembayaran("2","Dana","https://aksiberbagi.com/storage/bank/vi2Vr1CBPDczTvFlMIybb6bqQIjEKUmQvHAkhbqW.png"))
        arrayEwallet.add(ModelPembayaran("3","OVO","https://aksiberbagi.com/storage/bank/logo-ovo.png"))
        arrayEwallet.add(ModelPembayaran("4","Link Aja","https://aksiberbagi.com/storage/bank/8uOgHqnWJByhe5p3QMFzCTYxmTLBJpuKGtku3djn.png"))
        arrayEwallet.add(ModelPembayaran("5","ShopeePay","https://aksiberbagi.com/storage/bank/8QZzJV93mHSsDCEEMicKg985ba5MMbBjbsXvACDG.png"))
        val myAdapterEwallet = PilihPembayaranAdapter(arrayEwallet,this, nominalSet, spinnerSet )
        var mainMenuEwallet = findViewById<RecyclerView>(R.id.recyclerEwallet)
        mainMenuEwallet.layoutManager = LinearLayoutManager(this)
        mainMenuEwallet.adapter = myAdapterEwallet



        arrayTranfer.add(ModelPembayaran("6","BNI/BNI Syariah", "https://aksiberbagi.com/storage/bank/bni-syariah.png"))
        arrayTranfer.add(ModelPembayaran("7","Mandiri Syariah", "https://aksiberbagi.com/storage/bank/mandiri.png"))
        arrayTranfer.add(ModelPembayaran("8","BCA", "https://aksiberbagi.com/storage/bank/ajZ7LEACpYkW1mVJ6VsuWFdGudbcgusWKCnxiVi2.png"))
        arrayTranfer.add(ModelPembayaran("9","BRI", "https://aksiberbagi.com/storage/bank/YqbwMgj8qh6DiowN8ZYgaKHWGMg29XUJUdzV1iwj.png"))
        arrayTranfer.add(ModelPembayaran("10","Mandiri", "https://aksiberbagi.com/storage/bank/logo-mandiri.png"))
        arrayTranfer.add(ModelPembayaran("11","Muamalat", "https://aksiberbagi.com/storage/bank/Qdtf4FswhYjSyRyMcod8U59FQ6w5peVKyfHrtNHk.png"))
        arrayTranfer.add(ModelPembayaran("12","CIMB Niaga / Syariah", "https://aksiberbagi.com/storage/bank/eSxD2RemwuxkNdjIIAfZWpEeaioePxX4LfybUujK.png"))
        val myAdapterTransfer = PilihPembayaranAdapter(arrayTranfer, this, nominalSet , spinnerSet)
        var mainMenuTranfer = findViewById<RecyclerView>(R.id.recyclerTransfer)
        mainMenuTranfer.layoutManager = LinearLayoutManager(this)
        mainMenuTranfer.adapter = myAdapterTransfer

    }


    private fun getKoneksi(
        tokenValue: String?,
        context: PilihPembayaranActivity
    ){
        val header: String? = tokenValue
        ApiService.getKoneksi(header).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                getPilihPembayaran(tokenValue, context)
            }

            override fun onError(anError: ANError?) {
                refreshToken(tokenValue,context)
            }

        })
    }

    private fun refreshToken(
        tokenValue: String?,
        context: PilihPembayaranActivity
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

                            }
                        } else if (response?.getString("message")
                                .equals("Token expired berhasil di refresh")
                        ) {
                            val token: String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)

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

    private fun getPilihPembayaran(tokenValue: String?, context: PilihPembayaranActivity){

        ApiService.getPembayaran(tokenValue).getAsJSONObject(object : JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                val jsonObject = response?.getJSONObject("data")
                val jsonArrayEwallet = jsonObject?.getJSONArray("eWallet")
                val jsonArrayBank = jsonObject?.getJSONArray("bank")
                if (jsonArrayEwallet?.length()!! > 0) {
                    for (i in 0 until jsonArrayEwallet.length()) {

                    }
                }

                if (jsonArrayBank?.length()!! > 0) {
                    for (i in 0 until jsonArrayBank.length()) {

                    }
                }
            }

            override fun onError(anError: ANError?) {
                TODO("Not yet implemented")
            }

        })
    }
}