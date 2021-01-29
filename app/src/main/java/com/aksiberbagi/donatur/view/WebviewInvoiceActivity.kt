package com.aksiberbagi.donatur.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.services.Preferences


class WebviewInvoiceActivity : AppCompatActivity() {
    private val TAG = "Program Detail"
    public val  USER_AGENT = "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview_invoice_activity)

        val sharedPreference: Preferences = Preferences(this)
        val urlPayment:String = sharedPreference.getValueString("invoiceUrl")!!

        val webview: WebView = findViewById(R.id.eWalletURL)
        webview.settings.userAgentString = USER_AGENT
        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true
        webview.settings.useWideViewPort = true
        webview.settings.loadWithOverviewMode = true
        webview.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY

        val layoutLanjut : LinearLayout = findViewById(R.id.layoutLanjutApkPembayaran)



        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
//                webview.loadUrl(url)
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
                gone(view)
                val uri = Uri.parse(failingUrl)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    val toast = Toast.makeText(this@WebviewInvoiceActivity,"Mohon install Aplikasi pembayaran pihak ketiga terlebih dahulu", Toast.LENGTH_LONG)
                        toast.show()
                }
            }
        }
        webview.loadUrl(urlPayment)

    }

    fun gone(view: View){
        view.visibility = View.GONE
    }

    fun show(view: View){
        view.visibility = View.VISIBLE
    }
}