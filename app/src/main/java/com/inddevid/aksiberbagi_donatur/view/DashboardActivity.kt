package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.inddevid.aksiberbagi_donatur.R


class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.dashboard_activity)
        val bottomBar: BottomAppBar = findViewById(R.id.bottom_bar)
        bottomBar.bringToFront()
        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNavAksiberbagi)
        val navController = findNavController(R.id.fragment)
        bottomNav.setupWithNavController(navController)

        val btnDonasiAll: FloatingActionButton = findViewById(R.id.floatingBtn)
        btnDonasiAll.setOnClickListener{startActivity(Intent(this@DashboardActivity, ProgramAllActivity::class.java)) }
    }


}