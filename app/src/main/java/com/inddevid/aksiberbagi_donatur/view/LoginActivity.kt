package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColor
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.services.ApiError
import com.inddevid.aksiberbagi_donatur.services.ApiService
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    private val TAG = "MyActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        supportActionBar?.hide()
        val closeBtnLogin: Button = findViewById(R.id.closeLogin)
        val submitLogin: Button = findViewById(R.id.loginSubmit)
        closeBtnLogin.setOnClickListener{ startActivity(
            Intent(
                this@LoginActivity,
                IntroActivity::class.java
            )
        ) }


        var phoneNumber: TextInputEditText = findViewById(R.id.phoneNumber)
        var layoutPhone: TextInputLayout = findViewById(R.id.namePhone)

        var userPassword: TextInputEditText = findViewById(R.id.password)
        var layoutPassword: TextInputLayout =findViewById(R.id.namePassword)

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

    }

    private fun validateForm(
        phoneNumber: String,
        password: String,
        layoutPhone: TextInputLayout,
        layoutPass: TextInputLayout
    ){
        if (phoneNumber != "" && password != ""){
            submitLogin(phoneNumber, password, layoutPhone, layoutPass)
        }else if (phoneNumber == ""){
            layoutPhone.requestFocus()
            layoutPhone.boxStrokeColor = getColor(this, R.color.error_color)
            layoutPhone.helperText = "Harap Masukkan No Telepon"
        }else if (password == ""){
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
    ){
        val body = JSONObject()
        try {
            body.put("nomor_telepon", phone)
            body.put("password", password)
            ApiService.postMasuk(body)
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        try {
                            if (response?.getString("message")
                                    .equals("Nomor telepon tidak terdaftar")
                            ) {
                                layoutPhone.requestFocus()
                                layoutPhone.boxStrokeColor = getColor(
                                    this@LoginActivity,
                                    R.color.error_color
                                )
                                layoutPhone.helperText = response?.getString("message")
                            } else if (response?.getString("message")
                                    .equals("Data donatur ditemukan")
                            ) {
                                val data: JSONObject? = response?.getJSONObject("data")
                                val donaturJson: JSONObject? = data?.getJSONObject("donatur")
                                val toast = Toast.makeText(
                                    applicationContext, donaturJson?.getString(
                                        "tbldonatur_nama"
                                    ), Toast.LENGTH_LONG
                                )
                                toast.show()
                            }
                        } catch (e: JSONException) {
                            val toast = Toast.makeText(
                                applicationContext,
                                "Kesalahan Try",
                                Toast.LENGTH_LONG
                            )
                            toast.show()
                        }
                    }

                    override fun onError(anError: ANError? ) {
                        val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                        if (apiError?.message == "Nomor telepon tidak terdaftar"){
                            val toast = Toast.makeText(applicationContext, "Nomor Tidak terdaftar oke okee", Toast.LENGTH_LONG)
                            toast.show()
                        }
                        Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                        Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                        Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
                    }

                })
        }catch (e: JSONException){

        }
    }



}