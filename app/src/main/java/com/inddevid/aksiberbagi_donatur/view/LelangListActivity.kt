package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androchef.happytimer.countdowntimer.DynamicCountDownView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.facebook.shimmer.ShimmerFrameLayout
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.BerandaLelang
import com.inddevid.aksiberbagi_donatur.presenter.ListLelangAdapter
import com.inddevid.aksiberbagi_donatur.services.ApiError
import com.inddevid.aksiberbagi_donatur.services.ApiService
import com.inddevid.aksiberbagi_donatur.services.Preferences
import org.json.JSONObject

class LelangListActivity : AppCompatActivity() {
    private val TAG = "Lelang List activity"
    private val arrayListLelang = ArrayList<BerandaLelang>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lelang_list_activity)

        //token dari preference
        val sharedPreference: Preferences = Preferences(this)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")

        val btnShare: LinearLayout = findViewById(R.id.shareLelangBtn)
        val btnBack: LinearLayout = findViewById(R.id.backLelangBtn)

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

        btnBack.setOnClickListener {
            startActivity(Intent(this@LelangListActivity, DashboardActivity::class.java ))
        }

        val countDown: DynamicCountDownView = findViewById(R.id.normalCountDownLelang)
        countDown.timerTextBackgroundTintColor = ContextCompat.getColor(
            this,
            R.color.colorOrange
        )
        val shimmerLayout : ShimmerFrameLayout = findViewById(R.id.shimmerLelangList)
        shimmerLayout.startShimmer()
        getKoneksi(retrivedToken!!, this)

        val btnProgramAll : Button = findViewById(R.id.btnAllProgram)
        btnProgramAll.setOnClickListener {
            startActivity(Intent(this@LelangListActivity, ProgramAllActivity::class.java))
        }

    }

    private fun getKoneksi(tokenValue: String, context: LelangListActivity){
        ApiService.getKoneksi(tokenValue).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                getLelangBaik(tokenValue,context)
            }

            override fun onError(anError: ANError?) {
                val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                if(anError?.errorDetail!!.equals("connectionError")){
                    val toast = Toast.makeText(
                        this@LelangListActivity,
                        "Ada masalah dengan Koneksi Internet Anda",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    return
                }else if(apiError?.message.equals("Token expired")){
//                    refreshToken(tokenValue, context, view)
                }else{
                    Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                    Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                    Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
                }

            }
        })
    }

    private fun getLelangBaik(tokenValue: String?, context: LelangListActivity){
        val header : String? = tokenValue
        ApiService.getLelang(header).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                if(response?.getString("message").equals("Data flashsale berhasil diambil")) {
                    if (response?.getString("timer")!!.toInt() >= 0) {
                        val jsonArray = response?.getJSONArray("data")
                        val timer = response?.getString("timer")!!.toInt()
                        if (jsonArray?.length()!! > 0) {
                            for (i in 0 until jsonArray.length()) {
                                val item = jsonArray.getJSONObject(i)
                                val nominalItem = item?.getJSONObject("nominal")
                                val idLelang: String? = item?.getString("id")
                                val judulLelang: String? = item?.getString("nama_flash_sale")
                                val img: String? = item?.getString("gambar_url")
                                val nominal: Double? =
                                    nominalItem?.getString("nominal_flash_sale")!!.toDouble()
                                val stokSekarang: String? = item?.getString("stok")
                                val stokAwal: String? = item?.getString("stok_awal")
                                val dataProgram = item?.getJSONObject("program")
                                val idLelangProgram = dataProgram?.getString("tblprogram_id")
                                val judulLelangProgram = dataProgram?.getString("tblprogram_judul")
                                arrayListLelang.add(
                                    BerandaLelang(
                                        idLelang,
                                        judulLelang,
                                        img,
                                        stokSekarang?.toInt(),
                                        stokAwal?.toInt(),
                                        nominal,
                                        "",
                                        idLelangProgram,
                                        judulLelangProgram
                                    )
                                )
                            }
                            val myAdapterLelang =
                                ListLelangAdapter(arrayListLelang, this@LelangListActivity)
                            val viewLelang =
                                context.findViewById<RecyclerView>(R.id.recyclerLelangAll)
                            viewLelang.layoutManager = LinearLayoutManager(
                                this@LelangListActivity
                            )
                            viewLelang.adapter = myAdapterLelang
                            val countDown: DynamicCountDownView =
                                context.findViewById(R.id.normalCountDownLelang)
                            countDown.initTimer(timer)
                            countDown.startTimer()
                            val shimmerLayout: ShimmerFrameLayout =
                                context.findViewById(R.id.shimmerLelangList)
                            shimmerLayout.stopShimmer()
                            shimmerLayout.visibility = View.GONE
                            viewLelang.visibility = View.VISIBLE
                        }
                    }else{
                        val countDown: DynamicCountDownView =
                            context.findViewById(R.id.normalCountDownLelang)
                        countDown.visibility = View.GONE
                        val shimmerLayout: ShimmerFrameLayout =
                            context.findViewById(R.id.shimmerLelangList)
                        shimmerLayout.stopShimmer()
                        shimmerLayout.visibility = View.GONE
                        val layoutUtama = context.findViewById<LinearLayout>(R.id.layoutUtama)
                        layoutUtama.visibility = View.GONE
                        val layoutNotFound: LinearLayout = context.findViewById(R.id.layoutNotfound)
                        layoutNotFound.visibility = View.VISIBLE
                    }
                }
            }

            override fun onError(anError: ANError?) {

            }

        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@LelangListActivity, DashboardActivity::class.java ))
    }
}