package com.aksiberbagi.donatur.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.services.Preferences


class DashboardActivity : AppCompatActivity() {
    private var countBack = 0
    private var backPressed: Long = 0
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
        val backToast = Toast.makeText(
            applicationContext,
            "tekan dua kali untuk keluar",
            Toast.LENGTH_LONG
        )
        if(backPressed + 2000 > System.currentTimeMillis()){
            backToast.cancel()
            super.onBackPressed()
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
        }else{
            backToast.show()
        }
        backPressed = System.currentTimeMillis()


//        countBack++
//        if (countBack <= 1) {
//            val toast = Toast.makeText(
//                applicationContext,
//                "tekan dua kali untuk keluar",
//                Toast.LENGTH_LONG
//            )
//            toast.show()
//        } else if (countBack > 1) {
//            super.onBackPressed()
//            val a = Intent(Intent.ACTION_MAIN)
//            a.addCategory(Intent.CATEGORY_HOME)
//            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(a)
//        }
    }

}