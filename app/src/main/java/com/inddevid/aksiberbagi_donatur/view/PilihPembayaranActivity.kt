package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.ModelPembayaran
import com.inddevid.aksiberbagi_donatur.presenter.PilihPembayaranAdapter

class PilihPembayaranActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pilih_pembayaran_activity)

        val toolbar: Toolbar = findViewById(R.id.upAppbarMetodePembayaran)
        toolbar.title = "Metode Pembayaran"
        toolbar.setTitleTextColor(Color.WHITE)
        var valueNominal: String? = intent.getStringExtra("nominal")
        toolbar.setNavigationOnClickListener{
            val mIntent = Intent(this, ProgramDetailActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("dialogAktif", "true")
            mBundle.putString("nominalDonasi", valueNominal)
            mIntent.putExtras(mBundle)
            startActivity(mIntent)
        }
        val listEwallet: ListView = findViewById(R.id.listViewEwallet)
        var list = mutableListOf<ModelPembayaran>()
        list.add(ModelPembayaran("Gopay","https://aksiberbagi.com/storage/bank/gopay-new.png"))
        list.add(ModelPembayaran("Dana","https://aksiberbagi.com/storage/bank/vi2Vr1CBPDczTvFlMIybb6bqQIjEKUmQvHAkhbqW.png"))
        list.add(ModelPembayaran("OVO","https://aksiberbagi.com/storage/bank/logo-ovo.png"))
        list.add(ModelPembayaran("Link Aja","https://aksiberbagi.com/storage/bank/8uOgHqnWJByhe5p3QMFzCTYxmTLBJpuKGtku3djn.png"))
        list.add(ModelPembayaran("ShopeePay","https://aksiberbagi.com/storage/bank/8QZzJV93mHSsDCEEMicKg985ba5MMbBjbsXvACDG.png"))
        listEwallet.adapter = PilihPembayaranAdapter(this, R.layout.pilih_pembayaran_row, list)

        val listTransfer: ListView = findViewById(R.id.listViewTransfer)



//        var textNominal : TextView = findViewById(R.id.nominalDonasi)
//        textNominal.text = valueNominal
    }
}