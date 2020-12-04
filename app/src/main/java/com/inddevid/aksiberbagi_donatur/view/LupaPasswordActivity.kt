package com.inddevid.aksiberbagi_donatur.view

import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.material.textfield.TextInputEditText
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.services.ApiError
import com.inddevid.aksiberbagi_donatur.services.ApiService
import com.inddevid.aksiberbagi_donatur.services.Preferences
import org.json.JSONObject


class LupaPasswordActivity : AppCompatActivity() {
    private val TAG = "Lupa Password"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lupa_password_activity)

        val sharedPreference: Preferences = Preferences(this)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")

        val toolbar: Toolbar = findViewById(R.id.upAppbarPasswordForgot)
        toolbar.title = "Lupa Password"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationOnClickListener{
            startActivity(Intent(this@LupaPasswordActivity, LoginActivity::class.java))
        }

        val btnSubmitNotelp: Button = findViewById(R.id.submitForgot)
        val inputNoTelp: TextInputEditText = findViewById(R.id.inputNoTelp)
        btnSubmitNotelp.setOnClickListener {
            val noTelpValue = inputNoTelp.text.toString()
            sharedPreference.save("noPenggunaReset", noTelpValue)
            postLupaPassword(noTelpValue, this )
        }

        val otpTextInput: OtpTextView = findViewById(R.id.otpKode)
        otpTextInput.otpListener = object : OTPListener {
            override fun onInteractionListener() {
                // fired when user types something in the Otpbox
            }

            override fun onOTPComplete(otp: String) {
                // fired when user has entered the OTP fully.
                val noTelp = sharedPreference.getValueString("noPenggunaReset")
                postKodeOTP(noTelp!!, otp, this@LupaPasswordActivity)
            }
        }



    }

    private fun postLupaPassword(
        noTelp : String,
        context : LupaPasswordActivity
    ){
        val body = JSONObject()
        body.put("nomor_telepon", noTelp)
        ApiService.postLupaPassword(body).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                if (response?.getString("message").equals("Berhasil mengirim kode verifikasi ke WhatsApp anda")){
                    val layoutUtama: LinearLayout = context.findViewById(R.id.layoutLupaPassword)
                    val layoutVerifikasi: LinearLayout = context.findViewById(R.id.layoutVerifikasiKode)
                    layoutUtama.visibility = View.GONE
                    layoutVerifikasi.visibility = View.VISIBLE
                }
            }

            override fun onError(anError: ANError?) {
                val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                if(anError?.errorDetail!!.equals("connectionError")){
                    val toast = Toast.makeText(
                        this@LupaPasswordActivity,
                        "Ada masalah dengan Koneksi Internet Anda",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    return
                }
                if(apiError?.message == "Nomor telepon tidak terdaftar"){
                    Toast.makeText(this@LupaPasswordActivity, "Nomor telepon tidak terdaftar", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun postKodeOTP(
        noHp: String,
        noKode : String,
        context : LupaPasswordActivity
    ){
        val body = JSONObject()
        body.put("no_hp", noHp)
        body.put("kode_reset", noKode)
        ApiService.postKodeOTPVerifikasi(body).getAsJSONObject(object : JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                if (response?.getString("message").equals("Sukses mencocokkan kode reset")){
                    startActivity(Intent(this@LupaPasswordActivity, PasswordSetActivity::class.java))
                }else if(response?.getString("message").equals("Gagal mencocokkan kode reset")){
                    Toast.makeText(this@LupaPasswordActivity, "Gagal mencocokkan kode reset", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(anError: ANError?) {
                Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
                if(anError?.errorDetail!!.equals("connectionError")){
                    val toast = Toast.makeText(
                        this@LupaPasswordActivity,
                        "Ada masalah dengan Koneksi Internet Anda",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    return
                }
            }
        })
    }

}

