package com.inddevid.aksiberbagi_donatur.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.inddevid.aksiberbagi_donatur.R

class PasswordSetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.password_set_activity)

        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Ubah Kata Sandi"
        val textInput: TextInputLayout = findViewById(R.id.passwordConfirmation1)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}