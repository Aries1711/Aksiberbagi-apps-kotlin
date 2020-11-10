package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
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
import com.inddevid.aksiberbagi_donatur.model.BerandaProgramAll
import com.inddevid.aksiberbagi_donatur.presenter.BerandaProgramAllAdapter
import com.inddevid.aksiberbagi_donatur.services.ApiService
import com.inddevid.aksiberbagi_donatur.services.Preferences
import org.json.JSONException
import org.json.JSONObject

class ProgramAllActivity : AppCompatActivity() {
    private val arrayProgramAll = ArrayList<BerandaProgramAll>()
    private val TAG = "Program All"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.program_all_activity)
        val sharedPreference: Preferences = Preferences(this)
        val toolbar: Toolbar = findViewById(R.id.upAppbarAllProgram)
        toolbar.title = "Program Galang Dana"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        var mainMenuAll = findViewById(R.id.recyclerProgramAllSearch) as RecyclerView
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")
        getKoneksi(retrivedToken, mainMenuAll)

        toolbar.setNavigationOnClickListener{startActivity(Intent(this@ProgramAllActivity, DashboardActivity::class.java))}

    }

    private fun getKoneksi(tokenValue: String?, view: RecyclerView){
        ApiService.getKoneksi(tokenValue).getAsJSONObject(object : JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                getAllprogram(tokenValue, view)
            }

            override fun onError(anError: ANError?) {
                refreshToken(tokenValue,view)
            }

        })
    }

    private fun refreshToken(tokenValue: String?, view: RecyclerView){
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
                                getKoneksi(token, view)
                            }
                        }else if(response?.getString("message").equals("Token expired berhasil di refresh")){
                            val token : String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(token, view)
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

    private fun getAllprogram(tokenValue: String?, view: RecyclerView){
        ApiService.getAllProgram(tokenValue).getAsJSONObject(object : JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                val jsonArray =response?.getJSONArray("data")
                if (jsonArray?.length()!! > 0){
                    for(i in 0 until jsonArray.length()){
                        val item = jsonArray.getJSONObject(i)
                        val idProgram = item?.getString("tblprogram_id")
                        val img = item?.getString("thumbnail_url")
                        val judul = item?.getString("tblprogram_judul")
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
                        arrayProgramAll.add(BerandaProgramAll(idProgram,img,judul,volunter,capaian,sisaHari, startProgram,progressProgram,targetDonasi))
                        val myAdapterAll = BerandaProgramAllAdapter(arrayProgramAll,this@ProgramAllActivity)
                        view.layoutManager = LinearLayoutManager(this@ProgramAllActivity)
                        view.adapter = myAdapterAll
                    }
                }
            }

            override fun onError(anError: ANError?) {

            }

        })
    }
}