package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.services.ApiError
import com.inddevid.aksiberbagi_donatur.services.ApiService
import com.inddevid.aksiberbagi_donatur.services.NumberFormaterDot
import com.inddevid.aksiberbagi_donatur.services.Preferences
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

        //deklarasi token
        val sharedPreference: Preferences = Preferences(this)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")

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
                    spinnerJenisZakat.visibility = View.GONE
                }else if(arrayJenisZakat[position] == "Zakat Emas dan Perak"){
                    idProgramZakat = 152
                    textTitleDialog.text = "Zakat Emas dan Perak"
                    spinnerJenisZakat.visibility = View.GONE
                }else if(arrayJenisZakat[position] == "Zakat Perdagangan"){
                    idProgramZakat = 151
                    textTitleDialog.text = "Zakat Perdagangan"
                    spinnerJenisZakat.visibility = View.GONE
                }else if(arrayJenisZakat[position] == "Zakat Penghasilan"){
                    idProgramZakat = 153
                    textTitleDialog.text = "Zakat Penghasilan"
                    spinnerJenisZakat.visibility = View.GONE
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
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

        val inputNominalZakat = view.findViewById<TextInputEditText>(R.id.nominalZakat)
        val initialisasiNominal = "0"
        inputNominalZakat.text = initialisasiNominal.toEditable()
        inputNominalZakat.addTextChangedListener(NumberFormaterDot(inputNominalZakat))


        getKoneksi( retrivedToken!!, this, view)

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

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}