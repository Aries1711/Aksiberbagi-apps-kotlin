package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.material.textfield.TextInputEditText
import com.inddevid.aksiberbagi_donatur.R
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        supportActionBar?.hide()
        val closeBtnLogin: Button = findViewById(R.id.closeLogin)
        val submitLogin: Button = findViewById(R.id.loginSubmit)
        closeBtnLogin.setOnClickListener{ startActivity(Intent(this@LoginActivity, IntroActivity::class.java)) }


        val phoneNumber: TextInputEditText = findViewById(R.id.phoneNumber)
        val userPassword: TextInputEditText = findViewById(R.id.password)
        submitLogin.setOnClickListener {
            var phone = phoneNumber.text.toString()
            var password = userPassword.text.toString()
            submitLogin(phone,password)
        }

    }

    private fun submitLogin(phone:String, password: String){
        AndroidNetworking.post("http://192.168.18.3/aksiberbagi-api/public/v1/autentikasi/masuk")
            .addBodyParameter("nomor_telepon", phone)
            .addBodyParameter("password", password)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                 try {
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
    }

}