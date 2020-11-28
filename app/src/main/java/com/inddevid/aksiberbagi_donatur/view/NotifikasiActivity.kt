package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.inddevid.aksiberbagi_donatur.R

class NotifikasiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notifikasi_activity)

        val toolbar: Toolbar = findViewById(R.id.upAppbarNotifikasi)
        toolbar.inflateMenu(R.menu.pengguna_upbar_menu)
        toolbar.title = "Notifikasi"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        toolbar.setNavigationOnClickListener{
            startActivity(Intent(this@NotifikasiActivity, DashboardActivity::class.java))
        }

        val btnProgramAll : Button = findViewById(R.id.donasiBtn)
        btnProgramAll.setOnClickListener {
            startActivity(Intent(this@NotifikasiActivity, ProgramAllActivity::class.java))
        }
    }
}