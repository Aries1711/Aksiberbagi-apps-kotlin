package com.inddevid.aksiberbagi_donatur.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.inddevid.aksiberbagi_donatur.R

class SignUpActivity : AppCompatActivity() {
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)
        supportActionBar?.hide()
        val backButton: Button = findViewById(R.id.closeDaftar)
        backButton.setOnClickListener{ redirectBack() }
        val masukLogin: Button = findViewById(R.id.btnMasuk)
        masukLogin.setOnClickListener{ redirectLogin() }
    }

    private fun redirectBack(){
        val intent = Intent (this@SignUpActivity, IntroActivity::class.java)
        startActivity(intent)
    }
    private fun redirectLogin(){
        val intent = Intent (this@SignUpActivity, LoginActivity::class.java)
        startActivity(intent)
    }
}