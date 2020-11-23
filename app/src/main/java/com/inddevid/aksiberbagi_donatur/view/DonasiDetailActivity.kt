package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.inddevid.aksiberbagi_donatur.R

class DonasiDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donasi_detail_activity)

        val toolbar: Toolbar = findViewById(R.id.upAppbarDetailDonasi)
        toolbar.title = "Donasi"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE)
        toolbar.setNavigationOnClickListener{
            val mIntent = Intent(this, DashboardActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("donasiSayaAktif", "true")
            mIntent.putExtras(mBundle)
            startActivity(mIntent)}
    }
}