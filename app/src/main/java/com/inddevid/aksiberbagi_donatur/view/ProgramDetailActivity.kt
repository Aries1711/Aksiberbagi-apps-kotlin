package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.ListDonasi
import com.inddevid.aksiberbagi_donatur.presenter.ListDonasiAdapter
import com.inddevid.aksiberbagi_donatur.services.ApiService
import com.inddevid.aksiberbagi_donatur.services.Converter
import com.inddevid.aksiberbagi_donatur.services.Preferences
import org.json.JSONException
import org.json.JSONObject


class ProgramDetailActivity : AppCompatActivity() {

    private val arrayDonatur = ArrayList<ListDonasi>()
    private val TAG = "Program Detail"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.program_detail_activity)

        val sharedPreference: Preferences = Preferences(this)
        val imageProgram: ImageView = findViewById(R.id.toolbarImage)
        var allDonasi = findViewById<RecyclerView>(R.id.totalDonasiRecycler)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")
        val idProgram: String? = sharedPreference.getValueString("idProgram")
        val textJumlahDonatur: TextView = findViewById(R.id.jumlahDonasiTextProgram)
        getKoneksi(retrivedToken, idProgram ,allDonasi, textJumlahDonatur)

        //appbar background Image options
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(900, 470)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

        //deklarasi value tampilan mulai dari gambar judul dll
            //gambar
            val imgProgram: String? = sharedPreference.getValueString("img")
            val judulProgram: String? = sharedPreference.getValueString("judul")
            val capaianProgram: String? = sharedPreference.getValueString("capaian")
            val sisaHariProgram: String? =sharedPreference.getValueString("sisaHari")
            val tanggalMulaiProgram: String? =sharedPreference.getValueString("tanggalMulai")
            Glide.with(this).load(imgProgram).apply(options).into(imageProgram)
            //judul
            var textTitle: TextView = findViewById(R.id.titleTextProgram)
            textTitle.text = judulProgram
            //capaian donasi
            var textCapaian: TextView = findViewById(R.id.capaianTextProgram)
            textCapaian.text = Converter.rupiah(capaianProgram?.toDouble())
            //target Donasi
            var textTargetCapain: TextView = findViewById(R.id.targetTextProgram)
            textTargetCapain.text = "\u221E"
            //sisa hari
            var textSisaHari: TextView = findViewById(R.id.sisaHariTextProgram)
            //tanggal mulai
            var textStart: TextView = findViewById(R.id.tanggalMulaiTextProgram)
            textStart.text = tanggalMulaiProgram
            if (sisaHariProgram == "Tidak terbatas") run {
                textSisaHari.text = "\u221E"
            }else{
                textSisaHari.text = sisaHariProgram
            }


        val toolbar: Toolbar = findViewById(R.id.upAppbarProgramDetail)
        toolbar.setNavigationOnClickListener{startActivity(
            Intent(
                this@ProgramDetailActivity,
                DashboardActivity::class.java
            )
        )}
        val myWebView: WebView = findViewById(R.id.keteranganProgram)
        myWebView.loadUrl("https://aksiberbagi.com/sedekahair")

        //deklarasi btn Donasi dan dialog swipe
        val btnDonasi: Button = findViewById(R.id.donasiBtn)
        val dialogPembayaran = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view : View  = layoutInflater.inflate(R.layout.dialog_pembayaran_donasi, null)
        dialogPembayaran.setContentView(view)
        val textNominalLayout: TextInputLayout = view.findViewById(R.id.nominalDonasiLayout)
        val textNominalDonasi : TextInputEditText = view.findViewById(R.id.nominalDonasi)
        val btnDonasiClose: LinearLayout = view.findViewById(R.id.btnCollapse)
        val btnPilihBayar: TextView = view.findViewById(R.id.pilihPembayaranBtn)
        val imgPembayaran: ImageView = view.findViewById(R.id.imgBank)
        val textPembayaran: TextView = view.findViewById(R.id.titleJenisPembayaran)
        val btnLanjutPembayaran: Button = view.findViewById(R.id.donasiLanjutPembayaran)
        val pilihNominal: Spinner = view.findViewById(R.id.spinerPilihNominal)

        //Array pilihan nominal donasi
        val nominalItems = arrayListOf("Pilih Nominal Donasi","Rp 50000 Semua Bisa Sedekah",
            "Rp 100000 Sedekah Pilihan", "Rp 250000 Sedekah Terbaik", "Rp 500000 Sedekah Berkah",
            "Rp 1000000 Sedekah Pilihan", "Masukkan Nominal Lain")
        val adapterNominal = ArrayAdapter(this, R.layout.list_pilih_program_dropdown, nominalItems)
        pilihNominal.adapter = adapterNominal

        //set spinner untuk set keadaan dan nilai variabel yg terpengaruh spinner
        var spinnerPilihNominal : String? = ""

        pilihNominal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if ( nominalItems[p2] == "Masukkan Nominal Lain"){
                    spinnerPilihNominal = nominalItems[p2]
                    show(textNominalLayout)
                }else{
                    gone(textNominalLayout)
                    spinnerPilihNominal = nominalItems[p2]
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        hidden(imgPembayaran)
        gone(textNominalLayout)

        // Receiver intent data
        var dialogPembayaranAktif: String? = intent.getStringExtra("dialogAktif")
        var nominalPembayaran: String? = intent.getStringExtra("nominalDonasi")
        var pilihanPembayaran: String? = intent.getStringExtra("pilihanPembayaran")
        var imgPilihan: String? = intent.getStringExtra("imagePilihan")
        var spinner: String? = intent.getStringExtra("spinner")

        if (spinner == "Masukkan Nominal Lain"){
            pilihNominal.setSelection(adapterNominal.getPosition("Masukkan Nominal Lain"))
        }else{
            pilihNominal.setSelection(adapterNominal.getPosition("Pilih Nominal Donasi"))
        }

        if (dialogPembayaranAktif == "true"  && pilihanPembayaran != ""){
            dialogPembayaran.show()
            show(textNominalLayout)
            textPembayaran.text = pilihanPembayaran
            show(imgPembayaran)
            Glide.with(this).load(imgPilihan).into(imgPembayaran)
            textNominalDonasi.text = nominalPembayaran?.toEditable()
        }else if(dialogPembayaranAktif == "true"){
            dialogPembayaran.show()
            textNominalDonasi.text = nominalPembayaran?.toEditable()
        }else{
            dialogPembayaran.dismiss()
        }

        btnLanjutPembayaran.setOnClickListener { startActivity(Intent(this@ProgramDetailActivity, InvoiceActivity::class.java)) }

        btnDonasi.setOnClickListener {
            dialogPembayaran.show()
//            getPilihanNominal(retrivedToken, view)
        }

        btnDonasiClose.setOnClickListener {
            dialogPembayaran.dismiss()
        }


        //deklarasi variabel untuk intent ke menu pilih pembayaran
        btnPilihBayar.setOnClickListener {
            val mIntent = Intent(this, PilihPembayaranActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("nominal", textNominalDonasi.text.toString())
            mBundle.putString("spinnerValue", spinnerPilihNominal)
            mIntent.putExtras(mBundle)
            startActivity(mIntent)
        }


        val doaBtn: Switch = view.findViewById(R.id.doaDonasiBtn)
        val inputLayoutDoa: TextInputLayout = view.findViewById(R.id.doaDonasi)
        gone(inputLayoutDoa)
        doaBtn?.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
            if (message == "Switch1:ON"){
                show(inputLayoutDoa)
            }else{
                gone(inputLayoutDoa)
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun gone(view: View){
        view.visibility = View.GONE
    }

    fun show(view: View){
        view.visibility = View.VISIBLE
    }

    private fun hidden(view: View){
        view.visibility = View.INVISIBLE
    }
    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    private fun getKoneksi(tokenValue: String?, idProgram: String?, donatur: RecyclerView, jumlahDonatur: TextView){
        val header: String? = tokenValue
        ApiService.getKoneksi(header).getAsJSONObject(object: JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                getDonaturProgramA(tokenValue, idProgram ,donatur, jumlahDonatur)
            }
            override fun onError(anError: ANError?) {
                refreshToken(tokenValue ,idProgram ,donatur, jumlahDonatur)
            }

        })
    }

    private fun refreshToken(tokenValue: String?,idProgram: String?, donatur: RecyclerView, jumlahDonatur: TextView){
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
                                getDonaturProgramA(tokenValue, idProgram ,donatur, jumlahDonatur)
                            }
                        }else if(response?.getString("message").equals("Token expired berhasil di refresh")){
                            val token : String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getDonaturProgramA(tokenValue, idProgram ,donatur,jumlahDonatur)
                            }
                        }else{
                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                    val intent = Intent(this@ProgramDetailActivity, IntroActivity::class.java)
                                    startActivity(intent)
                                }, 2500)
                            }
                        }

                    }catch (e : JSONException){
                        val toast = Toast.makeText(
                            this@ProgramDetailActivity,
                            "Invalid Json",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                }
                override fun onError(anError: ANError?) {
                    Looper.myLooper()?.let {
                        Handler(it).postDelayed({
                            val intent = Intent(this@ProgramDetailActivity, IntroActivity::class.java)
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

    private fun getDonaturProgramA(tokenValue: String?, idProgram: String?, donatur: RecyclerView, jumlahDonatur: TextView){
        ApiService.getDonatur(tokenValue,idProgram).getAsJSONObject(object:JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                val jsonArray =response?.getJSONArray("data")
                val totalDonatur: String? = response?.getString("total_donatur")
                jumlahDonatur.text = "($totalDonatur)"
                if (jsonArray?.length()!! > 0){
                    for(i in 0 until 5){
                        val item = jsonArray.getJSONObject(i)
                        val img: String? = "https://aksiberbagi.com/assets/images/user_akber.png"
                        val namaDonatur : String? = item?.getString("tbldonatur_nama")
                        val donasiDonatur: Double? = item?.getString("tbldonasi_nominal")!!.toDouble()
                        val doaDonatur: String? = item?.getString("tbldonasi_doa")
                        val waktuDonasi: String? = item?.getString("tbldonasi_tglinsert")
                        arrayDonatur.add(ListDonasi(img, namaDonatur,donasiDonatur,doaDonatur, waktuDonasi))
                        val myAdapterListDonasi = ListDonasiAdapter(arrayDonatur, applicationContext)
                        donatur.layoutManager = LinearLayoutManager(applicationContext)
                        donatur.adapter = myAdapterListDonasi
                    }
                }
            }
            override fun onError(anError: ANError?) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getPilihanNominal(tokenValue: String?, view: View){

    }
}

