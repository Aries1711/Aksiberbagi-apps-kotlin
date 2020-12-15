package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.androchef.happytimer.countdowntimer.DynamicCountDownView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.services.Preferences

class LelangActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lelang_activity)

        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(425, 470)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

        val sharedPreference: Preferences = Preferences(this)
        val idLelang = sharedPreference.getValueString("idLelang")
        val judulLelang = sharedPreference.getValueString("judulLelang")
        val urlImage = sharedPreference.getValueString("imgLelang")
        val nominalLelang = sharedPreference.getValueString("nominalLelang")
        val idLelangProgram = sharedPreference.getValueString("idLelangProgram")
        val judulLelangProgram = sharedPreference.getValueString("judulLelangProgram")
        val toolbar: Toolbar = findViewById(R.id.upAppbarLelang)
        toolbar.title = "Lelang Baik"
        toolbar.bringToFront()
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationOnClickListener{ startActivity(Intent(this@LelangActivity, DashboardActivity::class.java))}

        val imageLelang : ImageView = findViewById(R.id.toolbarImage)
        Glide.with(this).load(urlImage).apply(options).into(imageLelang)
        val nominalText : TextView = findViewById(R.id.nominalText)
        nominalText.text = nominalLelang

        val countDown: DynamicCountDownView = findViewById(R.id.normalCountDownLelang)
        countDown.timerTextBackgroundTintColor = ContextCompat.getColor(
            this,
            R.color.colorOrange
        )
        val timer = 3600
        countDown.initTimer(timer)
        countDown.startTimer()

        val judulText : TextView = findViewById(R.id.judulText)
        judulText.text = judulLelang

        val judulTextProgram : TextView = findViewById(R.id.judulProgramText)
        judulTextProgram.text = judulLelangProgram


        val webViewFlashsale : WebView = findViewById(R.id.webviewCeritaFlashsale)
        val url = "https://aksiberbagi.com/apk/keterangan/$idLelangProgram"
        webViewFlashsale.loadUrl(url)


    }
}