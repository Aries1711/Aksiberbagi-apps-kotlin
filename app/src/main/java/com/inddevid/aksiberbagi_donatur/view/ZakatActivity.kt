package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.services.ApiError
import com.inddevid.aksiberbagi_donatur.services.ApiService
import com.inddevid.aksiberbagi_donatur.services.Preferences
import org.json.JSONObject

class ZakatActivity : AppCompatActivity() {
    private var idProgramZakat : Int = 0
    private val idPembayaran: ArrayList<Int> = arrayListOf(0)

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

        val textTitleDialog = view.findViewById<TextView>(R.id.titleDialogDonasiZakat)

        btnBayarZakatHeader.setOnClickListener {
            dialogPembayaran.show()
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
            idProgramZakat = 150
            textTitleDialog.text = "Masukkan Nominal Zakat Tabungan Anda "
        }


        val imageZakatEmas: ImageView = findViewById(R.id.imageZakatEmas)
        Glide.with(this).load("https://aksiberbagi.com/storage/program/cbabdc05e9c3b9e802e1c028c42470b9_zemas.jpg").apply(options).into(imageZakatEmas)
        val btnZakatEmas: Button = findViewById(R.id.btnZakatEmas)
        btnZakatEmas.setOnClickListener {
            dialogPembayaran.show()
            idProgramZakat = 152
            textTitleDialog.text = "Masukkan Nominal Zakat Emas Anda "
        }

        val imageZakatDagang: ImageView = findViewById(R.id.imageZakatPerdagangan)
        Glide.with(this).load("https://aksiberbagi.com/storage/program/0ee1c6f32d6d11f44cbb9ffb42c3ed84_zdagang.jpg").apply(options).into(imageZakatDagang)
        val btnZakatPerdagangan : Button = findViewById(R.id.btnZakatPerdagangan)
        btnZakatPerdagangan.setOnClickListener {
            dialogPembayaran.show()
            idProgramZakat = 151
            textTitleDialog.text = "Masukkan Nominal Zakat Dagang Anda "
        }

        val imageZakatPenghasilan: ImageView = findViewById(R.id.imageZakatPenghasilan)
        Glide.with(this).load("https://aksiberbagi.com/storage/program/04fb0a8e0d93c42586fc31f186c07bde_zhasilan.jpg").apply(options).into(imageZakatPenghasilan)
        val btnZakatPenghasilan : Button = findViewById(R.id.btnZakatPenghasilan)
        btnZakatPenghasilan.setOnClickListener {
            dialogPembayaran.show()
            idProgramZakat = 153
            textTitleDialog.text = "Masukkan Nominal Zakat Penghasilan Anda "
        }

    }

    private fun getKoneksi(tokenValue: String, context: ZakatActivity, view: View){
        ApiService.getKoneksi(tokenValue).getAsJSONObject(object : JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
//                getPilihanPembayaran
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
                }else{
//                    refreshToken(tokenValue,view, context)
                }

            }
        })
    }

    private fun getPembayaran(tokenValue: String, context: ZakatActivity, view: View){
        ApiService.getPembayaran(tokenValue).getAsJSONObject(object : JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                TODO("Not yet implemented")
            }

            override fun onError(anError: ANError?) {
                TODO("Not yet implemented")
            }
        })
    }
}