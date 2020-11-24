package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.services.ApiService
import com.inddevid.aksiberbagi_donatur.services.Converter
import com.inddevid.aksiberbagi_donatur.services.Preferences
import org.json.JSONException
import org.json.JSONObject

class DonasiDetailActivity : AppCompatActivity() {
    private val TAG = "activity donasi detail"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donasi_detail_activity)

        val sharedPreference: Preferences = Preferences(this)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")

        val toolbar: Toolbar = findViewById(R.id.upAppbarDetailDonasi)
        toolbar.title = "Donasi"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE)
        toolbar.setNavigationOnClickListener{
            val mIntent = Intent(this, DashboardActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("donasiSayaAktif", "true")
            mIntent.putExtras(mBundle)
            startActivity(mIntent)}

        var titleHeader : TextView = findViewById(R.id.detailHeaderText)
        var titleSummary : TextView = findViewById(R.id.detailHeaderSummaryText)
        val statusValue: String? = sharedPreference.getValueString("detailDonasiStatus")
        if (statusValue == "GAGAL" && statusValue == "EXPIRED" ){
            titleHeader.text = "Donasi Dibatalkan"
            titleSummary.text = "Batas waktu pembayaran telah berakhir atau donasi gagal tercatat di sistem"
        }else if(statusValue == "MENUNGGU"){
            titleHeader.text = "Menunggu Pembayaran"
            titleSummary.text = "Segera lakukan pembayaran melalui metode pilihan pembayaran sesuai detail berikut"
        }else{
            titleHeader.text = "Terima Kasih!"
            titleSummary.text = "Donasimu telah kami terima dan akan kami salurkan"
        }

        var tanggalDonasi : TextView = findViewById(R.id.detailDonasiTanggal)
        val tanggalValue: String? = sharedPreference.getValueString("detailDonasiWaktu")
        tanggalDonasi.text = tanggalValue

        var paymentDonasi : TextView = findViewById(R.id.detailDonasiPayment)
        val paymentValue: String? = sharedPreference.getValueString("detailDonasiPembayaran")
        paymentDonasi.text = paymentValue

        var nominalDonasi : TextView = findViewById(R.id.detailDonasiNominal)
        val nominalValue: String? = sharedPreference.getValueString("detailDonasiNominal")
        val nominalFormated = Converter.ribuan(nominalValue!!.toDouble())
        nominalDonasi.text = "Rp $nominalFormated"

        var frameDonasiStatus : FrameLayout = findViewById(R.id.detailDonasiStatusframe)
        var textDonasiStatus: TextView = findViewById(R.id.detailDonasiSayaStatus)
        if (statusValue != "SUKSES"){
            frameDonasiStatus.setBackgroundResource(R.drawable.rounded_donasi_download)
            textDonasiStatus.text = statusValue
            textDonasiStatus.setTextColor(Color.parseColor("#eb6b34"))
        }else{
            frameDonasiStatus.setBackgroundResource(R.drawable.rounded_status_donasi)
            textDonasiStatus.text = statusValue
            textDonasiStatus.setTextColor(Color.parseColor("#15BBDA"))
        }

        getKoneksi(retrivedToken,this)

    }

    private fun getKoneksi(
        tokenValue: String?,
        context: DonasiDetailActivity
    ){
        val header: String? = tokenValue
        ApiService.getKoneksi(header).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                getDonasiByID(tokenValue, context)
            }

            override fun onError(anError: ANError?) {
                if(anError?.errorDetail!!.equals("connectionError")){
                    val toast = Toast.makeText(
                        this@DonasiDetailActivity,
                        "Ada masalah dengan Koneksi Internet Anda",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                }else{
                    refreshToken(tokenValue,context)
                }
            }

        })
    }

    private fun refreshToken(
        tokenValue: String?,
        context: DonasiDetailActivity
    ){
        val header : String? = tokenValue
        val sharedPreference: Preferences = Preferences(this@DonasiDetailActivity)
        try {
            ApiService.postRefreshToken(header).getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        if (response?.getString("message").equals("Refresh berhasil")) {
                            val token: String? = response?.getString("token")
                            //save token
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getDonasiByID(tokenValue, context)
                            }
                        } else if (response?.getString("message")
                                .equals("Token expired berhasil di refresh")
                        ) {
                            val token: String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getDonasiByID(tokenValue, context)
                            }
                        } else {
                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                    val intent = Intent(
                                        this@DonasiDetailActivity,
                                        IntroActivity::class.java
                                    )
                                    startActivity(intent)
                                }, 2500)
                            }
                        }

                    } catch (e: JSONException) {
                        val toast = Toast.makeText(
                            this@DonasiDetailActivity,
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
                                this@DonasiDetailActivity,
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
                this@DonasiDetailActivity,
                "Kesalahan Header",
                Toast.LENGTH_LONG
            )
            toast.show()
        }
    }

    private fun getDonasiByID(
        tokenValue: String?,
        context: DonasiDetailActivity
    ){
        val sharedPreference: Preferences = Preferences(this)
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(220, 175)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)
        val idDonasi: String? = sharedPreference.getValueString("idDonasiDetail")
        ApiService.getDonasiDetail(tokenValue,idDonasi).getAsJSONObject(object: JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                val btnPembayaran = context.findViewById<Button>(R.id.btnPembayaran)
                val data = response?.getJSONObject("dataDonasi")
                if(response?.getString("jenisDonasi").equals("Transfer") && data?.getString("tbldonasi_status").equals("MENUNGGU") ){
                    btnPembayaran.visibility = View.VISIBLE
                }else{
                    btnPembayaran.visibility = View.GONE
                }
                val dataProgram = data?.getJSONObject("program")
                val imgProgram = dataProgram?.getString("thumbnail_url")
                val imageProgram = context.findViewById<ImageView>(R.id.imageDetailDonasi)
                Glide.with(context.applicationContext).load(imgProgram).apply(options).into(imageProgram)
                val idProgram = dataProgram?.getString("tblprogram_id")
            }

            override fun onError(anError: ANError?) {
                Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
            }

        })
    }

}