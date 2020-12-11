package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.inddevid.aksiberbagi_donatur.R

class ZakatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zakat_activity)

        val toolbar: Toolbar = findViewById(R.id.upAppbarZakat)
        toolbar.title = "Zakat"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationOnClickListener{ startActivity(Intent(this@ZakatActivity, DashboardActivity::class.java))}

        //glide option
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(500, 190)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

        //tombol bayar zakat header dan kalkulator
        val btnBayarZakatHeader: LinearLayout = findViewById(R.id.btnZakatBayar)
        val btnZakatKalkulator: LinearLayout = findViewById(R.id.btnZakatHitung)

        btnBayarZakatHeader.setOnClickListener {

        }

        btnZakatKalkulator.setOnClickListener {
            startActivity(Intent(this@ZakatActivity, KalkulatorActivity::class.java))
        }


        //list pilihan zakat berdasarkan jenis
        val imageZakatTabungan: ImageView = findViewById(R.id.imageZakatTabungan)
        Glide.with(this).load("https://aksiberbagi.com/storage/program/2a2692b30977847c99b23f478aab9e02_zsimpan.jpg").apply(options).into(imageZakatTabungan)

        val imageZakatEmas: ImageView = findViewById(R.id.imageZakatEmas)
        Glide.with(this).load("https://aksiberbagi.com/storage/program/cbabdc05e9c3b9e802e1c028c42470b9_zemas.jpg").apply(options).into(imageZakatEmas)

        val imageZakatDagang: ImageView = findViewById(R.id.imageZakatPerdagangan)
        Glide.with(this).load("https://aksiberbagi.com/storage/program/0ee1c6f32d6d11f44cbb9ffb42c3ed84_zdagang.jpg").apply(options).into(imageZakatDagang)

        val imageZakatPenghasilan: ImageView = findViewById(R.id.imageZakatPenghasilan)
        Glide.with(this).load("https://aksiberbagi.com/storage/program/04fb0a8e0d93c42586fc31f186c07bde_zhasilan.jpg").apply(options).into(imageZakatPenghasilan)
    }
}