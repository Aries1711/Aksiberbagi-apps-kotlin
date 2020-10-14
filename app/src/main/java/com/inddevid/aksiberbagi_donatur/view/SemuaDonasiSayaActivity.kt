package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.CardDonasiSaya
import com.inddevid.aksiberbagi_donatur.presenter.RecyclerDonasiSayaAdapter

class SemuaDonasiSayaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.all_donasi_saya_activity)

        val toolbar: Toolbar = findViewById(R.id.upAppbarSemuaDonasi)
        toolbar.title = "Catatan Kebaikan"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE)
        toolbar.setNavigationOnClickListener{
            val mIntent = Intent(this, DashboardActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("donasiSayaAktif", "true")
            mIntent.putExtras(mBundle)
            startActivity(mIntent)}
        //declare valu for replacement donasi saya
        val imageUrl:String = "https://aksiberbagi.com/storage/program/Raih%20Keutamaan%20Bulan%20Muharram;%20Perbanyak%20Amal%20Shalih-banner.jpg"
        val titleCardDonasi:String = "Sedekah Air untuk Pesantren Pelosok dan ..."
        val titleCardDonasiA:String = "Oke"
        val paymentDonasi:String = "Gopay"
        val timePayment:String = "1 jam lalu"
        val donasiSum:String = "Rp 100.789"
        val donasiSayaStat:String = "Berhasil"
        //declare the arraylist for card donasi saya
        val arrayList = ArrayList<CardDonasiSaya>()
        arrayList.add(CardDonasiSaya(titleCardDonasi,paymentDonasi,donasiSum,timePayment,donasiSayaStat,imageUrl))
        arrayList.add(CardDonasiSaya(titleCardDonasiA,paymentDonasi,donasiSum,timePayment,donasiSayaStat,imageUrl))
        arrayList.add(CardDonasiSaya(titleCardDonasi,paymentDonasi,donasiSum,timePayment,donasiSayaStat,imageUrl))
        val myAdapterB = RecyclerDonasiSayaAdapter(arrayList, this)

        var mainMenuB = findViewById<RecyclerView>(R.id.recyclerDonasiSayaAll)
        mainMenuB.layoutManager = LinearLayoutManager(this)
        mainMenuB.adapter = myAdapterB
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}