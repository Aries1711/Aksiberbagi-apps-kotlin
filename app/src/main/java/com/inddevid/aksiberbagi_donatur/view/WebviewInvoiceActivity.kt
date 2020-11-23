package com.inddevid.aksiberbagi_donatur.view

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
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.services.Preferences


class WebviewInvoiceActivity : AppCompatActivity() {
    private val TAG = "Program Detail"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview_invoice_activity)

        val sharedPreference: Preferences = Preferences(this)
        val urlPayment:String = sharedPreference.getValueString("invoiceUrl")!!

        val webview: WebView = findViewById(R.id.eWalletURL)
        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true;
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