package com.aksiberbagi.donatur.view

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
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.services.ApiService
import com.aksiberbagi.donatur.services.Converter
import com.aksiberbagi.donatur.services.Preferences
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
        toolbar.title = "Donasi Detail"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE)
        toolbar.setNavigationOnClickListener {
            val mIntent = Intent(this, DashboardActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("donasiSayaAktif", "true")
            mIntent.putExtras(mBundle)
            startActivity(mIntent)
        }

        var keyFirebase: String = "tidak ada"
        var dataTagihan: String = "tidak ada"

        var statusValue: String? = ""
        var tanggalValue: String? = ""
        var paymentValue: String? = ""
        var nominalValue: String? = ""
        var jenisValue: String? = ""

        if (intent.hasExtra("keyFirebase")) {
                dataTagihan = sharedPreference.getValueString("dataTagihan").toString()
                val dataTagihanObject = JSONObject(dataTagihan)
                statusValue = dataTagihanObject.getString("donasi_status")
                tanggalValue = dataTagihanObject.getString("donasi_tglinsert")
                paymentValue = dataTagihanObject.getString("bank_nama")
                nominalValue = dataTagihanObject.getString("donasi_nominal")
                jenisValue = dataTagihanObject.getString("jenis_donasi")
                sharedPreference.save("idDonasiDetail", dataTagihanObject.getString("donasi_id"))
                sharedPreference.save("detailDonasiNominal", dataTagihanObject.getString("donasi_nominal"))
        } else {
            statusValue = sharedPreference.getValueString("detailDonasiStatus")
            tanggalValue = sharedPreference.getValueString("detailDonasiWaktu")
            paymentValue = sharedPreference.getValueString("detailDonasiPembayaran")
            nominalValue = sharedPreference.getValueString("detailDonasiNominal")
            jenisValue = sharedPreference.getValueString("detailDonasiJenis")
        }


        var titleHeader: TextView = findViewById(R.id.detailHeaderText)
        var titleSummary: TextView = findViewById(R.id.detailHeaderSummaryText)

        if (statusValue == "GAGAL" || statusValue == "EXPIRED") {
            titleHeader.text = "Donasi Dibatalkan"
            titleSummary.text =
                "Batas waktu pembayaran telah berakhir atau donasi gagal tercatat di sistem"
        } else if (statusValue == "MENUNGGU") {
            titleHeader.text = "Menunggu Pembayaran"
            titleSummary.text =
                "Segera lakukan pembayaran melalui metode pilihan pembayaran sesuai detail berikut"
        } else {
            titleHeader.text = "Terima Kasih #orangBaik"
            titleSummary.text = "Donasimu telah kami terima dan akan kami salurkan"
        }

        val btnPembayaran: Button = findViewById(R.id.btnPembayaran)
        if (jenisValue == "Transfer" && statusValue == "MENUNGGU") {
            btnPembayaran.visibility = View.VISIBLE
        } else {
            btnPembayaran.visibility = View.GONE
        }
        val detailDonasiLagiBtn : Button = findViewById(R.id.detailDonasiLagiBtn)

        var tanggalDonasi: TextView = findViewById(R.id.detailDonasiTanggal)

        var paymentDonasi: TextView = findViewById(R.id.detailDonasiPayment)

        var nominalDonasi: TextView = findViewById(R.id.detailDonasiNominal)
        val nominalFormated = Converter.ribuan(nominalValue!!.toDouble())



        tanggalDonasi.text = tanggalValue
        paymentDonasi.text = paymentValue
        nominalDonasi.text = "Rp $nominalFormated"


        var frameDonasiStatus: FrameLayout = findViewById(R.id.detailDonasiStatusframe)
        var textDonasiStatus: TextView = findViewById(R.id.detailDonasiSayaStatus)
        if (statusValue != "SUKSES") {
            frameDonasiStatus.setBackgroundResource(R.drawable.rounded_donasi_download)
            textDonasiStatus.text = statusValue
            textDonasiStatus.setTextColor(Color.parseColor("#eb6b34"))
        } else {
            frameDonasiStatus.setBackgroundResource(R.drawable.rounded_status_donasi)
            textDonasiStatus.text = statusValue
            textDonasiStatus.setTextColor(Color.parseColor("#15BBDA"))
        }

        getKoneksi(retrivedToken, this)

        getDetailProgramById( this, retrivedToken,detailDonasiLagiBtn )


    }

    private fun getKoneksi(
        tokenValue: String?,
        context: DonasiDetailActivity
    ) {
        val header: String? = tokenValue
        ApiService.getKoneksi(header).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                getDonasiByID(tokenValue, context)
            }

            override fun onError(anError: ANError?) {
                if (anError?.errorDetail!!.equals("connectionError")) {
                    val toast = Toast.makeText(
                        this@DonasiDetailActivity,
                        "Ada masalah dengan Koneksi Internet Anda",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                } else {
                    refreshToken(tokenValue, context)
                }
            }

        })
    }

    private fun refreshToken(
        tokenValue: String?,
        context: DonasiDetailActivity
    ) {
        val header: String? = tokenValue
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
        } catch (e: JSONException) {
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
    ) {
        val sharedPreference: Preferences = Preferences(this)
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(220, 175)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)
        val idDonasi: String? = sharedPreference.getValueString("idDonasiDetail")
        ApiService.getDonasiDetail(tokenValue, idDonasi)
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val data = response?.getJSONObject("dataDonasi")
                    val dataProgram = data?.getJSONObject("program")
                    val dataBank = data?.getJSONObject("bank")
                    val imgProgram = dataProgram?.getString("thumbnail_url")
                    val imageProgram = context.findViewById<ImageView>(R.id.imageDetailDonasi)
                    Glide.with(context.applicationContext).load(imgProgram).apply(options)
                        .into(imageProgram)
                    val idProgram = dataProgram?.getString("tblprogram_id")
                    val judulProgram = dataProgram?.getString("tblprogram_judul")
                    var judulProgramText =
                        context.findViewById<TextView>(R.id.detailDonasiProgramJudul)
                    judulProgramText.text = judulProgram

                    val btnPembayaran = context.findViewById<Button>(R.id.btnPembayaran)
                    btnPembayaran.setOnClickListener{
                        val kodeUnik = data?.getString("tbldonasi_nourut")
                        val nominalPlusKode = data?.getString("tbldonasi_nominal")
                        var nominalRaw = nominalPlusKode!!.toInt() - kodeUnik!!.toInt()
                        sharedPreference.save("invoiceNominal",sharedPreference.getValueString("detailDonasiNominal"))
                        sharedPreference.save("donasiNominal", nominalRaw)
                        sharedPreference.save("invoiceKodeUnik", data?.getString("tbldonasi_nourut"))
                        sharedPreference.save("invoiceKode", data?.getString("tbldonasi_invoice"))
                        sharedPreference.save("invoiceBank", dataBank?.getString("tblbank_nama"))
                        sharedPreference.save("invoiceBankAN",dataBank?.getString("tblbank_namapemilik"))
                        sharedPreference.save("invoiceBankUrl", dataBank?.getString("logo_url"))
                        sharedPreference.save("invoiceBankRekening",dataBank?.getString("tblbank_rekening"))
                        sharedPreference.save("invoiceProgramJudul", judulProgram)

                        startActivity(
                            Intent(
                                this@DonasiDetailActivity,
                                InvoiceActivity::class.java
                            )
                        )
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                    Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                    Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
                }

            })
    }

    private fun getDetailProgramById(context: DonasiDetailActivity, tokenValue: String?, buttonDonasiLagi: Button){
        val sharedPreference: Preferences = Preferences(this)
        val idDonasi: String? = sharedPreference.getValueString("idDonasiDetail")

        ApiService.getProgramDetailID(tokenValue, idDonasi).getAsJSONObject(object : JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                val data = response?.getJSONObject("data")
                val cabang = data?.getJSONObject("cabang")

                val idProgram = data?.getString("tblprogram_id")
                val gambarProgram = data?.getString("thumbnail_url")
                val slugProgram = data?.getString("tblprogram_namalink")
                val judulProgram = data?.getString("tblprogram_judul")
                val penggalangProgram = cabang?.getString("tblcabang_nama")
                val capaianProgram = data?.getString("capaian_donasi")
                val sisaHariProgram = data?.getString("sisa_hari")
                val tanggalMulai = data?.getString("tanggal_mulai_donasi")
                val progressProgram = data?.getInt("progress")
                val targetNominal: String? = data?.getString("tblprogram_isiantargetnominal")
                var targetDonasi : String?
                if (targetNominal == "null" || targetNominal == "0" ){
                    targetDonasi = "100"
                }else{
                    targetDonasi = data?.getString("target_nominal")
                }

                buttonDonasiLagi.setOnClickListener {
                    sharedPreference.save("idProgram", idProgram)
                    sharedPreference.save("img", gambarProgram)
                    sharedPreference.save("urlProgram", slugProgram)
                    sharedPreference.save("judul", judulProgram)
                    sharedPreference.save("penggalang", penggalangProgram)
                    sharedPreference.save("capaian", capaianProgram)
                    sharedPreference.save("sisaHari", sisaHariProgram)
                    sharedPreference.save("tanggalMulai", tanggalMulai)
                    sharedPreference.save("progressProgram", progressProgram)
                    sharedPreference.save("targetProgram", targetDonasi)
                    val mIntent = Intent(context, ProgramDetailActivity::class.java)
                    context.startActivity(mIntent)
                }

            }

            override fun onError(anError: ANError?) {
                Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
            }

        })

    }

}