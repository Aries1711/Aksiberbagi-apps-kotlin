package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.androchef.happytimer.countdowntimer.DynamicCountDownView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.services.ApiError
import com.inddevid.aksiberbagi_donatur.services.ApiService
import com.inddevid.aksiberbagi_donatur.services.Converter
import com.inddevid.aksiberbagi_donatur.services.Preferences
import org.json.JSONException
import org.json.JSONObject


class LelangActivity : AppCompatActivity() {
    private val TAG = "Lelang Activity Scroll"
    private val arrayIdPembayaran: ArrayList<String> = arrayListOf("0")
    private val arrayNamaPembayaran: ArrayList<String> = arrayListOf("Pilih Pembayaran")
    private val arrayIdNominal: ArrayList<String> = arrayListOf("0")
    private val arrayNamaNominal: ArrayList<String> = arrayListOf("Pilih Nominal")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lelang_activity)

        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(425, 470)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

        val sharedPreference: Preferences = Preferences(this)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")
        val idLelang = sharedPreference.getValueString("idLelang")
        val judulLelang = sharedPreference.getValueString("judulLelang")
        val urlImage = sharedPreference.getValueString("imgLelang")
        val terdonasikan = sharedPreference.getValueString("terdonasiLelang")

        val nominalLelang = sharedPreference.getValueString("nominalLelang")
        val idLelangProgram = sharedPreference.getValueString("idLelangProgram")
        val judulLelangProgram = sharedPreference.getValueString("judulLelangProgram")
        val toolbarLelang : Toolbar = findViewById(R.id.toolbarLelang)
        toolbarLelang.bringToFront()
        val btnBack: LinearLayout = findViewById(R.id.backLelangBtn)
        val btnShare: LinearLayout = findViewById(R.id.shareLelangBtn)
        val btnOption: LinearLayout = findViewById(R.id.opsiLelangBtn)
        val layoutScroll : NestedScrollView = findViewById(R.id.layoutLelangKonten)
        layoutScroll.viewTreeObserver.addOnScrollChangedListener {
            val scrollY: Int = layoutScroll.scrollY
            if (scrollY >= 1020){
                toolbarLelang.setBackgroundColor(ContextCompat.getColor(this, R.color.colorInputStrokeBlue))
                btnBack.background = ContextCompat.getDrawable(this, R.drawable.background_transparent)
                btnShare.background = ContextCompat.getDrawable(this, R.drawable.background_transparent)
                btnOption.background = ContextCompat.getDrawable(this, R.drawable.background_transparent)
            }else{
                toolbarLelang.background = ContextCompat.getDrawable(this, R.drawable.background_transparent)
                btnBack.background = ContextCompat.getDrawable(this, R.drawable.btn_toolbar_active)
                btnShare.background = ContextCompat.getDrawable(this, R.drawable.btn_toolbar_active)
                btnOption.background = ContextCompat.getDrawable(this, R.drawable.btn_toolbar_active)
            }
            Log.d(TAG, "scrollY: $scrollY")
        }

        btnBack.setOnClickListener {
            startActivity(Intent(this@LelangActivity, DashboardActivity::class.java))
        }

        btnShare.setOnClickListener {
            val shareIntent = Intent()
            val pesan = "Yuk ikutan lelang baik Aksiberbagi.com, Bagikan kemulian dengan lelang baik"
            val urlProgram = "https://aksiberbagi.com/lelang-baik"
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, pesan)
            shareIntent.putExtra(Intent.EXTRA_TEXT, urlProgram)
            shareIntent.type = "text/plain"
            shareIntent.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            startActivity(Intent.createChooser(shareIntent, "Bagikan flashsale dengan kemulian bersedekah"))
        }


        val imageLelang : ImageView = findViewById(R.id.toolbarImage)
        Glide.with(this).load(urlImage).apply(options).into(imageLelang)
        val nominalText : TextView = findViewById(R.id.nominalText)
        nominalText.text = "$nominalLelang Paket terdonasikan"
        val textTerdonasikan : TextView = findViewById(R.id.terdonasikanJumlahText)
        textTerdonasikan.text = terdonasikan

        val countDown: DynamicCountDownView = findViewById(R.id.normalCountDownLelang)
        countDown.timerTextBackgroundTintColor = ContextCompat.getColor(
            this,
            R.color.colorOrange
        )
        val timer = 3600
        countDown.initTimer(timer)
        countDown.startTimer()

        val judulText : TextView = findViewById(R.id.judulText)
        judulText.text = judulLelang

        val judulTextProgram : TextView = findViewById(R.id.judulProgramText)
        judulTextProgram.text = judulLelangProgram


        val webViewFlashsale : WebView = findViewById(R.id.webviewCeritaFlashsale)
        val url = "https://aksiberbagi.com/apk/keterangan/$idLelangProgram"
        webViewFlashsale.loadUrl(url)

        val dialogPembayaran = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view : View = layoutInflater.inflate(R.layout.dialog_donasi_lelang, null)
        dialogPembayaran.setContentView(view)

        val btnPembayaran : Button = findViewById(R.id.donasiBtn)
        btnPembayaran.setOnClickListener {
            dialogPembayaran.show()
        }

        getKoneksi(retrivedToken!!, this, view)

    }

    private fun getKoneksi(tokenValue: String, context: LelangActivity, view: View){
        ApiService.getKoneksi(tokenValue).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                getPilihNominalLelang(tokenValue, context, view)
                getPilihanPembayaran(tokenValue, context, view)
            }

            override fun onError(anError: ANError?) {
                val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                if(anError?.errorDetail!!.equals("connectionError")){
                    val toast = Toast.makeText(
                        this@LelangActivity,
                        "Ada masalah dengan Koneksi Internet Anda",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    return
                }else if(apiError?.message.equals("Token expired")){
                    refreshToken(tokenValue, context, view)
                }else{
                    Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                    Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                    Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
                }

            }
        })
    }

    private fun refreshToken(
        tokenValue: String?,
        context: LelangActivity,
        view: View
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
                                getKoneksi(token,context,view)
                            }
                        } else if (response?.getString("message")
                                .equals("Token expired berhasil di refresh")
                        ) {
                            val token: String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(token,context,view)
                            }
                        } else {
                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                    val intent = Intent(
                                        this@LelangActivity,
                                        IntroActivity::class.java
                                    )
                                    startActivity(intent)
                                }, 2500)
                            }
                        }

                    } catch (e: JSONException) {
                        val toast = Toast.makeText(
                            this@LelangActivity,
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
                                this@LelangActivity,
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

    private fun getPilihNominalLelang(tokenValue: String, context: LelangActivity, view: View){
        val sharedPreference: Preferences = Preferences(this)
        val idLelang = sharedPreference.getValueString("idLelang")
        ApiService.getLelangNominal(tokenValue, idLelang).getAsJSONObject(object: JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                val dataArray = response?.getJSONArray("data")
                if (dataArray?.length()!! > 0){
                    for (i in 0 until dataArray.length()){
                        val item = dataArray.getJSONObject(i)
                        val idNominal = item?.getString("id")
                        val namaNominal = item?.getString("nama_nominal_flash_sale")
                        val nominalNominal = item?.getString("nominal_flash_sale")
                        val nominalFormated = Converter.rupiah(nominalNominal!!.toDouble())
                        val jenisNominal = "$namaNominal $nominalFormated"
                        arrayIdNominal.add(idNominal!!)
                        arrayNamaNominal.add(jenisNominal!!)
                    }
                    val spinner = view.findViewById<Spinner>(R.id.spinnerJenisNominal)
                    val adapterNominal = ArrayAdapter(
                        this@LelangActivity,
                        R.layout.list_pilih_program_dropdown,
                        arrayNamaNominal
                    )
                    spinner.adapter = adapterNominal
                }
            }

            override fun onError(anError: ANError?) {
                Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
            }
        })
    }

    private fun getPilihanPembayaran(tokenValue: String, context: LelangActivity, view: View){
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
                        arrayIdPembayaran.add(idPembayaran!!)
                        arrayNamaPembayaran.add(namaPembayaran!!)
                    }
                }
                if (jsonArrayBank?.length()!! > 0) {
                    for (i in 0 until jsonArrayBank.length()) {
                        val item = jsonArrayBank.getJSONObject(i)
                        val idPembayaran = item?.getString("tblbank_id")
                        val namaPembayaran = item?.getString("tblbank_nama")
                        arrayIdPembayaran.add(idPembayaran!!)
                        arrayNamaPembayaran.add(namaPembayaran!!)
                    }
                }
                val spinner = view.findViewById<Spinner>(R.id.spinnerPilihPembayaran)
                val adapterPembayaran = ArrayAdapter(
                    this@LelangActivity,
                    R.layout.list_pilih_program_dropdown,
                    arrayNamaPembayaran
                )
                spinner.adapter = adapterPembayaran
            }

            override fun onError(anError: ANError?) {

                Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
            }
        })
    }
}