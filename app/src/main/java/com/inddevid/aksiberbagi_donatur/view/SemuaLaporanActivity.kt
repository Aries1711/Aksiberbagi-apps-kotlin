package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.inddevid.aksiberbagi_donatur.R

class SemuaLaporanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.semua_laporan_activity)

        val toolbar: Toolbar = findViewById(R.id.upAppbarSemuaLaporan)
        toolbar.title = "Laporan Aksiberbagi"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE)
        toolbar.setNavigationOnClickListener{ startActivity(Intent(this@SemuaLaporanActivity, DashboardActivity::class.java)) }

        val webViewLaporan: WebView = findViewById(R.id.laporanWebView)
        webViewLaporan.loadUrl("https://aksiberbagi.com/apk/berita")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}