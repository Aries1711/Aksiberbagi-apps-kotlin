package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.google.android.material.textfield.TextInputEditText
import com.inddevid.aksiberbagi_donatur.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        supportActionBar?.hide()
        val closeBtnLogin: Button = findViewById(R.id.closeLogin)
        val submitLogin: Button = findViewById(R.id.loginSubmit)
        closeBtnLogin.setOnClickListener{ startActivity(Intent(this@LoginActivity, IntroActivity::class.java)) }


        val phoneNumber: TextInputEditText = findViewById(R.id.phoneNumber)
        val userPassword: TextInputEditText = findViewById(R.id.namePassword)
        submitLogin.setOnClickListener {
            var phone = phoneNumber.text.toString()
            var password = userPassword.text.toString()
        }

    }

    private fun submitLogin(phone:String, password: String){
        AndroidNetworking.initialize(this)
        AndroidNetworking.get("")
    }

}