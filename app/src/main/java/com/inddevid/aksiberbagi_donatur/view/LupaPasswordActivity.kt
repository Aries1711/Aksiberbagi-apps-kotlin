package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.services.Preferences

class LupaPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lupa_password_activity)

        val sharedPreference: Preferences = Preferences(this)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")

        val toolbar: Toolbar = findViewById(R.id.upAppbarPasswordForgot)
        toolbar.title = "Lupa Password"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationOnClickListener{
            startActivity(Intent(this@LupaPasswordActivity, LoginActivity::class.java ))
        }

        val layoutUtama: LinearLayout = findViewById(R.id.layoutLupaPassword)
        val layoutVerifikasi: LinearLayout = findViewById(R.id.layoutVerifikasiKode)

        val btnSubmitNotelp: Button = findViewById(R.id.submitForgot)
        btnSubmitNotelp.setOnClickListener {
            layoutUtama.visibility = View.GONE
            layoutVerifikasi.visibility = View.VISIBLE
        }
        val btnSubmitKode: Button = findViewById(R.id.submitVerifikasi)
        btnSubmitKode.setOnClickListener {
            layoutUtama.visibility = View.VISIBLE
            layoutVerifikasi.visibility = View.GONE
        }


    }
}