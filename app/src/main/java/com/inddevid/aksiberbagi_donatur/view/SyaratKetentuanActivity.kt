package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.services.Preferences

class SyaratKetentuanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.syarat_ketentuan_activity)

        val sharedPreference: Preferences = Preferences(this)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")
        val toolbar: Toolbar = findViewById(R.id.upAppbarSyaratKetentuan)
        toolbar.title = "Syarat dan Ketentuan Aksiberbagi.com"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationOnClickListener{
            if (retrivedToken == "" || retrivedToken == "null"){
                startActivity(Intent(this, SignUpActivity::class.java))
            }else{
                val mIntent = Intent(this, DashboardActivity::class.java)
                val mBundle = Bundle()
                mBundle.putString("penggunaAktif", "true")
                mIntent.putExtras(mBundle)
                startActivity(mIntent)
            }
        }

        val btnRelawan: FrameLayout = findViewById(R.id.relawanSyarat)
        var textRelawan: TextView = findViewById(R.id.relawanSyaratText)

        val btnDonatur: FrameLayout = findViewById(R.id.relawanDonatur)
        var textDonatur: TextView = findViewById(R.id.relawanDonaturText)

        val btnAjukan: FrameLayout = findViewById(R.id.relawanAjukan)
        var textAjukan: TextView = findViewById(R.id.relawanAjukanText)

        var stringUrl:String = "https://aksiberbagi.com/apk/s&k"

        val myWebSyarat: WebView = findViewById(R.id.webviewSyarat)
        myWebSyarat.loadUrl(stringUrl)


        btnRelawan.background = getDrawable(R.drawable.rounded_linear_layout)
        textRelawan.setTextColor(Color.parseColor("#FFFFFF"));

        btnRelawan.setOnClickListener {
            btnAjukan.background = getDrawable(R.drawable.rounded_linear_layout_width)
            textAjukan.setTextColor(Color.parseColor("#15BBDA"));
            btnDonatur.background = getDrawable(R.drawable.rounded_linear_layout_width)
            textDonatur.setTextColor(Color.parseColor("#15BBDA"));
            btnRelawan.background = getDrawable(R.drawable.rounded_linear_layout)
            textRelawan.setTextColor(Color.parseColor("#FFFFFF"));
            stringUrl = "https://aksiberbagi.com/relawan"
            myWebSyarat.loadUrl(stringUrl)
        }

        btnDonatur.setOnClickListener {
            btnAjukan.background = getDrawable(R.drawable.rounded_linear_layout_width)
            textAjukan.setTextColor(Color.parseColor("#15BBDA"));
            btnRelawan.background = getDrawable(R.drawable.rounded_linear_layout_width)
            textRelawan.setTextColor(Color.parseColor("#15BBDA"));
            btnDonatur.background = getDrawable(R.drawable.rounded_linear_layout)
            textDonatur.setTextColor(Color.parseColor("#FFFFFF"));
            stringUrl = "https://aksiberbagi.com/s&k"
            myWebSyarat.loadUrl(stringUrl)
        }

        btnAjukan.setOnClickListener {
            btnRelawan.background = getDrawable(R.drawable.rounded_linear_layout_width)
            textRelawan.setTextColor(Color.parseColor("#15BBDA"));
            btnDonatur.background = getDrawable(R.drawable.rounded_linear_layout_width)
            textDonatur.setTextColor(Color.parseColor("#15BBDA"));
            btnAjukan.background = getDrawable(R.drawable.rounded_linear_layout)
            textAjukan.setTextColor(Color.parseColor("#FFFFFF"));
            stringUrl = "https://aksiberbagi.com/kontak"
            myWebSyarat.loadUrl(stringUrl)
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}