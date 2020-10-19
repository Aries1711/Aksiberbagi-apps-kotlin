package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        var nominalSet: String = ""
        if (valueNominal == ""){
            nominalSet = "0"
        }else{
            nominalSet = valueNominal.toString()
        }
        toolbar.setNavigationOnClickListener{
            val mIntent = Intent(this, ProgramDetailActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("dialogAktif", "true")
            mBundle.putString("nominalDonasi", nominalSet)
            mIntent.putExtras(mBundle)
            startActivity(mIntent)
        }


        val arrayEwallet = ArrayList<ModelPembayaran>()
        arrayEwallet.add(ModelPembayaran("1","Gopay","https://aksiberbagi.com/storage/bank/gopay-new.png"))
        arrayEwallet.add(ModelPembayaran("2","Dana","https://aksiberbagi.com/storage/bank/vi2Vr1CBPDczTvFlMIybb6bqQIjEKUmQvHAkhbqW.png"))
        arrayEwallet.add(ModelPembayaran("3","OVO","https://aksiberbagi.com/storage/bank/logo-ovo.png"))
        arrayEwallet.add(ModelPembayaran("4","Link Aja","https://aksiberbagi.com/storage/bank/8uOgHqnWJByhe5p3QMFzCTYxmTLBJpuKGtku3djn.png"))
        arrayEwallet.add(ModelPembayaran("5","ShopeePay","https://aksiberbagi.com/storage/bank/8QZzJV93mHSsDCEEMicKg985ba5MMbBjbsXvACDG.png"))
        val myAdapterEwallet = PilihPembayaranAdapter(arrayEwallet,this, nominalSet)
        var mainMenuEwallet = findViewById<RecyclerView>(R.id.recyclerEwallet)
        mainMenuEwallet.layoutManager = LinearLayoutManager(this)
        mainMenuEwallet.adapter = myAdapterEwallet


        val arrayTranfer = ArrayList<ModelPembayaran>()

        arrayTranfer.add(ModelPembayaran("6","BNI/BNI Syariah", "https://aksiberbagi.com/storage/bank/bni-syariah.png"))
        arrayTranfer.add(ModelPembayaran("7","Mandiri Syariah", "https://aksiberbagi.com/storage/bank/mandiri.png"))
        arrayTranfer.add(ModelPembayaran("8","BCA", "https://aksiberbagi.com/storage/bank/ajZ7LEACpYkW1mVJ6VsuWFdGudbcgusWKCnxiVi2.png"))
        arrayTranfer.add(ModelPembayaran("9","BRI", "https://aksiberbagi.com/storage/bank/YqbwMgj8qh6DiowN8ZYgaKHWGMg29XUJUdzV1iwj.png"))
        arrayTranfer.add(ModelPembayaran("10","Mandiri", "https://aksiberbagi.com/storage/bank/logo-mandiri.png"))
        arrayTranfer.add(ModelPembayaran("11","Muamalat", "https://aksiberbagi.com/storage/bank/Qdtf4FswhYjSyRyMcod8U59FQ6w5peVKyfHrtNHk.png"))
        arrayTranfer.add(ModelPembayaran("12","CIMB Niaga / Syariah", "https://aksiberbagi.com/storage/bank/eSxD2RemwuxkNdjIIAfZWpEeaioePxX4LfybUujK.png"))
        val myAdapterTransfer = PilihPembayaranAdapter(arrayTranfer, this, nominalSet)
        var mainMenuTranfer = findViewById<RecyclerView>(R.id.recyclerTransfer)
        mainMenuTranfer.layoutManager = LinearLayoutManager(this)
        mainMenuTranfer.adapter = myAdapterTransfer

    }
}