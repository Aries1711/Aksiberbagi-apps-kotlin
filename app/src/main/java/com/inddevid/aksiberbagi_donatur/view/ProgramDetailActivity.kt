package com.inddevid.aksiberbagi_donatur.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
import com.inddevid.aksiberbagi_donatur.services.*
import org.json.JSONException
import org.json.JSONObject
import kotlin.toString as toString1


class ProgramDetailActivity : AppCompatActivity() {

    private val arrayDonatur = ArrayList<ListDonasi>()
    private val TAG = "Program Detail"
    private val nominalItems: ArrayList<String> = arrayListOf("Pilih Nominal Donasi")
    private val idNominal: ArrayList<Int> = arrayListOf(0)
    private var btnKeteranganStatus : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.program_detail_activity)

        val sharedPreference: Preferences = Preferences(this)
        val imageProgram: ImageView = findViewById(R.id.toolbarImage)
        var allDonasi = findViewById<RecyclerView>(R.id.totalDonasiRecycler)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")
        val idProgram: String? = sharedPreference.getValueString("idProgram")
        val textJumlahDonatur: TextView = findViewById(R.id.jumlahDonasiTextProgram)

        //appbar background Image options
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(900, 475)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

        //deklarasi value tampilan mulai dari gambar judul dll
            //gambar
            val imgProgram: String? = sharedPreference.getValueString("img")
            val judulProgram: String? = sharedPreference.getValueString("judul")
            val capaianProgram: String? = sharedPreference.getValueString("capaian")
            val sisaHariProgram: String? =sharedPreference.getValueString("sisaHari")
            val tanggalMulaiProgram: String? =sharedPreference.getValueString("tanggalMulai")
            val switchAnonimValue: String? = sharedPreference.getValueString("ANONIM")
            val progresProgramValue: Int? = sharedPreference.getValueInt("progressProgram")
            val targetProgram: String? = sharedPreference.getValueString("targetProgram")

//            val toast = Toast.makeText(this, progresProgramValue.toString(), Toast.LENGTH_LONG)
//            toast.show()

            Glide.with(this).load(imgProgram).apply(options).into(imageProgram)
            //progresbar
            var progresBarProgram: ProgressBar = findViewById(R.id.progressBar)
            progresBarProgram.progress = progresProgramValue!!
            //judul
            var textTitle: TextView = findViewById(R.id.titleTextProgram)
            textTitle.text = judulProgram
            //capaian donasi
            var textCapaian: TextView = findViewById(R.id.capaianTextProgram)
            textCapaian.text = "Rp $capaianProgram"
            //target Donasi
            var textTargetCapain: TextView = findViewById(R.id.targetTextProgram)
            if(targetProgram == "100") {
                textTargetCapain.text = "\u221E"
            }else{
                val target = targetProgram.toString1()
                textTargetCapain.text = "Rp $target"
            }
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

        //deklarasi btn Donasi dan dialog swipe
        val btnDonasi: Button = findViewById(R.id.donasiBtn)
        val dialogPembayaran = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view : View  = layoutInflater.inflate(R.layout.dialog_pembayaran_donasi, null)
        dialogPembayaran.setContentView(view)
        val textNominalLayout: TextInputLayout = view.findViewById(R.id.nominalDonasiLayout)
        val textNominalDonasi : TextInputEditText = view.findViewById(R.id.nominalDonasi)
        val textDoaDonatur : TextInputEditText = view.findViewById(R.id.doaDonatur)
        val btnDonasiClose: LinearLayout = view.findViewById(R.id.btnCollapse)
        val btnPilihBayar: TextView = view.findViewById(R.id.pilihPembayaranBtn)
        val btnPilihBayarB: LinearLayout = view.findViewById(R.id.layoutImageBank)
        val btnPilihBayarC: TextView = view.findViewById(R.id.titleJenisPembayaran)
        val imgPembayaran: ImageView = view.findViewById(R.id.imgBank)
        val textPembayaran: TextView = view.findViewById(R.id.titleJenisPembayaran)
        val btnLanjutPembayaran: Button = view.findViewById(R.id.donasiLanjutPembayaran)
        val switchAnonimSet: Switch = view.findViewById(R.id.anonimDonasiBtn)
        val helperNominal : TextView = view.findViewById(R.id.helperNominal)

        val initialisasiNominal = "0"
        textNominalDonasi.text = initialisasiNominal.toEditable()
        textNominalDonasi.addTextChangedListener(NumberFormaterDot(textNominalDonasi))
