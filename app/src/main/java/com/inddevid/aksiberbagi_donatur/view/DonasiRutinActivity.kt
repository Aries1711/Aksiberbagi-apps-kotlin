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
import androidx.constraintlayout.widget.ConstraintLayout
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.facebook.shimmer.ShimmerFrameLayout
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.services.ApiError
import com.inddevid.aksiberbagi_donatur.services.ApiService
import com.inddevid.aksiberbagi_donatur.services.Preferences
import org.json.JSONException
import org.json.JSONObject

class DonasiRutinActivity : AppCompatActivity() {
    private val TAG = "Donasi Rutin"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donasi_rutin_activity)

        val sharedPreference: Preferences = Preferences(this)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")
        val toolbar: Toolbar = findViewById(R.id.upAppbarDonasiRutin)
        toolbar.title = "Donasi Rutin"
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener{
            val mIntent = Intent(this, DashboardActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("berandaAktif", "true")
            mIntent.putExtras(mBundle)
            startActivity(mIntent)
        }

        val layoutShimmer: ConstraintLayout = findViewById(R.id.layoutShimmer)
        val shimmer: ShimmerFrameLayout = findViewById(R.id.shimmerDonasiRutin)
        layoutShimmer.visibility = View.VISIBLE
        shimmer.startShimmer()

        getKoneksi(retrivedToken, this@DonasiRutinActivity)

//         set pilihan program dropdown
        val items = listOf("Wujudkan Pondok Pesantren Tahfidz Qur’an Hadist Internasional Pekanbaru, Riau", "Simpan Hartamu dilangit, Sedekah Jariyah Atas Nama Keluarga", "Bangun Rumah di Surga: Sedekah Jariah Renovasi Masjid Pelosok", "Android")
        val adapterProgram = ArrayAdapter(this, R.layout.list_pilih_program_dropdown, items)
        val dropDownProgram : Spinner = findViewById(R.id.spinerProgram)
        dropDownProgram.adapter = adapterProgram

        val frekuensiItems = listOf("Harian", "Mingguan", "Bulanan")
        val adapterFrekuensi = ArrayAdapter(this, R.layout.list_pilih_program_dropdown, frekuensiItems)
        val dropDownFrekuensi : Spinner = findViewById(R.id.spinerFrekuensi)
        dropDownFrekuensi.adapter = adapterFrekuensi

        //set val spinner untuk frekuensi yang dipilih
        val frekuensiMingguans = listOf("senin", "selasa", "rabu", "kamis", "jumat", "minggu")
        val dropDownMingguan : Spinner = findViewById(R.id.frekuensiMingguan)
        val adapterMingguan = ArrayAdapter(this, R.layout.list_pilih_program_dropdown, frekuensiMingguans)
        dropDownMingguan.adapter = adapterMingguan

        val frekuensiBulanans = listOf("1","2","3","5","6","7","8","9","10", "11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28")
        val dropDownBulanan : Spinner = findViewById(R.id.frekuensiBulanan)
        val adapterBulanan = ArrayAdapter(this, R.layout.list_pilih_program_dropdown,frekuensiBulanans)
        dropDownBulanan.adapter = adapterBulanan

        //set layout hidden untuk frekuensi yang dipilih
        val layoutB : LinearLayout = findViewById(R.id.layoutBulanan)
        val layoutM : LinearLayout = findViewById(R.id.layoutMingguan)

        dropDownFrekuensi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (frekuensiItems[p2] == "Mingguan"){
                    show(layoutM)
                    gone(layoutB)
                }else if (frekuensiItems[p2] == "Bulanan"){
                    gone(layoutM)
                    show(layoutB)
                }else{
                    gone(layoutM)
                    gone(layoutB)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }

    private fun getKoneksi(tokenValue: String?, context: DonasiRutinActivity){
        ApiService.getKoneksi(tokenValue).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                getDonasiRutinList(tokenValue, context)
            }

            override fun onError(anError: ANError?) {
                val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                if(anError?.errorDetail!!.equals("connectionError")){
                    val toast = Toast.makeText(
                        this@DonasiRutinActivity,
                        "Ada masalah dengan Koneksi Internet Anda",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    return
                }else{
                    refreshToken(tokenValue, context)
                }
            }

        })
    }

    private fun refreshToken(tokenValue: String?, context: DonasiRutinActivity){
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
                                getKoneksi(token,context)
                            }
                        }else if(response?.getString("message").equals("Token expired berhasil di refresh")){
                            val token : String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(token,context)
                            }
                        }else{
                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                    val intent = Intent(this@DonasiRutinActivity, IntroActivity::class.java)
                                    startActivity(intent)
                                }, 2500)
                            }
                        }

                    }catch (e : JSONException){
                        val toast = Toast.makeText(
                            this@DonasiRutinActivity,
                            "Invalid Json",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                }
                override fun onError(anError: ANError?) {
                    Looper.myLooper()?.let {
                        Handler(it).postDelayed({
                            val intent = Intent(this@DonasiRutinActivity, IntroActivity::class.java)
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

    private fun getDonasiRutinList(tokenValue: String?, context: DonasiRutinActivity){
        ApiService.getDonasiRutin(tokenValue).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                if (response?.getString("message").equals("Donasi rutin tidak ditemukan")){

                }else{

                }
            }

            override fun onError(anError: ANError?) {
                val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)

                if (apiError?.message == "Donasi rutin tidak ditemukan") {
                    val layoutCreate = context.findViewById<ConstraintLayout>(R.id.createLayout)
                    val layoutShimmer: ConstraintLayout = context.findViewById(R.id.layoutShimmer)
                    val shimmer: ShimmerFrameLayout = context.findViewById(R.id.shimmerDonasiRutin)
                    layoutShimmer.visibility = View.GONE
                    shimmer.stopShimmer()
                    layoutCreate.visibility = View.VISIBLE
                }
                Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
            }

        })
    }

    fun gone(view: View){
        view.visibility = View.GONE
    }

    fun show(view: View){
        view.visibility = View.VISIBLE
    }
}