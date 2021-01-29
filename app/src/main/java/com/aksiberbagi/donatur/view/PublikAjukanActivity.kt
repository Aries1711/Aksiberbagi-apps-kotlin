package com.aksiberbagi.donatur.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.aksiberbagi.donatur.R

class PublikAjukanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.publik_ajukan_activity)

        val toolbar: Toolbar = findViewById(R.id.upAppbarPublikAjukan)
        toolbar.title = "Publik Ajukan"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationOnClickListener{ startActivity(Intent(this@PublikAjukanActivity, DashboardActivity::class.java))}

        val layoutBanner : LinearLayout = findViewById(R.id.layoutBannerPublikAjukan)
        val layoutForm : LinearLayout = findViewById(R.id.layoutFormAjukan)
        val btnShowForm : Button = findViewById(R.id.ajukanBtnForm)
        val btnSubmitForm : Button = findViewById(R.id.ajukanBtnFormSubmit)

        btnShowForm.setOnClickListener {
//            layoutBanner.visibility = View.GONE
//            layoutForm.visibility = View.VISIBLE
            startActivity(Intent(this@PublikAjukanActivity, WebviewAjuanActivity::class.java))
        }
    }
}