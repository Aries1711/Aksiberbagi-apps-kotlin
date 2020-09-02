package com.inddevid.aksiberbagi_donatur.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.inddevid.aksiberbagi_donatur.R

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)
        supportActionBar?.hide()
    }
}