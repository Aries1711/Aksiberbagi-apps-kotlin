package com.inddevid.aksiberbagi_donatur.view

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.services.Preferences

class WebviewInvoiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview_invoice_activity)

        val sharedPreference: Preferences = Preferences(this)
        val urlPayment: String? = sharedPreference.getValueString("paymentURL")

        val webView: WebView = findViewById(R.id.eWalletURL)
        webView.loadUrl(urlPayment!!)

    }
}