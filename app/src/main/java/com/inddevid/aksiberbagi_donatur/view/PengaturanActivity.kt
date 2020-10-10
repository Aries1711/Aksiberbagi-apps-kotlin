package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.inddevid.aksiberbagi_donatur.R

class PengaturanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pengaturan_activity)

        val toolbar: Toolbar = findViewById(R.id.upAppbarPengaturan)
        toolbar.title = "Pengaturan"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationOnClickListener{startActivity(Intent(this@PengaturanActivity, PenggunaIndex::class.java))}
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}