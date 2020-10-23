package com.inddevid.aksiberbagi_donatur.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.services.Preferences
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)
        supportActionBar?.hide()
        val backButton: Button = findViewById(R.id.closeDaftar)
        backButton.setOnClickListener{ startActivity(Intent(this@SignUpActivity, IntroActivity::class.java))}
        val masukLogin: Button = findViewById(R.id.btnMasuk)
        masukLogin.setOnClickListener{ startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))}

        //variabel submit signup
        var phoneInput: TextInputEditText = findViewById(R.id.phoneInput)
        var phoneLayout: TextInputLayout = findViewById(R.id.phoneLayout)

        var fullnameInput: TextInputEditText = findViewById(R.id.fullnameInput)
        var fullnameLayout: TextInputLayout =findViewById(R.id.fullnameLayout)

        var passwordInput: TextInputEditText = findViewById(R.id.passwordInput)
        var passwordLayout: TextInputLayout = findViewById(R.id.passwordLayout)

        var passwordConfirmInput: TextInputEditText = findViewById(R.id.passwordConfirmationInput)
        var passwordConfirmLayout: TextInputLayout = findViewById(R.id.passwordConfirmationInput)


        val submitSignUp: Button = findViewById(R.id.signupSubmit)
        submitSignUp.setOnClickListener {
            var phone:String = phoneInput.text.toString()
            var fullname:String = fullnameInput.text.toString()
            var password:String = passwordInput.text.toString()
            var confirmation:String = passwordConfirmInput.text.toString()

            validateForm(phone, fullname,password,confirmation, phoneLayout, fullnameLayout,passwordLayout, passwordConfirmLayout )
        }
    }

    private fun validateForm(
        phone: String,
        fullname: String,
        password: String,
        confirm: String,

        phoneLayout: TextInputLayout,
        fullnameLayout: TextInputLayout,
        passwordLayout: TextInputLayout,
        passwordConfirmLayout: TextInputLayout

    ){
        if (phone != "" && fullname != "" && password != "" && confirm != ""){
            submitSignup(phone,fullname, password , confirm, phoneLayout, fullnameLayout, passwordLayout,passwordConfirmLayout)
        }else if (phone == ""){
            phoneLayout.requestFocus()
            phoneLayout.boxStrokeColor = ContextCompat.getColor(this, R.color.error_color)
            phoneLayout.helperText = "Harap Masukkan No Telepon"
        }else if (fullname == ""){
            fullnameLayout.requestFocus()
            fullnameLayout.boxStrokeColor = ContextCompat.getColor(this, R.color.error_color)
            fullnameLayout.helperText = "Harap Masukkan Nama Anda"
        }else if (password == ""){
            passwordLayout.requestFocus()
            passwordLayout.boxStrokeColor = ContextCompat.getColor(this, R.color.error_color)
            passwordLayout.helperText = "Password Tidak Boleh Kosong"
        }else if (confirm == ""){
            passwordLayout.requestFocus()
            passwordLayout.boxStrokeColor = ContextCompat.getColor(this, R.color.error_color)
            passwordConfirmLayout.helperText = " Konfirmasi Password Tidak Boleh Kosong"
        }
    }

    private fun submitSignup(
        phone: String,
        fullname: String,
        password: String,
        confirm: String,
        phoneLayout: TextInputLayout,
        fullnameLayout: TextInputLayout,
        passwordLayout: TextInputLayout,
        passwordConfirmLayout: TextInputLayout
    ){
        val body = JSONObject()
        val sharedPreference: Preferences = Preferences(this)
    }
}