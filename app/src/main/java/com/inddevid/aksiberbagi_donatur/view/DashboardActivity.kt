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
import com.inddevid.aksiberbagi_donatur.services.Preferences


class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dashboard_activity)
        val bottomBar: BottomAppBar = findViewById(R.id.bottom_bar)
        bottomBar.bringToFront()
        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNavAksiberbagi)
        val navController = findNavController(R.id.fragment)
        bottomNav.setupWithNavController(navController)


        //retrive token from preference
        val sharedPreference: Preferences = Preferences(this)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")

        //retrieve intent from PenggunaIndex
        var valuePengguna: String? = intent.getStringExtra("penggunaAktif")
        if (valuePengguna == "true"){
            navController.navigate(R.id.penggunaIndex)
            bottomNav.menu.getItem(4).isChecked = true
        }

        //retrieve intent from DonasiSayaIndex
        var valueDonasi: String? = intent.getStringExtra("donasiSayaAktif")
        if (valueDonasi == "true"){
            navController.navigate(R.id.donasiSayaIndex)
            bottomNav.menu.getItem(3).isChecked = true
        }
        //retrieve intent from DonasiRutinActivity
        var valueFavorit: String? = intent.getStringExtra("favoritAktif")
        if (valueFavorit == "true"){
            navController.navigate(R.id.favoritIndex)
            bottomNav.menu.getItem(1).isChecked = true
        }

        //retrieve intent from DonasiRutinActivity
        var valueDonasiRutin: String? = intent.getStringExtra("berandaIndex")
        if (valueDonasiRutin == "true"){
            navController.navigate(R.id.berandaIndex)
            bottomNav.menu.getItem(0).isChecked = true
        }

        val btnDonasiAll: FloatingActionButton = findViewById(R.id.floatingBtn)
        btnDonasiAll.setOnClickListener{startActivity(
            Intent(
                this@DashboardActivity,
                ProgramAllActivity::class.java
            )
        ) }
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
            finishAffinity();
            finish();
        } else {
            super.onBackPressed()
            finishAffinity();
            finish();
//            supportFragmentManager.popBackStack()
        }
    }

}