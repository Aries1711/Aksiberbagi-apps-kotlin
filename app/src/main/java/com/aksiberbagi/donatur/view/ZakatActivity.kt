package com.aksiberbagi.donatur.view

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
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.services.ApiError
import com.aksiberbagi.donatur.services.ApiService
import com.aksiberbagi.donatur.services.NumberFormaterDot
import com.aksiberbagi.donatur.services.Preferences
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONException
import org.json.JSONObject

class ZakatActivity : AppCompatActivity() {
    private var idProgramZakat : Int = 0
    private val arrayIdPembayaran: ArrayList<String> = arrayListOf("0")
    private val arrayNamaPembayaran: ArrayList<String> = arrayListOf("Pilih Pembayaran")
    private val TAG = "Zakat Activity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zakat_activity)

        val toolbar: Toolbar = findViewById(R.id.upAppbarZakat)
        toolbar.title = "Zakat"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationOnClickListener{ startActivity(Intent(this@ZakatActivity, DashboardActivity::class.java))}

        //glide option
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(500, 190)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

        //deklarasi token dan preference
        val sharedPreference: Preferences = Preferences(this)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")
        val switchAnonimValue: String? = sharedPreference.getValueString("ANONIM")
        var statusDialogPembayaranIntent: String? = intent.getStringExtra("dialogPembayaran")
        var nominalIntent: String? = intent.getStringExtra("nominalZakatKalkulator")
        var idZakatIntent: String? = intent.getStringExtra("idZakatKalkulator")
        var jenisIntent: String? = intent.getStringExtra("jenisZakatKalkulator")

        //tombol bayar zakat header dan kalkulator
        val btnBayarZakatHeader: LinearLayout = findViewById(R.id.btnZakatBayar)
        val btnZakatKalkulator: LinearLayout = findViewById(R.id.btnZakatHitung)

        val dialogPembayaran = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view : View = layoutInflater.inflate(R.layout.dialog_donasi_zakat, null)
        dialogPembayaran.setContentView(view)


        val spinnerJenisZakat : Spinner = view.findViewById(R.id.spinnerJenisZakat)
        val textTitleDialog = view.findViewById<TextView>(R.id.titleDialogDonasiZakat)

        btnBayarZakatHeader.setOnClickListener {
            dialogPembayaran.show()
            spinnerJenisZakat.visibility = View.VISIBLE
        }

