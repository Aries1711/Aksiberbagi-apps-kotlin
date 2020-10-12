package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.inddevid.aksiberbagi_donatur.R

class TentangActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tentang_activity)
        val toolbar: Toolbar = findViewById(R.id.upAppbarTentang)
        toolbar.title = "AksiBerbagi.com"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationOnClickListener{
            val mIntent = Intent(this, DashboardActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("penggunaAktif", "true")
            mIntent.putExtras(mBundle)
            startActivity(mIntent)}
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}