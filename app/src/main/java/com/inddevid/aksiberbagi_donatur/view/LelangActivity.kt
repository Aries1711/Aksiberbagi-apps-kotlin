package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.androchef.happytimer.countdowntimer.DynamicCountDownView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.services.Preferences


class LelangActivity : AppCompatActivity() {
    private val TAG = "Lelang Activity Scroll"

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
        val toolbarLelang : Toolbar = findViewById(R.id.toolbarLelang)
        toolbarLelang.bringToFront()
        val btnBack: LinearLayout = findViewById(R.id.backLelangBtn)
        val btnShare: LinearLayout = findViewById(R.id.shareLelangBtn)
        val btnOption: LinearLayout = findViewById(R.id.opsiLelangBtn)
        val layoutScroll : NestedScrollView = findViewById(R.id.layoutLelangKonten)
        layoutScroll.viewTreeObserver.addOnScrollChangedListener {
            val scrollY: Int = layoutScroll.scrollY
            if (scrollY >= 1020){
                toolbarLelang.setBackgroundColor(ContextCompat.getColor(this, R.color.colorInputStrokeBlue))
                btnBack.background = ContextCompat.getDrawable(this, R.drawable.background_transparent)
                btnShare.background = ContextCompat.getDrawable(this, R.drawable.background_transparent)
                btnOption.background = ContextCompat.getDrawable(this, R.drawable.background_transparent)
            }else{
                toolbarLelang.background = ContextCompat.getDrawable(this, R.drawable.background_transparent)
                btnBack.background = ContextCompat.getDrawable(this, R.drawable.btn_toolbar_active)
                btnShare.background = ContextCompat.getDrawable(this, R.drawable.btn_toolbar_active)
                btnOption.background = ContextCompat.getDrawable(this, R.drawable.btn_toolbar_active)
            }
            Log.d(TAG, "scrollY: $scrollY")
        }

        btnBack.setOnClickListener {
            startActivity(Intent(this@LelangActivity, DashboardActivity::class.java))
        }

        btnShare.setOnClickListener {
            val shareIntent = Intent()
            val pesan = "Yuk ikutan lelang baik Aksiberbagi.com, Bagikan kemulian dengan lelang baik"
            val urlProgram = "https://aksiberbagi.com/lelang-baik"
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, pesan)
            shareIntent.putExtra(Intent.EXTRA_TEXT, urlProgram)
            shareIntent.type = "text/plain"
            shareIntent.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            startActivity(Intent.createChooser(shareIntent, "Bagikan flashsale dengan kemulian bersedekah"))
        }


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