//        textNominalDonasi.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(p0: Editable?) {
//                var s: String? = null
//                try {
//                    s = String.format("%,d", p0?.toString()?.toLong())
//                } catch (e: NumberFormatException) {
//                    val toast = Toast.makeText(
//                        this@ProgramDetailActivity,
//                        "Error di formator",
//                        Toast.LENGTH_LONG
//                    )
//                }
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//        })

        if (switchAnonimValue == "T"){
            switchAnonimSet.isChecked = true
        }

        switchAnonimSet.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
            if (message == "Switch1:ON"){
                sharedPreference.save("ANONIM", "T")
            }else{
                sharedPreference.save("ANONIM", "F")
            }
        }

        //ambil semua atribut nilai yang di tampilkan dari database server
        val pilihNominal: Spinner = view.findViewById(R.id.spinerPilihNominal)
        getKoneksi(retrivedToken, idProgram, allDonasi, textJumlahDonatur, pilihNominal)
        Log.d(TAG, "value pada pilihan nominal $nominalItems")
        //set spinner untuk set keadaan dan nilai variabel yg terpengaruh spinner
        var spinnerPilihNominal : String? = ""
        var spinnerNominal : Int = 0
        pilihNominal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if ( nominalItems[p2] == "Masukkan Nominal Lain"){
                    spinnerPilihNominal = nominalItems[p2]
                    spinnerNominal = idNominal[p2]
                    show(textNominalLayout)
                }else{
                    gone(textNominalLayout)
                    spinnerPilihNominal = nominalItems[p2]
                    spinnerNominal = idNominal[p2]
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
        var idPembayaran: String? = sharedPreference.getValueString("idPembayaran")
        var pilihanPembayaran: String? = intent.getStringExtra("pilihanPembayaran")
        var imgPilihan: String? = intent.getStringExtra("imagePilihan")

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

        btnDonasi.setOnClickListener {
            dialogPembayaran.show()
        }

        btnDonasiClose.setOnClickListener {
            dialogPembayaran.dismiss()
        }


        //deklarasi variabel untuk intent ke menu pilih pembayaran
        btnPilihBayar.setOnClickListener {
            val mIntent = Intent(this, PilihPembayaranActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("nominal", textNominalDonasi.text.toString1())
            mBundle.putString("spinnerValue", spinnerPilihNominal)
            mIntent.putExtras(mBundle)
            startActivity(mIntent)
        }

        btnPilihBayarB.setOnClickListener {
            val mIntent = Intent(this, PilihPembayaranActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("nominal", textNominalDonasi.text.toString1())
            mBundle.putString("spinnerValue", spinnerPilihNominal)
            mIntent.putExtras(mBundle)
            startActivity(mIntent)
        }

        btnPilihBayarC.setOnClickListener {
            val mIntent = Intent(this, PilihPembayaranActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("nominal", textNominalDonasi.text.toString1())
            mBundle.putString("spinnerValue", spinnerPilihNominal)
            mIntent.putExtras(mBundle)
            startActivity(mIntent)
        }
        //end multiple btn untuk pilih pembayaran.

        val doaBtn: Switch = view.findViewById(R.id.doaDonasiBtn)
        val inputLayoutDoa: TextInputLayout = view.findViewById(R.id.doaDonasi)
        var doaStatus = ""
        gone(inputLayoutDoa)
        doaBtn.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
            if (message == "Switch1:ON"){
                doaStatus = "True"
                show(inputLayoutDoa)
            }else{
                doaStatus = "False"
                gone(inputLayoutDoa)
                hideSoftKeyboard(this, inputLayoutDoa)
            }
        }

        //tampilan webview keterangan
        val myWebView: WebView = findViewById(R.id.keteranganProgram)
        myWebView.loadUrl("https://aksiberbagi.com/sedekahair")
        //btn lihat semua keterangan
        val btnKeteranganLihat : Button = findViewById(R.id.btnKeteranganProgram)
        val layoutKeterangan : LinearLayout = findViewById(R.id.layoutKeteranganDetail)
        btnKeteranganLihat.setOnClickListener {
            if(btnKeteranganStatus == ""){
                btnShowLayout(btnKeteranganLihat, layoutKeterangan)
            }else{
                btnHideLayout(btnKeteranganLihat, layoutKeterangan)
            }
        }

        // mengambil seluruh deklarasi data yang di perlukan untuk donasi
        var nominalDonasiDonatur = 0
        var doaDonatur = ""

        //klik button lanjut pembayaran post ke server
        btnLanjutPembayaran.setOnClickListener {
//            set deklarasi variabel yang di perlukan
            if(doaStatus == "True") doaDonatur = textDoaDonatur.text?.toString()!!

            if(spinnerNominal == 0 ){
                if(textNominalDonasi.text!!.toString() == "" ){
                    nominalDonasiDonatur = 0
                }else{
                    nominalDonasiDonatur = Integer.parseInt(textNominalDonasi.text!!.toString().replace(".", ""))
                }
            }else{
                nominalDonasiDonatur = spinnerNominal
            }
//            set validator untuk proses donasi
            if(nominalDonasiDonatur < 1000) {
                helperNominal.setTextColor(Color.parseColor("#ed2a18"))
                return@setOnClickListener
            }else{
                helperNominal.setTextColor(Color.parseColor("#86b4ba"))
            }

            if(idPembayaran == null){
                btnPilihBayarC.setTextColor(Color.parseColor("#ed2a18"))
                return@setOnClickListener
            }else{
//                sharedPreference.save("donasiProgramId",idProgram)
//                sharedPreference.save("donasiPembayaranId", idPembayaran)
//                sharedPreference.save("donasiNominal", nominalDonasiDonatur)
//                sharedPreference.save("donasiDoa", doaDonatur)
//                postDonasiDonatur(retrivedToken)
                startActivity(Intent(this@ProgramDetailActivity, InvoiceActivity::class.java))
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

    private fun postDonasiDonatur(tokenValue: String?){
        val body = JSONObject()
        val sharedPreference: Preferences = Preferences(this)
        body.put("program_id", sharedPreference.getValueString("donasiProgramId"))
        body.put("metode_pembayaran_id", sharedPreference.getValueString("donasiPembayaranId"))
        body.put("nominal", sharedPreference.getValueInt("donasiNominal"))
        body.put("pesan_doa", sharedPreference.getValueString("donasiDoa"))
        body.put("hamba_Allah", sharedPreference.getValueString("ANONIM"))
        body.put("is_android", true)
        body.put("no_wa", sharedPreference.getValueString(""))
        body.put("email", sharedPreference.getValueString(""))
        body.put("nama_donatur", sharedPreference.getValueString(""))
        body.put("kode_negara_no_hp", sharedPreference.getValueString(""))
        body.put("panggilan", sharedPreference.getValueString(""))
        body.put("influencer_id", null)
        body.put("no_link_aja", sharedPreference.getValueString(""))
        body.put("no_ovo", sharedPreference.getValueString(""))
        body.put("no_dana", sharedPreference.getValueString(""))
        body.put("nominal_flash_sale_id", null)
        ApiService.postDonasi(tokenValue, body).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                if(response?.getString("message").equals("Donasi berhasil")){
                    val toast = Toast.makeText(
                        this@ProgramDetailActivity,
                        "Oke Donasi Done",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                }
            }

            override fun onError(anError: ANError?) {
                Log.d(TAG, "OnErrorProsesDonasiBody " + anError?.errorBody)
                Log.d(TAG, "OnErrorProsesDonasiCode " + anError?.errorCode)
                Log.d(TAG, "OnErrorProsesDonasiDetail " + anError?.errorDetail)
            }

        })
    }

    private fun getKoneksi(
        tokenValue: String?,
        idProgram: String?,
        donatur: RecyclerView,
        jumlahDonatur: TextView,
        spinner: Spinner
    ){
        val header: String? = tokenValue
        ApiService.getKoneksi(header).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                getDonaturProgramA(tokenValue, idProgram, donatur, jumlahDonatur)
                getNominal(tokenValue, idProgram, spinner)
            }

            override fun onError(anError: ANError?) {
                refreshToken(tokenValue, idProgram, donatur, jumlahDonatur)
            }

        })
    }

    private fun refreshToken(
        tokenValue: String?,
        idProgram: String?,
        donatur: RecyclerView,
        jumlahDonatur: TextView
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
                                getDonaturProgramA(tokenValue, idProgram, donatur, jumlahDonatur)
                            }
                        } else if (response?.getString("message")
                                .equals("Token expired berhasil di refresh")
                        ) {
                            val token: String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getDonaturProgramA(tokenValue, idProgram, donatur, jumlahDonatur)
                            }
                        } else {
                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                    val intent = Intent(
                                        this@ProgramDetailActivity,
                                        IntroActivity::class.java
                                    )
                                    startActivity(intent)
                                }, 2500)
                            }
                        }

                    } catch (e: JSONException) {
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
                            val intent = Intent(
                                this@ProgramDetailActivity,
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

    private fun getDonaturProgramA(
        tokenValue: String?,
        idProgram: String?,
        donatur: RecyclerView,
        jumlahDonatur: TextView
    ){
        ApiService.getDonatur(tokenValue, idProgram).getAsJSONObject(object :
            JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                val jsonArray = response?.getJSONArray("data")
                val totalDonatur: String? = response?.getString("total_donatur")
                jumlahDonatur.text = "($totalDonatur)"
                if (jsonArray?.length()!! > 0) {
                    for (i in 0 until 5) {
                        val item = jsonArray.getJSONObject(i)
                        val img: String? = "https://aksiberbagi.com/assets/images/user_akber.png"
                        val namaDonatur: String? = item?.getString("tbldonatur_nama")
                        val donasiDonatur: Double? =
                            item?.getString("tbldonasi_nominal")!!.toDouble()
                        val doaDonatur: String? = item?.getString("tbldonasi_doa")
                        val waktuDonasi: String? = item?.getString("tbldonasi_tglinsert")
                        arrayDonatur.add(
                            ListDonasi(
                                img,
                                namaDonatur,
                                donasiDonatur,
                                doaDonatur,
                                waktuDonasi
                            )
                        )
                        val myAdapterListDonasi = ListDonasiAdapter(
                            arrayDonatur,
                            applicationContext
                        )
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

    private fun getNominal(tokenValue: String?, idProgram: String?, spinner: Spinner){
            ApiService.getPilihanNominal(tokenValue, idProgram).getAsJSONObject(object :
                JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val jsonArray = response?.getJSONArray("data")
                    if (jsonArray?.length()!! > 0) {
                        for (i in 0 until jsonArray.length()) {
                            val item = jsonArray.getJSONObject(i)
                            val nominalId = item?.getInt("tblnominal_id")
                            val nominalRaw = item?.getString("tblnominal_nominal")!!.toDouble()
                            val nominal = Converter.rupiah(nominalRaw)
                            val keterangan = item?.getString("tblnominal_keterangan")
                            val pilihanAdapter = "$nominal $keterangan"
                            nominalItems.add(pilihanAdapter)
                            idNominal.add(nominalRaw!!.toInt())
                        }
                        var spinnerString: String? = intent.getStringExtra("spinner")
                        nominalItems.add("Masukkan Nominal Lain")
                        idNominal.add(0)
                        val adapterNominal = ArrayAdapter(
                            this@ProgramDetailActivity,
                            R.layout.list_pilih_program_dropdown,
                            nominalItems
                        )
                        spinner.adapter = adapterNominal

                        nominalItems.forEach {
                            if (spinnerString == it) {
                                spinner.setSelection(adapterNominal.getPosition(it))
                            }
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                    apiError?.message == "Nomor telepon telah terdaftar"
                    Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                    Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                    Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
                }

            })
    }

    private fun hideSoftKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun btnShowLayout(btn: Button, layout: LinearLayout){
            val params: ViewGroup.LayoutParams = layout.layoutParams
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            layout.layoutParams = params
            btn.text = "Sembunyikan"
            btnKeteranganStatus = "On"
    }

    private fun btnHideLayout(btn: Button, layout: LinearLayout){
            val params: ViewGroup.LayoutParams = layout.layoutParams
            params.height = 1000
            layout.layoutParams = params
            btn.text = "Lihat Semua"
            btnKeteranganStatus = ""
    }
}


