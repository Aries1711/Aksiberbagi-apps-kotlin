package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.inddevid.aksiberbagi_donatur.R

class SyaratKetentuanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.syarat_ketentuan_activity)

        val toolbar: Toolbar = findViewById(R.id.upAppbarSyaratKetentuan)
        toolbar.title = "Syarat dan Ketentuan Aksiberbagi.com"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationOnClickListener{
            val mIntent = Intent(this, DashboardActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("penggunaAktif", "true")
            mIntent.putExtras(mBundle)
            startActivity(mIntent)}
        val myWebSyarat: WebView = findViewById(R.id.webviewSyarat)
        myWebSyarat.loadUrl("https://aksiberbagi.com/relawan")
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}