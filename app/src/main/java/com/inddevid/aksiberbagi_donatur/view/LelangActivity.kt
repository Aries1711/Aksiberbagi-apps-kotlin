package com.inddevid.aksiberbagi_donatur.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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
    private val arrayNominalRaw: ArrayList<String> = arrayListOf("0")
    private var idNominal: String = "0"
    private var nominalNominal: String = "0"
    private var idPembayaran: String = "0"

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
        val switchAnonimValue: String? = sharedPreference.getValueString("ANONIM")
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
        nominalText.text = nominalLelang


        val countDown: DynamicCountDownView = findViewById(R.id.normalCountDownLelang)
        countDown.timerTextBackgroundTintColor = ContextCompat.getColor(
            this,
            R.color.colorOrange
        )

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
        val spinnerNominal : Spinner = view.findViewById(R.id.spinnerJenisNominal)
        val helperNominal : TextView = view.findViewById(R.id.helperNominal)
        val spinnerPembayaran : Spinner = view.findViewById(R.id.spinnerPilihPembayaran)
        val helperPembayaran : TextView = view.findViewById(R.id.helperPembayaran)
        val textNoPembayaranLayout: TextInputLayout = view.findViewById(R.id.noPembayaranLayout)
        val textNoPembayaranInput: TextInputEditText = view.findViewById(R.id.noPembayaran)
        textNoPembayaranLayout.visibility = View.GONE

        spinnerNominal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                helperNominal.visibility = View.GONE
                idNominal = arrayIdNominal[position]
                nominalNominal = arrayNominalRaw[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        spinnerPembayaran.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                helperPembayaran.visibility = View.GONE
                if(arrayIdPembayaran[position] == "1001"){
                    idPembayaran = arrayIdPembayaran[position]
                    textNoPembayaranLayout.visibility = View.VISIBLE
                    textNoPembayaranLayout.hint = "Masukkan No akun Link Aja"
                }else if(arrayIdPembayaran[position] == "1002"){
                    idPembayaran = arrayIdPembayaran[position]
                    textNoPembayaranLayout.visibility = View.VISIBLE
                    textNoPembayaranLayout.hint = "Masukkan No akun Ovo"
                }else if(arrayIdPembayaran[position] == "1003"){
                    idPembayaran = arrayIdPembayaran[position]
                    textNoPembayaranLayout.visibility = View.VISIBLE
                    textNoPembayaranLayout.hint = "Masukkan No akun Dana"
                }else{
                    idPembayaran = arrayIdPembayaran[position]
                    textNoPembayaranLayout.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }



        val switchAnonimSet: Switch = view.findViewById(R.id.anonimDonasiBtn)
        val switchDoaSet: Switch = view.findViewById(R.id.doaDonasiBtn)

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

        val inputLayoutDoa: TextInputLayout = view.findViewById(R.id.doaDonasi)
        val inputDoa: TextInputEditText = view.findViewById(R.id.doaDonatur)

        var doaStatus = ""
        inputLayoutDoa.visibility = View.GONE
        switchDoaSet.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
            if (message == "Switch1:ON"){
                doaStatus = "True"
                inputLayoutDoa.visibility = View.VISIBLE
            }else{
                doaStatus = "False"
                inputLayoutDoa.visibility = View.GONE
                hideSoftKeyboard(this, inputLayoutDoa)
            }
        }


        val btnPembayaran : Button = findViewById(R.id.donasiBtn)
        btnPembayaran.setOnClickListener {
            dialogPembayaran.show()
        }

        getKoneksi(retrivedToken!!, this, view, idLelang!!)


        //button lanjut pembayaran untuk ke invoice
        val btnLanjutPembayaran : Button = view.findViewById(R.id.donasiLanjutPembayaran)
        btnLanjutPembayaran.setOnClickListener {

            if (idNominal == "0"){
                helperNominal.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (idPembayaran == "0"){
                helperPembayaran.visibility = View.VISIBLE
                return@setOnClickListener
            }

            var doaDonasi : String = ""
            if (doaStatus == "True"){
                doaDonasi = inputDoa.text.toString()
            }

            if (idPembayaran == "1001"){
                val noLinkAja = textNoPembayaranInput.text?.toString()
                sharedPreference.save("penggunaLinkAja", noLinkAja)
            }else if(idPembayaran == "1002"){
                val noOvo = textNoPembayaranInput.text?.toString()
                sharedPreference.save("penggunaOvo", noOvo)
            }else if(idPembayaran == "1003"){
                val noDana = textNoPembayaranInput.text?.toString()
                sharedPreference.save("penggunaDana", noDana)
            }

            sharedPreference.save("donasiProgramId", idLelangProgram)
            sharedPreference.save("donasiPembayaranId", idPembayaran)
            sharedPreference.save("donasiNominal", nominalNominal.toInt())
            sharedPreference.save("donasiDoa", doaDonasi)
            sharedPreference.save("nominalLelangId", idNominal.toInt())
            postDonasiLelang(retrivedToken, this)
        }

    }

    private fun hideSoftKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun postDonasiLelang(tokenValue: String, context: LelangActivity){
        val body = JSONObject()
        val inputNoLayout = context.findViewById<TextInputLayout>(R.id.noPembayaranLayout)
        val sharedPreference: Preferences = Preferences(this)
        body.put("program_id", sharedPreference.getValueString("donasiProgramId"))
        body.put("metode_pembayaran_id", sharedPreference.getValueString("donasiPembayaranId"))
        body.put("nominal", sharedPreference.getValueInt("donasiNominal"))
        body.put("pesan_doa", sharedPreference.getValueString("donasiDoa"))
        body.put("hamba_Allah", sharedPreference.getValueString("ANONIM"))
        body.put("is_android", true)
        body.put("no_wa", sharedPreference.getValueString("penggunaWA"))
        body.put("email", sharedPreference.getValueString("penggunaEmail"))
        body.put("nama_donatur", sharedPreference.getValueString("penggunaNAMA"))
        body.put("kode_negara_no_hp", sharedPreference.getValueString("penggunaKodeNegara"))
        body.put("panggilan", sharedPreference.getValueString("penggunaPanggilan"))
        body.put("influencer_id", null)
        body.put("no_link_aja", sharedPreference.getValueString("penggunaLinkAja"))
        body.put("no_ovo", sharedPreference.getValueString("penggunaOvo"))
        body.put("no_dana", sharedPreference.getValueString("penggunaDana"))
        body.put("nominal_flash_sale_id",  sharedPreference.getValueInt("nominalLelangId"))
        ApiService.postDonasi(tokenValue, body).getAsJSONObject(object :JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                if (response?.getString("message").equals("Donasi berhasil diproses")) {
                    val data: JSONObject? = response?.getJSONObject("data")
                    val eWallet = data?.getString("isEwallet")
                    if (eWallet == "false") {
                        val dataDonasi = data?.getJSONObject("donasi")
                        sharedPreference.save(
                            "invoiceNominal",
                            dataDonasi?.getString("tbldonasi_nominal")
                        )
                        sharedPreference.save(
                            "invoiceKodeUnik",
                            dataDonasi?.getString("tbldonasi_nourut")
                        )
                        sharedPreference.save(
                            "invoiceKode",
                            dataDonasi?.getString("tbldonasi_invoice")
                        )
                        val dataDonasiBank = dataDonasi?.getJSONObject("bank")
                        sharedPreference.save(
                            "invoiceBank",
                            dataDonasiBank?.getString("tblbank_nama")
                        )
                        sharedPreference.save(
                            "invoiceBankAN",
                            dataDonasiBank?.getString("tblbank_namapemilik")
                        )
                        sharedPreference.save(
                            "invoiceBankUrl",
                            dataDonasiBank?.getString("logo_url")
                        )
                        sharedPreference.save(
                            "invoiceBankRekening",
                            dataDonasiBank?.getString("tblbank_rekening")
                        )
                        val dataDonasiProgram = dataDonasi?.getJSONObject("program")
                        sharedPreference.save(
                            "invoiceProgramJudul",
                            dataDonasiProgram?.getString("tblprogram_judul")
                        )
                        startActivity(
                            Intent(
                                this@LelangActivity,
                                InvoiceActivity::class.java
                            )
                        )
                    } else {
                        val midtransStatus = data?.getString("midtrans")
                        val xenditStatus = data?.getString("xendit")
                        val faspayStatus = data?.getString("faspay")
                        var redirectUrl: String? = ""
                        if (midtransStatus == "null" && faspayStatus == "null") {
                            val xendit = data?.getJSONObject("xendit")
                            redirectUrl = xendit?.getString("redirect_url")
                        } else if(xenditStatus == "null" && faspayStatus == "null"){
                            val midtrans = data?.getJSONObject("midtrans")
                            redirectUrl = midtrans?.getString("redirect_url")
                        } else if(xenditStatus == "null" && midtransStatus == "null"){
                            val faspay = data?.getJSONObject("faspay")
                            redirectUrl = faspay?.getString("deeplink")
                        }
                        val dataDonasi = data?.getJSONObject("donasi")
                        sharedPreference.save("invoiceUrl", redirectUrl)
                        sharedPreference.save(
                            "invoiceNominal",
                            dataDonasi?.getString("tbldonasi_nominal")
                        )
                        sharedPreference.save(
                            "invoiceKodeUnik",
                            dataDonasi?.getString("tbldonasi_nourut")
                        )
                        sharedPreference.save(
                            "invoiceKode",
                            dataDonasi?.getString("tbldonasi_invoice")
                        )
                        val dataDonasiBank = dataDonasi?.getJSONObject("bank")
                        sharedPreference.save(
                            "invoiceBank",
                            dataDonasiBank?.getString("tblbank_nama")
                        )
                        sharedPreference.save(
                            "invoiceBankAN",
                            dataDonasiBank?.getString("tblbank_namapemilik")
                        )
                        sharedPreference.save(
                            "invoiceBankUrl",
                            dataDonasiBank?.getString("logo_url")
                        )
                        sharedPreference.save(
                            "invoiceBankRekening",
                            dataDonasiBank?.getString("tblbank_rekening")
                        )
                        val dataDonasiProgram = dataDonasi?.getJSONObject("program")
                        sharedPreference.save(
                            "invoiceProgramJudul",
                            dataDonasiProgram?.getString("tblprogram_judul")
                        )
                        startActivity(
                            Intent(
                                this@LelangActivity,
                                WebviewInvoiceActivity::class.java
                            )
                        )
                    }
                }

            }

            override fun onError(anError: ANError?) {
                val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                if (apiError?.message == "Silakan masukkan no ovo yang valid dan sudah terdaftar") {
                    val toast = Toast.makeText(
                        this@LelangActivity,
                        "Silakan masukkan no ovo yang valid dan sudah terdaftar",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    inputNoLayout.requestFocus()
                    inputNoLayout.boxStrokeColor = ContextCompat.getColor(
                        this@LelangActivity,
                        R.color.error_color
                    )
                    inputNoLayout.helperText =
                        "Silakan masukkan no ovo yang valid dan sudah terdaftar"
                }
                Log.d(TAG, "OnErrorProsesDonasiBody " + anError?.errorBody)
                Log.d(TAG, "OnErrorProsesDonasiCode " + anError?.errorCode)
                Log.d(TAG, "OnErrorProsesDonasiDetail " + anError?.errorDetail)
            }
        })

    }

    private fun getKoneksi(tokenValue: String, context: LelangActivity, view: View, idLelang: String){
        ApiService.getKoneksi(tokenValue).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                getDetailLelang(tokenValue, context, view, idLelang)
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
                    refreshToken(tokenValue, context, view, idLelang)
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
        view: View,
        idLelang: String
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
                                getKoneksi(token,context,view,idLelang)
                            }
                        } else if (response?.getString("message")
                                .equals("Token expired berhasil di refresh")
                        ) {
                            val token: String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(token,context,view,idLelang)
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

    private fun getDetailLelang(tokenValue: String, context: LelangActivity, view: View, idLelang: String){
        ApiService.getLelangDetail(tokenValue, idLelang).getAsJSONObject(object : JSONObjectRequestListener{

            val textTerdonasikan : TextView = context.findViewById(R.id.terdonasikanJumlahText)
            val countDown: DynamicCountDownView = context.findViewById(R.id.normalCountDownLelang)

            override fun onResponse(response: JSONObject?) {
                val data = response?.getJSONObject("data")
                val timer = response?.getString("timer")
                val stokAwal = data?.getString("stok_awal")
                val stokSekarang = data?.getString("stok")
                val terdonasi = stokAwal!!.toInt() - stokSekarang!!.toInt()
                textTerdonasikan.text = "$terdonasi Paket terdonasikan"
                countDown.initTimer(timer!!.toInt())
                countDown.startTimer()
            }

            override fun onError(anError: ANError?) {

            }
        })
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
                        arrayNominalRaw.add(nominalNominal)
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