package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.inddevid.aksiberbagi_donatur.R

class KalkulatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kalkulator_activity)

        val toolbar: Toolbar = findViewById(R.id.upAppbarKalkulatorZakat)
        toolbar.title = "Kalkulator Zakat"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationOnClickListener{ startActivity(Intent(this@KalkulatorActivity, ZakatActivity::class.java))}

        val btnKalkulatorPenghasilan : FrameLayout = findViewById(R.id.btnKalkulatorPenghasilan)
        val btnKalkulatorPerdagangan : FrameLayout = findViewById(R.id.btnKalkulatorPerdagangan)
        val btnKalkulatorEmas : FrameLayout = findViewById(R.id.btnKalkulatorEmas)
        val btnKalkulatorTabungan : FrameLayout = findViewById(R.id.btnKalkulatorTabungan)

        btnKalkulatorPenghasilan.setOnClickListener {

        }
    }
}