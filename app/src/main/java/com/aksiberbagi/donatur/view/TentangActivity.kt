package com.aksiberbagi.donatur.view

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.aksiberbagi.donatur.R

class TentangActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tentang_activity)

        val toolbar: Toolbar = findViewById(R.id.upAppbarTentang)
        toolbar.title = "AksiBerbagi.com"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationOnClickListener{
            val mIntent = Intent(this, DashboardActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("penggunaAktif", "true")
            mIntent.putExtras(mBundle)
            startActivity(mIntent)}
        val webView: WebView = findViewById(R.id.webviewBantuan)

        webView.loadUrl("https://aksiberbagi.com/apk/tentang")

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}