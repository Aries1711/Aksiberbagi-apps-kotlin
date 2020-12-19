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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.ModelDonasiRutin
import com.inddevid.aksiberbagi_donatur.presenter.DonasiRutinListAdapter
import com.inddevid.aksiberbagi_donatur.services.ApiError
import com.inddevid.aksiberbagi_donatur.services.ApiService
import com.inddevid.aksiberbagi_donatur.services.Preferences
import org.json.JSONException
import org.json.JSONObject

class DonasiRutinActivity : AppCompatActivity() {
    private val TAG = "Donasi Rutin"
    private val arrayDonasiRutinList = ArrayList<ModelDonasiRutin>()
    private val idProgram: ArrayList<Int> = arrayListOf(0)
    private val programJudul: ArrayList<String> = arrayListOf("Pilih program favorit anda")

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
        val layoutList: ConstraintLayout = findViewById(R.id.layoutList)
        val layoutCreate: ConstraintLayout = findViewById(R.id.createLayout)
        val shimmer: ShimmerFrameLayout = findViewById(R.id.shimmerDonasiRutin)
        layoutShimmer.visibility = View.VISIBLE
        shimmer.startShimmer()

        getKoneksi(retrivedToken, this@DonasiRutinActivity)

        //
        val dropDownProgram : Spinner = findViewById(R.id.spinerProgram)

        val frekuensiItems = listOf("Harian", "Mingguan", "Bulanan")
        val frekuensiItemsPilihan = listOf("harian", "mingguan", "bulanan")
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

        val btnSubmit: Button = findViewById(R.id.btnSubmit)

        //btn tambah donasi rutin list
        val btnTambah: FloatingActionButton = findViewById(R.id.floatingTambahDonasiRutin)
        btnTambah.setOnClickListener{
            layoutList.visibility = View.GONE
            layoutCreate.visibility = View.VISIBLE
        }
        //set layout hidden untuk frekuensi yang dipilih
        val layoutB : LinearLayout = findViewById(R.id.layoutBulanan)
        val layoutM : LinearLayout = findViewById(R.id.layoutMingguan)


