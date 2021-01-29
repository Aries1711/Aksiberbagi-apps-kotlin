package com.aksiberbagi.donatur.view

import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.services.Preferences


class PengaturanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pengaturan_activity)

        val sharedPreference: Preferences = Preferences(this)
        val toolbar: Toolbar = findViewById(R.id.upAppbarPengaturan)
        toolbar.title = "Pengaturan"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationOnClickListener{
            val mIntent = Intent(this, DashboardActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("penggunaAktif", "true")
            mIntent.putExtras(mBundle)
            startActivity(mIntent)}
        val switchAnonimSet: Switch = findViewById(R.id.switchAnonim)
        val switchReminder: Switch = findViewById(R.id.switchReminder)

        val switchAnonimValue: String? = sharedPreference.getValueString("ANONIM")

        if (switchAnonimValue == "T"){
            switchAnonimSet.isChecked = true
        }


        switchAnonimSet.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
            if (message == "Switch1:ON"){
                sharedPreference.save("ANONIM", "T")
            }else{
                sharedPreference.save("ANONIM", "F")
            }
        }

        switchReminder.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
            if (message == "Switch1:ON"){

                val mIntent = Intent(this@PengaturanActivity, DonasiRutinActivity::class.java)
                val mBundle = Bundle()
                mBundle.putString("prevNavigate", "Pengaturan" )
                mIntent.putExtras(mBundle)
                this.startActivity(mIntent)
            }else{
                //todo
            }
        }

        val penggunaWAText : TextView = findViewById(R.id.penggunaWAText)
        val penggunaWa = sharedPreference.getValueString("penggunaWA")
        penggunaWAText.text = penggunaWa

        val ubahPasswordBtn : TextView = findViewById(R.id.penggunaUbahPasswordBtn)
        ubahPasswordBtn.setOnClickListener {
            startActivity(Intent(this ,PasswordSetActivity::class.java))
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}