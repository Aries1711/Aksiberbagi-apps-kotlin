package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
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
        val layoutPenghasilan : LinearLayout = findViewById(R.id.kalkulatorPenghasilan)

        val btnKalkulatorPerdagangan : FrameLayout = findViewById(R.id.btnKalkulatorPerdagangan)
        val layoutPerdagangan : LinearLayout = findViewById(R.id.kalkulatorPerdagangan)

        val btnKalkulatorEmas : FrameLayout = findViewById(R.id.btnKalkulatorEmas)
        val layoutEmas : LinearLayout = findViewById(R.id.kalkulatorEmas)

        val btnKalkulatorTabungan : FrameLayout = findViewById(R.id.btnKalkulatorTabungan)
        val layoutTabungan : LinearLayout = findViewById(R.id.kalkulatorTabungan)

        layoutPenghasilan.visibility = View.VISIBLE

        btnKalkulatorPenghasilan.setOnClickListener {
            layoutPenghasilan.visibility = View.VISIBLE
            layoutPerdagangan.visibility = View.GONE
            layoutEmas.visibility = View.GONE
            layoutTabungan.visibility = View.GONE
        }

        btnKalkulatorPerdagangan.setOnClickListener {
            layoutPerdagangan.visibility = View.VISIBLE
            layoutPenghasilan.visibility = View.GONE
            layoutEmas.visibility = View.GONE
            layoutTabungan.visibility = View.GONE
        }

        btnKalkulatorEmas.setOnClickListener {
            layoutEmas.visibility = View.VISIBLE
            layoutPenghasilan.visibility = View.GONE
            layoutPerdagangan.visibility = View.GONE
            layoutTabungan.visibility = View.GONE
        }

        btnKalkulatorTabungan.setOnClickListener {
            layoutTabungan.visibility = View.VISIBLE
            layoutPenghasilan.visibility = View.GONE
            layoutEmas.visibility = View.GONE
            layoutPerdagangan.visibility = View.GONE
        }



    }
}