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
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.model.BerandaProgramAll
import com.aksiberbagi.donatur.presenter.ProgramAllAdapter
import com.aksiberbagi.donatur.services.ApiError
import com.aksiberbagi.donatur.services.ApiService
import com.aksiberbagi.donatur.services.Preferences
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.facebook.shimmer.ShimmerFrameLayout
import org.json.JSONException
import org.json.JSONObject

class ProgramAllActivity : AppCompatActivity() {
    private val arrayProgramAll = ArrayList<BerandaProgramAll>()
    private val TAG = "Program All"
    private var totalPage = 0
    private var loadedPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.program_all_activity)
        val sharedPreference: Preferences = Preferences(this)
        val toolbar: Toolbar = findViewById(R.id.upAppbarAllProgram)
        toolbar.title = "Sudahkah sedekah hari ini ?"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE)
        var mainMenuAll = findViewById<RecyclerView>(R.id.recyclerProgramAllSearch)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")
        val shimmerLayout: ShimmerFrameLayout = findViewById(R.id.shimmerProgramAll)
        shimmerLayout.startShimmer()
        mainMenuAll.visibility = View.GONE

        getKoneksi(retrivedToken, mainMenuAll, this)


        val layoutScroll : NestedScrollView = findViewById(R.id.layoutProgram)
        layoutScroll.viewTreeObserver.addOnScrollChangedListener {
            val scrollY: Int = layoutScroll.scrollY
            Log.d(TAG, "nilai Y: $scrollY")
            if (scrollY > 900){
                getNextProgram(retrivedToken, mainMenuAll)
            }
        }

        toolbar.setNavigationOnClickListener{startActivity(Intent(this@ProgramAllActivity, DashboardActivity::class.java))}

    }

    private fun getKoneksi(tokenValue: String?, view: RecyclerView, context: ProgramAllActivity){
        ApiService.getKoneksi(tokenValue).getAsJSONObject(object : JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                getAllprogram(tokenValue, view, context)
            }

            override fun onError(anError: ANError?) {
                val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                if(anError?.errorDetail!!.equals("connectionError")){
                    val toast = Toast.makeText(
                        this@ProgramAllActivity,
                        "Ada masalah dengan Koneksi Internet Anda",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    return
                }else{
                    refreshToken(tokenValue,view, context)
                }
            }

        })
    }

    private fun refreshToken(tokenValue: String?, view: RecyclerView, context: ProgramAllActivity){
        val header : String? = tokenValue
        val sharedPreference: Preferences = Preferences(this)
        try {
            ApiService.postRefreshToken(header).getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        if (response?.getString("message").equals("Refresh berhasil")){
                            val token : String? = response?.getString("token")
                            //save token
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(token, view,context)
                            }
                        }else if(response?.getString("message").equals("Token expired berhasil di refresh")){
                            val token : String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(token, view,context)
                            }
                        }else{
                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                    val intent = Intent(this@ProgramAllActivity, IntroActivity::class.java)
                                    startActivity(intent)
                                }, 2500)
                            }
                        }

                    }catch (e : JSONException){
                        val toast = Toast.makeText(
                            this@ProgramAllActivity,
                            "Invalid Json",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                }
                override fun onError(anError: ANError?) {
                    Looper.myLooper()?.let {
                        Handler(it).postDelayed({
                            val intent = Intent(this@ProgramAllActivity, IntroActivity::class.java)
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

    private fun getAllprogram(tokenValue: String?, view: RecyclerView, context: ProgramAllActivity){
        ApiService.getAllProgram(tokenValue).getAsJSONObject(object : JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                val jsonObject =response?.getJSONObject("data")
                val totalPaginate = jsonObject?.getString("last_page")
                totalPage = totalPaginate!!.toInt() + 1
                val jsonArray =jsonObject?.getJSONArray("data")
                if (jsonArray?.length()!! > 0){
                    for(i in 0 until jsonArray.length()){
                        val item = jsonArray.getJSONObject(i)
                        val idProgram = item?.getString("tblprogram_id")
                        val img = item?.getString("thumbnail_url")
                        val judul = item?.getString("tblprogram_judul")
                        val link = item?.getString("tblprogram_namalink")
                        val dataCabang = item?.getJSONObject("cabang")
                        val volunter: String? = dataCabang?.getString("tblcabang_nama")
                        val capaian = item?.getString("capaian_donasi")
                        val sisaHari = item?.getString("sisa_hari")
                        val startProgram = item?.getString("tanggal_mulai_donasi")
                        val targetNominal: String? = item?.getString("tblprogram_isiantargetnominal")
                        var progressProgram: Int? = item?.getInt("progress")
                        var targetDonasi : String?
                        if (targetNominal == "null" || targetNominal == "0" ){
                            targetDonasi = "100"
                        }else{
                            targetDonasi = item?.getString("target_nominal")
                        }
                        val nav = "ProgramAll"
                        arrayProgramAll.add(BerandaProgramAll(idProgram,img,judul,volunter,capaian,sisaHari, startProgram,progressProgram,targetDonasi, nav, link))
                        val myAdapterAll = ProgramAllAdapter(arrayProgramAll,this@ProgramAllActivity)
                        view.layoutManager = LinearLayoutManager(this@ProgramAllActivity)
                        view.adapter = myAdapterAll
                        view.visibility = View.VISIBLE
                        val shimmer = context.findViewById<ShimmerFrameLayout>(R.id.shimmerProgramAll)
                        shimmer.stopShimmer()
                        shimmer.visibility = View.GONE
                    }
                }
            }

            override fun onError(anError: ANError?) {
                Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
            }

        })
    }

    private fun getNextProgram(tokenValue: String?, view: RecyclerView){
        if (loadedPage < totalPage){
            loadedPage += 1
            ApiService.getNextPageProgram(tokenValue, loadedPage.toString()).getAsJSONObject(object: JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    val jsonObject =response?.getJSONObject("data")
                    val jsonArray =jsonObject?.getJSONArray("data")
                    if (jsonArray?.length()!! > 0){
                        for(i in 0 until jsonArray.length()){
                            val item = jsonArray.getJSONObject(i)
                            val idProgram = item?.getString("tblprogram_id")
                            val img = item?.getString("thumbnail_url")
                            val judul = item?.getString("tblprogram_judul")
                            val link = item?.getString("tblprogram_namalink")
                            val volunter: String? = "Aksiberbagi.com"
                            val capaian = item?.getString("capaian_donasi")
                            val sisaHari = item?.getString("sisa_hari")
                            val startProgram = item?.getString("tanggal_mulai_donasi")
                            val targetNominal: String? = item?.getString("tblprogram_isiantargetnominal")
                            var progressProgram: Int? = item?.getInt("progress")
                            var targetDonasi : String?
                            if (targetNominal == "null" || targetNominal == "0" ){
                                targetDonasi = "100"
                            }else{
                                targetDonasi = item?.getString("target_nominal")
                            }
                            val nav = "ProgramAll"
                            arrayProgramAll.add(BerandaProgramAll(idProgram,img,judul,volunter,capaian,sisaHari, startProgram,progressProgram,targetDonasi, nav, link))
                            val myAdapterAll = ProgramAllAdapter(arrayProgramAll,this@ProgramAllActivity)
                            view.layoutManager = LinearLayoutManager(this@ProgramAllActivity)
                            view.adapter = myAdapterAll
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                    Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                    Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
                }
            })
        }else{
            return@getNextProgram
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ProgramAllActivity, DashboardActivity::class.java ))
    }
}