        var idProgramPilihan: Int = 0
        dropDownProgram.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                idProgramPilihan = idProgram[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        var rentangWaktuPilihan = ""
        dropDownFrekuensi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                rentangWaktuPilihan  = frekuensiItemsPilihan[p2]
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

        var rentangWaktuMingguanPilihan = ""
        dropDownMingguan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                rentangWaktuMingguanPilihan = frekuensiMingguans[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        var rentangWaktuBulanan = ""
        dropDownBulanan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                rentangWaktuBulanan = frekuensiBulanans[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        val helperProgram: TextView = findViewById(R.id.helperProgram)
        btnSubmit.setOnClickListener {
            if(idProgramPilihan == 0 ){
                helperProgram.visibility = View.VISIBLE
                helperProgram.text = "Pilih program penggalangan"
                return@setOnClickListener
            }else{
                if(rentangWaktuPilihan == "bulanan"){
                    sharedPreference.save("donasiRutinProgram", idProgramPilihan )
                    sharedPreference.save("donasiRutinRentangWaktu", rentangWaktuPilihan)
                    sharedPreference.save("donasiRutinOpsiWaktu", rentangWaktuBulanan)
                    postDonasiRutin(retrivedToken, this)
                }else if(rentangWaktuPilihan == "mingguan"){
                    sharedPreference.save("donasiRutinProgram", idProgramPilihan )
                    sharedPreference.save("donasiRutinRentangWaktu", rentangWaktuPilihan)
                    sharedPreference.save("donasiRutinOpsiWaktu", rentangWaktuMingguanPilihan)
                    postDonasiRutin(retrivedToken, this)
                }else{
                    sharedPreference.save("donasiRutinProgram", idProgramPilihan )
                    sharedPreference.save("donasiRutinRentangWaktu", rentangWaktuPilihan)
                    postDonasiRutin(retrivedToken, this)
                }
            }
        }

    }

    private fun postDonasiRutin(tokenValue: String?, context: DonasiRutinActivity){
        val sharedPreference: Preferences = Preferences(this)
        val body = JSONObject()
        body.put("rentang_waktu", sharedPreference.getValueString("donasiRutinRentangWaktu"))
        body.put("opsi_rentang_waktu", sharedPreference.getValueString("donasiRutinOpsiWaktu"))
        body.put("program_id", sharedPreference.getValueInt("donasiRutinProgram"))
        ApiService.postDonasiRutin(tokenValue, body).getAsJSONObject(object : JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                if(response?.getString("message").equals("Donasi rutin berhasil disimpan")){
                    val toast = Toast.makeText(
                        this@DonasiRutinActivity,
                        "Pendaftaran donasi rutin berhasil, menunggu verifikasi dari admin",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    arrayDonasiRutinList.clear()
                    getDonasiRutinList(tokenValue, context)
                }else{
                    val message = response?.getString("message")
                    val toast = Toast.makeText(
                        this@DonasiRutinActivity,
                        "Kesalahan sistem $message",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    return
                }
            }

            override fun onError(anError: ANError?) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getKoneksi(tokenValue: String?, context: DonasiRutinActivity){
        ApiService.getKoneksi(tokenValue).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                getDonasiRutinList(tokenValue, context)
                getProgramAll(tokenValue,context)
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
                if (response?.getString("message").equals("Donasi rutin berhasil diambil")){
                    val jsonArray = response?.getJSONArray("data")
                    for(i in 0 until jsonArray!!.length()){
                        val dataDonasiRutin = jsonArray.getJSONObject(i)
                        val dataProgram = dataDonasiRutin.getJSONObject("program_nama")
                        val idDonasiRutin = dataDonasiRutin?.getString("id")
                        val judulProgram = dataProgram?.getString("tblprogram_judul")
                        val rentangWaktu = dataDonasiRutin.getString("rentang_waktu")
                        val opsiWaktu = dataDonasiRutin.getString("opsi_rentang_waktu")
                        val imageProgram = dataProgram?.getString("thumbnail_url")
                        val status = dataDonasiRutin?.getString("status")
                        arrayDonasiRutinList.add(ModelDonasiRutin(idDonasiRutin,judulProgram,rentangWaktu,opsiWaktu,imageProgram,status))
                    }
                    val myAdapterDonasi = DonasiRutinListAdapter(arrayDonasiRutinList, this@DonasiRutinActivity)
                    val recyclerView = context.findViewById<RecyclerView>(R.id.recyclerDonasiRutin)
                    recyclerView.layoutManager = LinearLayoutManager(this@DonasiRutinActivity)
                    recyclerView.adapter = myAdapterDonasi
                    //close layout lain dan show layout
                    val layoutCreate : ConstraintLayout = context.findViewById(R.id.createLayout)
                    val layoutShimmer: ConstraintLayout = context.findViewById(R.id.layoutShimmer)
                    val shimmer: ShimmerFrameLayout = context.findViewById(R.id.shimmerDonasiRutin)
                    shimmer.stopShimmer()
                    val layoutList: ConstraintLayout = context.findViewById(R.id.layoutList)
                    layoutList.visibility = View.VISIBLE
                    layoutShimmer.visibility = View.GONE
                    layoutCreate.visibility = View.GONE
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

    private fun getProgramAll(tokenValue: String?, context: DonasiRutinActivity){
        ApiService.getAllProgram(tokenValue).getAsJSONObject(object: JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                if(response?.getString("message").equals("Data program berhasil diambil")){
                    val dataObject = response?.getJSONObject("data")
                    val dataArray = dataObject?.getJSONArray("data")
                    for (i in 0 until dataArray!!.length()){
                        val dataProgram = dataArray.getJSONObject(i)
                        val idProgramData = dataProgram.getString("tblprogram_id").toInt()
                        val judulProgramData = dataProgram.getString("tblprogram_judul")
                        idProgram.add(idProgramData)
                        programJudul.add(judulProgramData)
                    }

                    val adapterProgram = ArrayAdapter(
                        this@DonasiRutinActivity,
                        R.layout.list_pilih_program_dropdown,
                        programJudul
                    )

                    val dropDownProgram : Spinner = context.findViewById(R.id.spinerProgram)
                    dropDownProgram.adapter = adapterProgram

                }
            }

            override fun onError(anError: ANError?) {
                getKoneksi(tokenValue, context)
            }

        })
    }

    fun gone(view: View){
        view.visibility = View.GONE
    }

    fun show(view: View){
        view.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        var prevNavigate: String? = intent.getStringExtra("prevNavigate")
        if(prevNavigate == "Pengaturan"){
            startActivity(Intent(this@DonasiRutinActivity, PengaturanActivity::class.java))
        }else{
            startActivity(Intent(this@DonasiRutinActivity, DashboardActivity::class.java))
        }
    }

}