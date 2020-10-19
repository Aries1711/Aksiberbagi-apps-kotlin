package com.inddevid.aksiberbagi_donatur.view

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.inddevid.aksiberbagi_donatur.R

class InvoiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invoice_activity)

        val imageLogoBank: ImageView = findViewById(R.id.logoBank)
        Glide.with(this).load("https://aksiberbagi.com/storage/bank/bni-syariah.png").into(imageLogoBank)
    }
}