        val arrayJenisZakat: ArrayList<String> = arrayListOf("Pilih jenis zakat anda", "Zakat Tabungan", "Zakat Emas dan Perak", "Zakat Perdagangan","Zakat Penghasilan")
        val adapterZakat = ArrayAdapter(
            this@ZakatActivity,
            R.layout.list_pilih_program_dropdown,
            arrayJenisZakat
        )
        spinnerJenisZakat.adapter = adapterZakat
        spinnerJenisZakat.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(arrayJenisZakat[position] == "Pilih jenis zakat anda"){
                    idProgramZakat = 0
                }else if(arrayJenisZakat[position] == "Zakat Tabungan"){
                    idProgramZakat = 150
                    textTitleDialog.text = "Zakat Tabungan"
                    textTitleDialog.setTextColor(Color.parseColor("#546787"))
                    spinnerJenisZakat.visibility = View.GONE
                }else if(arrayJenisZakat[position] == "Zakat Emas dan Perak"){
                    idProgramZakat = 152
                    textTitleDialog.text = "Zakat Emas dan Perak"
                    textTitleDialog.setTextColor(Color.parseColor("#546787"))
                    spinnerJenisZakat.visibility = View.GONE
                }else if(arrayJenisZakat[position] == "Zakat Perdagangan"){
                    idProgramZakat = 151
                    textTitleDialog.text = "Zakat Perdagangan"
                    textTitleDialog.setTextColor(Color.parseColor("#546787"))
                    spinnerJenisZakat.visibility = View.GONE
                }else if(arrayJenisZakat[position] == "Zakat Penghasilan"){
                    idProgramZakat = 153
                    textTitleDialog.text = "Zakat Penghasilan"
                    textTitleDialog.setTextColor(Color.parseColor("#546787"))
                    spinnerJenisZakat.visibility = View.GONE
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        val helperNominalZakat = view.findViewById<TextView>(R.id.helperNominal)
        val inputNominalZakat = view.findViewById<TextInputEditText>(R.id.nominalZakat)
        val initialisasiNominal = "0"
        inputNominalZakat.text = initialisasiNominal.toEditable()
//        inputNominalZakat.addTextChangedListener(NumberFormaterDot(inputNominalZakat))

        if(statusDialogPembayaranIntent == "aktif"){
            dialogPembayaran.show()
            if (nominalIntent != "" || nominalIntent != "null" || nominalIntent != null ){
                inputNominalZakat.text = nominalIntent?.replace(".", "")?.replace(",00", "")?.toEditable()
            }

            if (idZakatIntent != "" || idZakatIntent != "null" || idZakatIntent != null ){
                idProgramZakat = idZakatIntent!!.toInt()
            }

            if (jenisIntent != "" || jenisIntent != "null" || jenisIntent != null){
                textTitleDialog.text = "Zakat $jenisIntent"
            }
        }

        val spinnerPembayaran: Spinner = view.findViewById(R.id.spinnerPilihPembayaran)
        val textNoPembayaranLayout: TextInputLayout = view.findViewById(R.id.noPembayaranLayout)
        val textNoPembayaranInput: TextInputEditText = view.findViewById(R.id.noPembayaran)
        textNoPembayaranLayout.visibility = View.GONE
        var idPembayaran: String = "0"
        spinnerPembayaran.onItemSelectedListener = object :  AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (arrayIdPembayaran[position] == "0"){
                    idPembayaran = "0"
                }else if(arrayIdPembayaran[position] == "1001"){
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


        btnZakatKalkulator.setOnClickListener {
            startActivity(Intent(this@ZakatActivity, KalkulatorActivity::class.java))
        }


        //list pilihan zakat berdasarkan jenis
        val imageZakatTabungan: ImageView = findViewById(R.id.imageZakatTabungan)
        Glide.with(this).load("https://aksiberbagi.com/storage/program/2a2692b30977847c99b23f478aab9e02_zsimpan.jpg").apply(options).into(imageZakatTabungan)
        val btnZakatTabungan: Button = findViewById(R.id.btnZakatTabungan)
        btnZakatTabungan.setOnClickListener {
            dialogPembayaran.show()
            spinnerJenisZakat.visibility = View.GONE
            idProgramZakat = 150
            textTitleDialog.text = "Zakat Tabungan"
        }


        val imageZakatEmas: ImageView = findViewById(R.id.imageZakatEmas)
        Glide.with(this).load("https://aksiberbagi.com/storage/program/cbabdc05e9c3b9e802e1c028c42470b9_zemas.jpg").apply(options).into(imageZakatEmas)
        val btnZakatEmas: Button = findViewById(R.id.btnZakatEmas)
        btnZakatEmas.setOnClickListener {
            dialogPembayaran.show()
            spinnerJenisZakat.visibility = View.GONE
            idProgramZakat = 152
            textTitleDialog.text = "Zakat Emas dan Perak"
        }

        val imageZakatDagang: ImageView = findViewById(R.id.imageZakatPerdagangan)
        Glide.with(this).load("https://aksiberbagi.com/storage/program/0ee1c6f32d6d11f44cbb9ffb42c3ed84_zdagang.jpg").apply(options).into(imageZakatDagang)
        val btnZakatPerdagangan : Button = findViewById(R.id.btnZakatPerdagangan)
        btnZakatPerdagangan.setOnClickListener {
            dialogPembayaran.show()
            spinnerJenisZakat.visibility = View.GONE
            idProgramZakat = 151
            textTitleDialog.text = "Zakat Perdagangan "
        }

        val imageZakatPenghasilan: ImageView = findViewById(R.id.imageZakatPenghasilan)
        Glide.with(this).load("https://aksiberbagi.com/storage/program/04fb0a8e0d93c42586fc31f186c07bde_zhasilan.jpg").apply(options).into(imageZakatPenghasilan)
        val btnZakatPenghasilan : Button = findViewById(R.id.btnZakatPenghasilan)
        btnZakatPenghasilan.setOnClickListener {
            dialogPembayaran.show()
            spinnerJenisZakat.visibility = View.GONE
            idProgramZakat = 153
            textTitleDialog.text = "Zakat Penghasilan"
        }


        getKoneksi( retrivedToken!!, this, view)


        //button lanjut pembayaran untuk ke invoice
        val btnPembayaran : Button = view.findViewById(R.id.donasiLanjutPembayaran)
        btnPembayaran.setOnClickListener {
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

            val nominalZakat = inputNominalZakat.text.toString().replace(".", "").replace(",00", "")


            if(idProgramZakat == 0){
                textTitleDialog.text = "Mohon pilih jenis zakat anda"
                textTitleDialog.setTextColor(Color.parseColor("#ed2a18"))
                return@setOnClickListener
            }

            if(nominalZakat.toInt() < 9999 ){
                helperNominalZakat.setTextColor(Color.parseColor("#ed2a18"))
                return@setOnClickListener
            }

            sharedPreference.save("donasiProgramId", idProgramZakat.toString())
            sharedPreference.save("donasiPembayaranId", idPembayaran)
            sharedPreference.save("donasiNominal", nominalZakat.toInt())
            sharedPreference.save("donasiDoa", doaDonasi)
            postDonasiZakat(retrivedToken, this@ZakatActivity)
        }

    }

    private fun hideSoftKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun postDonasiZakat(tokenValue: String, context: ZakatActivity){
        val body = JSONObject()
        val inputNoLayout = context.findViewById<TextInputLayout>(R.id.noPembayaranLayout)
        val sharedPreference: Preferences = Preferences(this)
        body.put("program_id", sharedPreference.getValueString("donasiProgramId"))
        body.put("metode_pembayaran_id", sharedPreference.getValueString("donasiPembayaranId"))
        body.put("nominal", sharedPreference.getValueInt("donasiNominal"))
        body.put("pesan_doa", sharedPreference.getValueString("donasiDoa"))
        body.put("hamba_Allah", sharedPreference.getValueString("ANONIM"))
        body.put("is_android", 1)
        body.put("no_wa", sharedPreference.getValueString("penggunaWA"))
        body.put("email", sharedPreference.getValueString("penggunaEmail"))
        body.put("nama_donatur", sharedPreference.getValueString("penggunaNAMA"))
        body.put("kode_negara_no_hp", sharedPreference.getValueString("penggunaKodeNegara"))
        body.put("panggilan", sharedPreference.getValueString("penggunaPanggilan"))
        body.put("influencer_id", null)
        body.put("no_link_aja", sharedPreference.getValueString("penggunaLinkAja"))
        body.put("no_ovo", sharedPreference.getValueString("penggunaOvo"))
        body.put("no_dana", sharedPreference.getValueString("penggunaDana"))
        body.put("nominal_flash_sale_id", null)
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
                                this@ZakatActivity,
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
                                this@ZakatActivity,
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
                        this@ZakatActivity,
                        "Silakan masukkan no ovo yang valid dan sudah terdaftar",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    inputNoLayout.requestFocus()
                    inputNoLayout.boxStrokeColor = ContextCompat.getColor(
                        this@ZakatActivity,
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

    private fun getKoneksi(tokenValue: String, context: ZakatActivity, view: View){
        ApiService.getKoneksi(tokenValue).getAsJSONObject(object : JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                getPilihanPembayaran(tokenValue, context, view)
            }

            override fun onError(anError: ANError?) {
                val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                if(anError?.errorDetail!!.equals("connectionError")){
                    val toast = Toast.makeText(
                        this@ZakatActivity,
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
        context: ZakatActivity,
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
                                        this@ZakatActivity,
                                        IntroActivity::class.java
                                    )
                                    startActivity(intent)
                                }, 2500)
                            }
                        }

                    } catch (e: JSONException) {
                        val toast = Toast.makeText(
                            this@ZakatActivity,
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
                                this@ZakatActivity,
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

    private fun getPilihanPembayaran(tokenValue: String, context: ZakatActivity, view: View){
        ApiService.getPembayaran(tokenValue).getAsJSONObject(object : JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                val jsonObject = response?.getJSONObject("data")
                val jsonArrayEwallet = jsonObject?.getJSONArray("ewallets")
                val jsonArrayBank = jsonObject?.getJSONArray("banks")
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
                    this@ZakatActivity,
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

    override fun onBackPressed() {
        startActivity(Intent(this@ZakatActivity , DashboardActivity::class.java))
    }

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}