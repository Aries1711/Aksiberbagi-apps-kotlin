package com.inddevid.aksiberbagi_donatur.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.inddevid.aksiberbagi_donatur.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro_activity)

        //code for noActionBar
        supportActionBar?.hide()

    }
}