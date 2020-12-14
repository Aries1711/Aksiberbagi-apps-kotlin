package com.inddevid.aksiberbagi_donatur.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.inddevid.aksiberbagi_donatur.R

class WebviewAjuanActivity : AppCompatActivity() {
    private val TAG = "Webview Publik ajuan"
    private val  USER_AGENT = "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview_ajuan_activity)

        val toolbar: Toolbar = findViewById(R.id.upAppbarWebviewAjuan)
        toolbar.title = "Publik Ajukan"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationOnClickListener{
            val mIntent = Intent(this, DashboardActivity::class.java)
            startActivity(mIntent)}

        val urlPublikAjuan: String = "https://aksiberbagi.com/ajuan-publik"
        val webView: WebView = findViewById(R.id.webviewAjuan)
        webView.settings.userAgentString = USER_AGENT
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                Log.i(TAG, "Finished loading URL: $url")
            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                Log.e(TAG, "Error: $description")
                Log.e(TAG, "UrlError: $failingUrl")
                val uri = Uri.parse(failingUrl)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    val toast = Toast.makeText(this@WebviewAjuanActivity,"Mohon install Aplikasi pembayaran pihak ketiga terlebih dahulu", Toast.LENGTH_LONG)
                    toast.show()
                }
            }
        }
        webView.loadUrl(urlPublikAjuan)
    }
    override fun onBackPressed() {
        val mIntent = Intent(this, DashboardActivity::class.java)
        startActivity(mIntent)
    }

}