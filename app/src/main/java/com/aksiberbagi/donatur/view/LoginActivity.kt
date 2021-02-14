package com.aksiberbagi.donatur.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColor
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.services.ApiError
import com.aksiberbagi.donatur.services.ApiService
import com.aksiberbagi.donatur.services.Preferences
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    private val NO_TELP = "085322778935";
    private val PASSWORD = "123456";
    private val IS_TESTING = false;

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        supportActionBar?.hide()
        val closeBtnLogin: Button = findViewById(R.id.closeLogin)
        val submitLogin: Button = findViewById(R.id.loginSubmit)
        closeBtnLogin.setOnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    IntroActivity::class.java
                )
            )
        }

        val btnForgotPassword: Button = findViewById(R.id.lupaAkun)
        btnForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, LupaPasswordActivity::class.java))
        }

        var phoneNumber: TextInputEditText = findViewById(R.id.phoneNumber)
        var layoutPhone: TextInputLayout = findViewById(R.id.namePhone)

        var userPassword: TextInputEditText = findViewById(R.id.password)
        var layoutPassword: TextInputLayout = findViewById(R.id.namePassword)

        submitLogin.setOnClickListener {

            var phone = phoneNumber.text.toString()
            var password = userPassword.text.toString()
            validateForm(phone, password, layoutPhone, layoutPassword)
        }

        phoneNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                layoutPhone.boxStrokeColor = getColor(
                    this@LoginActivity,
                    R.color.colorIndicatorPrimary
                )
                layoutPhone.helperText = ""
            }

        })

        userPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                layoutPassword.boxStrokeColor = getColor(
                    this@LoginActivity,
                    R.color.colorIndicatorPrimary
                )
                layoutPassword.helperText = ""
            }

        })

        if (this.IS_TESTING) {
            phoneNumber.setText(this.NO_TELP.toEditable())
            userPassword.setText(this.PASSWORD.toEditable())
        }
    }

    private fun validateForm(
        phoneNumber: String,
        password: String,
        layoutPhone: TextInputLayout,
        layoutPass: TextInputLayout
    ) {
        if (phoneNumber != "" && password != "") {
            submitLogin(phoneNumber, password, layoutPhone, layoutPass)
        } else if (phoneNumber == "") {
            layoutPhone.requestFocus()
            layoutPhone.boxStrokeColor = getColor(this, R.color.error_color)
            layoutPhone.helperText = "Harap Masukkan No Telepon"
        } else if (password == "") {
            layoutPass.requestFocus()
            layoutPass.boxStrokeColor = getColor(this, R.color.error_color)
            layoutPass.helperText = "Password Tidak Boleh Kosong"
        }
    }

    private fun submitLogin(
        phone: String,
        password: String,
        layoutPhone: TextInputLayout,
        layoutPass: TextInputLayout
    ) {
        val body = JSONObject()
        val sharedPreference: Preferences = Preferences(this)
        try {
            body.put("nomor_telepon", phone)
            body.put("password", password)
            ApiService.postMasuk(body)
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        try {
                            if (response?.getString("message").equals("Data donatur ditemukan")) {
                                val data: JSONObject? = response?.getJSONObject("data")
                                val token: String? = data?.getString("token")
                                val dataDonatur: JSONObject? = data?.getJSONObject("donatur")
                                val donaturWa = dataDonatur?.getString("tbldonatur_nowa")
                                val donaturNama = dataDonatur?.getString("tbldonatur_nama")
                                val donaturAlamat = dataDonatur?.getString("tbldonatur_alamat")
                                val donaturProfesi = dataDonatur?.getString("tbldonatur_pekerjaan")
                                val donaturEmail = dataDonatur?.getString("tbldonatur_email")
                                var donaturKodeNegara = dataDonatur?.getString("kode_negara_no_hp")
                                val donaturPanggilan = "Kak"
                                val donaturLinkAja = dataDonatur?.getString("no_link_aja")
                                val donaturDana = dataDonatur?.getString("no_dana")
                                val donaturOvo = dataDonatur?.getString("no_ovo")

                                //save token and donatur id on preferences
                                if (token != null) {
                                    sharedPreference.save("TOKEN", token)
                                    sharedPreference.save("penggunaWA", donaturWa)
                                    sharedPreference.save("penggunaNAMA", donaturNama)
                                    sharedPreference.save("penggunaAlamat", donaturAlamat)
                                    sharedPreference.save("penggunaProfesi", donaturProfesi)
                                    sharedPreference.save("penggunaEmail", donaturEmail)
                                    if (donaturKodeNegara == "null") donaturKodeNegara = "+62"
                                    sharedPreference.save("penggunaKodeNegara", donaturKodeNegara)
                                    sharedPreference.save("penggunaPanggilan", donaturPanggilan)
                                    sharedPreference.save("penggunaLinkAja", donaturLinkAja)
                                    sharedPreference.save("penggunaDana", donaturDana)
                                    sharedPreference.save("penggunaOvo", donaturOvo)
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            DashboardActivity::class.java
                                        )
                                    )
                                }
                            }
                        } catch (e: JSONException) {
                            val toast = Toast.makeText(
                                applicationContext,
                                "Kesalahan Sistem",
                                Toast.LENGTH_LONG
                            )
                            toast.show()
                        }
                    }

                    override fun onError(anError: ANError?) {
                        val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                        if (anError?.errorDetail!!.equals("connectionError")) {
                            val toast = Toast.makeText(
                                this@LoginActivity,
                                "Ada masalah dengan Koneksi Internet Anda",
                                Toast.LENGTH_LONG
                            )
                            toast.show()
                            return
                        }
                        if (anError?.errorDetail!!.equals("connectionError")) {
                            val toast = Toast.makeText(
                                this@LoginActivity,
                                "Ada masalah dengan Koneksi Internet Anda",
                                Toast.LENGTH_LONG
                            )
                            toast.show()

                        }
                        if (apiError?.message == "Nomor telepon tidak terdaftar") {
                            layoutPhone.requestFocus()
                            layoutPhone.boxStrokeColor = getColor(
                                this@LoginActivity,
                                R.color.error_color
                            )
                            layoutPhone.helperText = apiError?.message
                        } else if (apiError?.message == "Password yang dimasukkan salah") {
                            layoutPass.requestFocus()
                            layoutPass.boxStrokeColor = getColor(
                                this@LoginActivity,
                                R.color.error_color
                            )
                            layoutPass.helperText = apiError?.message
                        } else {
                            Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                            Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                            Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
                            val jsonObject = JSONObject(anError?.errorBody);
                            val jsonArray = jsonObject.getJSONArray("password");
                            if (jsonArray[0] == "The password must be at least 5 characters.") {
                                layoutPass.requestFocus()
                                layoutPass.boxStrokeColor = getColor(
                                    this@LoginActivity,
                                    R.color.error_color
                                )
                                layoutPass.helperText = "Password setidaknya berisi 5 karakter"
                            }
                        }
                        Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                        Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                        Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
                    }

                })
        } catch (e: JSONException) {

        }
    }


}