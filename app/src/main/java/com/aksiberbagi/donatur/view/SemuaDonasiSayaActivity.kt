package com.aksiberbagi.donatur.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.facebook.shimmer.ShimmerFrameLayout
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.model.CardDonasiSaya
import com.aksiberbagi.donatur.presenter.RecyclerDonasiSayaAdapter
import com.aksiberbagi.donatur.services.ApiService
import com.aksiberbagi.donatur.services.Preferences
import org.json.JSONException
import org.json.JSONObject

class SemuaDonasiSayaActivity : AppCompatActivity() {
    private val TAG = "Semua Donasi Saya"
    private val arrayDonasi = ArrayList<CardDonasiSaya>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.all_donasi_saya_activity)

        val sharedPreference: Preferences = Preferences(this)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")

        val toolbar: Toolbar = findViewById(R.id.upAppbarSemuaDonasi)
        toolbar.title = "Catatan Kebaikan"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE)
        toolbar.setNavigationOnClickListener{
            val mIntent = Intent(this, DashboardActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("donasiSayaAktif", "true")
            mIntent.putExtras(mBundle)
            startActivity(mIntent)}

        val shimmerFrame: ShimmerFrameLayout = findViewById(R.id.shimmerSemuaDonasi)
        shimmerFrame.startShimmer()

        getKoneksi(retrivedToken!!, this)

    }

    private fun getKoneksi(tokenValue: String, context: SemuaDonasiSayaActivity){
        ApiService.getKoneksi(tokenValue).getAsJSONObject(object: JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                getAllDonasiSaya(tokenValue, context)
            }

            override fun onError(anError: ANError?) {
                if(anError?.errorDetail!!.equals("connectionError")){
                    val toast = Toast.makeText(
                        this@SemuaDonasiSayaActivity,
                        "Ada masalah dengan Koneksi Internet Anda",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    return
                }else{
                    refreshToken(tokenValue,context)
                }
            }

        })

    }

    private fun refreshToken(
        tokenValue: String?,
        context: SemuaDonasiSayaActivity
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
                                getAllDonasiSaya(tokenValue, context)
                            }
                        } else if (response?.getString("message")
                                .equals("Token expired berhasil di refresh")
                        ) {
                            val token: String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getAllDonasiSaya(tokenValue, context)
                            }
                        } else {
                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                    val intent = Intent(
                                        this@SemuaDonasiSayaActivity,
                                        IntroActivity::class.java
                                    )
                                    startActivity(intent)
                                }, 2500)
                            }
                        }

                    } catch (e: JSONException) {
                        val toast = Toast.makeText(
                            this@SemuaDonasiSayaActivity,
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
                                this@SemuaDonasiSayaActivity,
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

    private fun getAllDonasiSaya(
        tokenValue: String?,
        context: SemuaDonasiSayaActivity
    ){
        ApiService.getDonasiSaya(tokenValue).getAsJSONObject(object: JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                // get All donasi terakhir user
                val invoiceDonasi = response?.getJSONArray("data")
                if (invoiceDonasi?.length()!! > 0){
                    for(i in 0 until invoiceDonasi.length()){
                        val item = invoiceDonasi.getJSONObject(i)
                        val program = item?.getJSONObject("program")
                        val idProgram = program?.getString("tblprogram_id")
                        val judulProgram = program?.getString("tblprogram_judul")
                        val bank = item?.getJSONObject("bank")
                        val namaBank = bank?.getString("tblbank_nama")
                        val nominaldonasi = item?.getString("tbldonasi_nominal")
                        val waktuDonasi = item?.getString("tanggal_donasi")
                        val jenisDonasi = item?.getString("jenis_donasi")
                        val donasiStatus = item?.getString("tbldonasi_status")
                        val gambarProgram = program?.getString("tblprogram_file")
                        arrayDonasi.add(CardDonasiSaya(judulProgram,namaBank,nominaldonasi,waktuDonasi,donasiStatus,gambarProgram,idProgram, jenisDonasi))
                    }
                    //inflate the recycler for cardview donasi saya
                    val myAdapterB = RecyclerDonasiSayaAdapter(arrayDonasi, this@SemuaDonasiSayaActivity)
                    var mainMenu = context.findViewById<RecyclerView>(R.id.recyclerDonasiSayaAll)
                    mainMenu.layoutManager = LinearLayoutManager(this@SemuaDonasiSayaActivity)
                    mainMenu.adapter = myAdapterB
                    val shimmerFrame: ShimmerFrameLayout = context.findViewById(R.id.shimmerSemuaDonasi)
                    val layoutUtama = context.findViewById<RecyclerView>(R.id.recyclerDonasiSayaAll)
                    shimmerFrame.stopShimmer()
                    shimmerFrame.visibility = View.GONE
                    layoutUtama.visibility = View.VISIBLE
                }

            }

            override fun onError(anError: ANError?) {
                val toast = Toast.makeText(this@SemuaDonasiSayaActivity, "ada Kesalahan Sistem", Toast.LENGTH_LONG)
                toast.show()
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}