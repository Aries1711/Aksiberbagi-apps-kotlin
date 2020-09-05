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
    }

    private fun redirectBack(){
        val intent = Intent (this@SignUpActivity, IntroActivity::class.java)
        startActivity(intent)
    }
}