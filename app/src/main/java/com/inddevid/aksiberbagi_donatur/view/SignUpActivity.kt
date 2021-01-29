package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.services.ApiError
import com.inddevid.aksiberbagi_donatur.services.ApiService
import com.inddevid.aksiberbagi_donatur.services.Preferences
import org.json.JSONException
import org.json.JSONObject


class SignUpActivity : AppCompatActivity() {
    private val TAG = "SignUpActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)
        supportActionBar?.hide()

        val backButton: Button = findViewById(R.id.closeDaftar)
        backButton.setOnClickListener{ startActivity(
            Intent(
                this@SignUpActivity,
                IntroActivity::class.java
            )
        )}
        val masukLogin: Button = findViewById(R.id.btnMasuk)
        masukLogin.setOnClickListener{ startActivity(
            Intent(
                this@SignUpActivity,
                LoginActivity::class.java
            )
        )}

        val syaratBtn : TextView = findViewById(R.id.syaratBtn)
        syaratBtn.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        syaratBtn.setOnClickListener {
            startActivity(Intent(this,SyaratKetentuanActivity::class.java))
        }

        //variabel submit signup
        var phoneInput: TextInputEditText = findViewById(R.id.phoneInput)
        var phoneLayout: TextInputLayout = findViewById(R.id.phoneLayout)

        var fullnameInput: TextInputEditText = findViewById(R.id.fullnameInput)
        var fullnameLayout: TextInputLayout =findViewById(R.id.fullnameLayout)

        var passwordInput: TextInputEditText = findViewById(R.id.passwordInput)
        var passwordLayout: TextInputLayout = findViewById(R.id.passwordLayout)

        var passwordConfirmInput: TextInputEditText = findViewById(R.id.passwordConfirmationInput)
        var passwordConfirmLayout: TextInputLayout = findViewById(R.id.passwordConfirmationLayout)


        val submitSignUp: Button = findViewById(R.id.signupSubmit)
        submitSignUp.setOnClickListener {
            var phone:String = phoneInput.text.toString()
            var fullname:String = fullnameInput.text.toString()
            var password:String = passwordInput.text.toString()
            var confirmation:String = passwordConfirmInput.text.toString()
            validateForm(
                phone,
                fullname,
                password,
                confirmation,
                phoneLayout,
                fullnameLayout,
                passwordLayout,
                passwordConfirmLayout
            )
        }

        phoneInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //do your stuff
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                phoneLayout.boxStrokeColor = ContextCompat.getColor(
                    this@SignUpActivity,
                    R.color.colorIndicatorPrimary
                )
                phoneLayout.helperText = ""
            }

            override fun afterTextChanged(s: Editable?) {
                //do your stuff
            }

        })

        passwordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //do your stuff
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passwordLayout.boxStrokeColor = ContextCompat.getColor(
                    this@SignUpActivity,
                    R.color.colorIndicatorPrimary
                )
                passwordLayout.helperText = ""
            }

            override fun afterTextChanged(s: Editable?) {
                //do your stuff
            }

        })

        passwordConfirmInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //do your stuff
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passwordConfirmLayout.boxStrokeColor = ContextCompat.getColor(
                    this@SignUpActivity,
                    R.color.colorIndicatorPrimary
                )
                passwordConfirmLayout.helperText = ""
            }

            override fun afterTextChanged(s: Editable?) {
                //do your stuff
            }

        })
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

        if (phone == ""){
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
            passwordConfirmLayout.requestFocus()
            passwordConfirmLayout.boxStrokeColor = ContextCompat.getColor(this, R.color.error_color)
            passwordConfirmLayout.helperText = " Konfirmasi Password Tidak Boleh Kosong"
        }else if (password != confirm){
            passwordConfirmLayout.requestFocus()
            passwordConfirmLayout.boxStrokeColor = ContextCompat.getColor(this, R.color.error_color)
            passwordConfirmLayout.helperText = " Konfirmasi Password Tidak Sama"
        }else{
            submitSignup(
                phone,
                fullname,
                password,
                confirm,
                phoneLayout,
                fullnameLayout,
                passwordLayout,
                passwordConfirmLayout
            )
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

        try{
            body.put("nomor_telepon", phone)
            body.put("password", password)
            body.put("konfirmasi_password", confirm)
            body.put("nama_lengkap", fullname)
            ApiService.postDaftar(body)
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        try {
                            if (response?.getString("message")
                                    .equals("Donatur berhasil mendaftar")
                            ) {
                                val data: JSONObject? = response?.getJSONObject("data")
                                val token: String? = data?.getString("token")
                                val dataDonatur: JSONObject? = data?.getJSONObject("donatur")
                                val donaturNama = dataDonatur?.getString("tbldonatur_nama")


                                //save token and donatur id on preferences
                                if (token != null) {
                                    sharedPreference.save("TOKEN", token)
                                    sharedPreference.save("penggunaNAMA", donaturNama )
                                    sharedPreference.save("penggunaTotalDonasi", 0)
                                    startActivity(
                                        Intent(
                                            this@SignUpActivity,
                                            DashboardActivity::class.java
                                        )
                                    )
                                }
                            }
                        } catch (e: JSONException) {

                        }
                    }

                    override fun onError(anError: ANError?) {
                        val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                        if(anError?.errorDetail!!.equals("connectionError")){
                            val toast = Toast.makeText(
                                this@SignUpActivity,
                                "Ada masalah dengan Koneksi Internet Anda",
                                Toast.LENGTH_LONG
                            )
                            toast.show()
                            return
                        }
                        if (apiError?.message == "Nomor telepon telah terdaftar") {
                            phoneLayout.requestFocus()
                            phoneLayout.boxStrokeColor = ContextCompat.getColor(
                                this@SignUpActivity,
                                R.color.error_color
                            )
                            phoneLayout.helperText = "Nomor telepon telah terdaftar"
                        }

                        val jsonObject = JSONObject(anError?.errorBody);
                        val jsonArray = jsonObject.getJSONArray("password");

                        if(jsonArray[0] == "The password must be at least 5 characters."){
                            passwordLayout.requestFocus()
                            passwordLayout.boxStrokeColor = ContextCompat.getColor(this@SignUpActivity, R.color.error_color)
                            passwordLayout.helperText = "Password minimal 5 karakter"
                        }


                        Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                        Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                        Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
                    }

                })
        }catch (e: JSONException){

        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, IntroActivity::class.java))
    }

}

