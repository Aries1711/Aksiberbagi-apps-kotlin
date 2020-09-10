package com.inddevid.aksiberbagi_donatur.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.inddevid.aksiberbagi_donatur.R


class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_activity)
        val appBar: Toolbar = findViewById(R.id.upAppbar)
        setSupportActionBar(appBar)
    }


}