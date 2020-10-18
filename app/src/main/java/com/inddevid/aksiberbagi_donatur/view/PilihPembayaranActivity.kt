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
        var listE = mutableListOf<ModelPembayaran>()
        listE.add(ModelPembayaran("Gopay","https://aksiberbagi.com/storage/bank/gopay-new.png"))
        listE.add(ModelPembayaran("Dana","https://aksiberbagi.com/storage/bank/vi2Vr1CBPDczTvFlMIybb6bqQIjEKUmQvHAkhbqW.png"))
        listE.add(ModelPembayaran("OVO","https://aksiberbagi.com/storage/bank/logo-ovo.png"))
        listE.add(ModelPembayaran("Link Aja","https://aksiberbagi.com/storage/bank/8uOgHqnWJByhe5p3QMFzCTYxmTLBJpuKGtku3djn.png"))
        listE.add(ModelPembayaran("ShopeePay","https://aksiberbagi.com/storage/bank/8QZzJV93mHSsDCEEMicKg985ba5MMbBjbsXvACDG.png"))
        listEwallet.adapter = PilihPembayaranAdapter(this, R.layout.pilih_pembayaran_row, listE)

        val listTransfer: ListView = findViewById(R.id.listViewTransfer)
        var listT = mutableListOf<ModelPembayaran>()
        listT.add(ModelPembayaran("BNI/BNI Syariah", "https://aksiberbagi.com/storage/bank/bni-syariah.png"))
        listT.add(ModelPembayaran("Mandiri Syariah", "https://aksiberbagi.com/storage/bank/mandiri.png"))
        listT.add(ModelPembayaran("BCA", "https://aksiberbagi.com/storage/bank/ajZ7LEACpYkW1mVJ6VsuWFdGudbcgusWKCnxiVi2.png"))
        listT.add(ModelPembayaran("BRI", "https://aksiberbagi.com/storage/bank/YqbwMgj8qh6DiowN8ZYgaKHWGMg29XUJUdzV1iwj.png"))
        listT.add(ModelPembayaran("Mandiri", "https://aksiberbagi.com/storage/bank/logo-mandiri.png"))
        listT.add(ModelPembayaran("Muamalat", "https://aksiberbagi.com/storage/bank/Qdtf4FswhYjSyRyMcod8U59FQ6w5peVKyfHrtNHk.png"))
        listT.add(ModelPembayaran("CIMB Niaga / Syariah", "https://aksiberbagi.com/storage/bank/eSxD2RemwuxkNdjIIAfZWpEeaioePxX4LfybUujK.png"))
        listTransfer.adapter = PilihPembayaranAdapter(this, R.layout.pilih_pembayaran_row, listT)



//        var textNominal : TextView = findViewById(R.id.nominalDonasi)
//        textNominal.text = valueNominal
    }
}