package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.inddevid.aksiberbagi_donatur.R

class BantuanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bantuan_activity)

        val toolbar: Toolbar = findViewById(R.id.upAppbarBantuan)
        toolbar.title = "Bantuan"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE)
        toolbar.setNavigationOnClickListener{
            val mIntent = Intent(this, DashboardActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("penggunaAktif", "true")
            mIntent.putExtras(mBundle)
            startActivity(mIntent)}

        val myWebView: WebView = findViewById(R.id.webviewBantuan)
        myWebView.loadUrl("https://aksiberbagi.com/kontak")

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}