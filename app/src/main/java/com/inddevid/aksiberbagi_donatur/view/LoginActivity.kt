package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.inddevid.aksiberbagi_donatur.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        supportActionBar?.hide()
        val closeBtnLogin: Button = findViewById(R.id.closeLogin)
        val submitLogin: Button = findViewById(R.id.loginSubmit)
        closeBtnLogin.setOnClickListener{ startActivity(Intent(this@LoginActivity, IntroActivity::class.java)) }
        submitLogin.setOnClickListener { startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))  }
    }
}