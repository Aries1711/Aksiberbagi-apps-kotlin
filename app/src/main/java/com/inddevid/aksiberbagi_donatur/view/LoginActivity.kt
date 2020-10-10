package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.material.textfield.TextInputEditText
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.services.ApiService
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    lateinit var phoneNumber: TextInputEditText
    lateinit var userPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        supportActionBar?.hide()
        val closeBtnLogin: Button = findViewById(R.id.closeLogin)
        val submitLogin: Button = findViewById(R.id.loginSubmit)
        closeBtnLogin.setOnClickListener{ startActivity(Intent(this@LoginActivity, IntroActivity::class.java)) }

        phoneNumber = findViewById(R.id.phoneNumber)
        userPassword = findViewById(R.id.password)
        submitLogin.setOnClickListener {
            val phone = phoneNumber.text.toString()
            val password = userPassword.text.toString()
            submitLogin(phone,password)
        }

        setDummy()

    }

    private fun setDummy(){
        phoneNumber.setText("085322778935")
        userPassword.setText("anamkun")
    }

    private fun submitLogin(phone:String, password: String){
        val data = JSONObject()
        try {
            data.put("nomor_telepon", phone)
            data.put("password", password)
            ApiService.postMasuk(data)
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        try {
                            Log.d("LOGIN_RESPONSE", response.toString())
                            if (response?.getString("message").equals("Nomor telepon tidak terdaftar")) {
                                val toast = Toast.makeText(applicationContext, "Nomor telepon tidak terdaftar", Toast.LENGTH_LONG)
                                toast.show()
                            }else{
                                val toast = Toast.makeText(applicationContext, "Tidak definisikan", Toast.LENGTH_LONG)
                                toast.show()
                            }
                        }catch (e : JSONException){
                            val toast = Toast.makeText(applicationContext, "Kesalahan Try", Toast.LENGTH_LONG)
                            toast.show()
                        }

                    }
                    override fun onError(anError: ANError?) {
                        val toast = Toast.makeText(applicationContext, "Error koneksi", Toast.LENGTH_LONG)
                        toast.show()
                    }

                })
        }catch(e:JSONException){
            e.message?.let { Log.d("LOGIN", it) }
        }

    }

}