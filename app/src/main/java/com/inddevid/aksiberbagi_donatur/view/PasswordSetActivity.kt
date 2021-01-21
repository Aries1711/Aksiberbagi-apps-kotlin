package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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

class PasswordSetActivity : AppCompatActivity() {
    private val TAG = "Password Set"
    private var boolKode = "False"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.password_set_activity)

        val sharedPreference: Preferences = Preferences(this)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")
        var kodeReset: String? = intent.getStringExtra("KODERESET")
        if(kodeReset != null || kodeReset != "" ){
            boolKode = "True"
        }

        val toolbar: Toolbar = findViewById(R.id.upAppbarPasswordSet)
        toolbar.title = "Reset Password"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationOnClickListener{
            Log.d(TAG, "retrivedToken $retrivedToken")
            if (retrivedToken == "" ||retrivedToken == null ){
                startActivity(Intent(this@PasswordSetActivity, IntroActivity::class.java))
            }else{
                val mIntent = Intent(this, DashboardActivity::class.java)
                val mBundle = Bundle()
                mBundle.putString("penggunaAktif", "true")
                mIntent.putExtras(mBundle)
                startActivity(mIntent)
            }
        }


        val submitPassword: Button = findViewById(R.id.passwordSubmit)
        submitPassword.setOnClickListener {
            Log.d(TAG, "button trigerred")
            submitPassword.isEnabled = false
            validatePassword(retrivedToken,this, kodeReset)
        }


    }

    private fun validatePassword(tokenValue: String?, context: PasswordSetActivity, kodeReset : String?){
        //deklarasi value password
        val layoutPasswordInputA : TextInputLayout = context.findViewById(R.id.passwordConfirmation1)
        val inputPasswordA: TextInputEditText = context.findViewById(R.id.passwordA)
        val layoutPasswordInputB : TextInputLayout = context.findViewById(R.id.passwordConfirmation2)
        val inputPasswordB: TextInputEditText = context.findViewById(R.id.passwordB)

        val passwordValueA = inputPasswordA.text.toString()
        val passwordValueB = inputPasswordB.text.toString()


        if(passwordValueA != "" && passwordValueB != ""){
            val countChar = passwordValueA.length
            if (countChar > 5){
                if(passwordValueA == passwordValueB){
                    if(kodeReset != null || kodeReset != ""){
                        postResetPassword(context)
                    }
                    if (tokenValue == "" || tokenValue == null || kodeReset == null || kodeReset == "" ){
                        postResetPassword(context)
                    }else{
                        getKoneksi(tokenValue, context)
                    }
                }else{
                    layoutPasswordInputB.boxStrokeColor = ContextCompat.getColor(this, R.color.error_color)
                    layoutPasswordInputB.helperText = "Harap masukkan konfirmasi password yang benar"
                }
            }else{
                layoutPasswordInputA.boxStrokeColor = ContextCompat.getColor(this, R.color.error_color)
                layoutPasswordInputA.helperText = "Password minimal terdiri dari 6 karakter"
            }
        }else{
            layoutPasswordInputA.boxStrokeColor = ContextCompat.getColor(this, R.color.error_color)
            layoutPasswordInputA.helperText = "Password baru tidak boleh kosong"
            layoutPasswordInputB.boxStrokeColor = ContextCompat.getColor(this, R.color.error_color)
            layoutPasswordInputB.helperText = "Password konfirmasi tidak boleh kosong"
        }
    }

    private fun getKoneksi(tokenValue: String?, context: PasswordSetActivity){
        ApiService.getKoneksi(tokenValue).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                postResetPassword(context)
            }

            override fun onError(anError: ANError?) {
                val submitPassword: Button = context.findViewById(R.id.passwordSubmit)
                submitPassword.isEnabled = true
                val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                if (apiError?.message == "Token expired"){
                    refreshToken(tokenValue, context)
                    val toast = Toast.makeText(this@PasswordSetActivity,"Cobalah beberapa saat lagi",
                        Toast.LENGTH_LONG)
                    toast.show()
                }
            }

        })
    }

    private fun refreshToken(
        tokenValue: String?,
        context: PasswordSetActivity
    ){
        val header : String? = tokenValue
        val sharedPreference: Preferences = Preferences(this)
        try {
            ApiService.postRefreshToken(header).getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        if (response?.getString("message").equals("Refresh berhasil")) {
                            val token: String? = response?.getString("token")
                            //save token
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(tokenValue, context)
                            }
                        } else if (response?.getString("message")
                                .equals("Token expired berhasil di refresh")
                        ) {
                            val token: String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(tokenValue, context)
                            }
                        } else {
                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                    val intent = Intent(
                                        this@PasswordSetActivity,
                                        IntroActivity::class.java
                                    )
                                    startActivity(intent)
                                }, 2500)
                            }
                        }

                    } catch (e: JSONException) {
                        val toast = Toast.makeText(
                            this@PasswordSetActivity,
                            "Invalid Json",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                }

                override fun onError(anError: ANError?) {
                    Looper.myLooper()?.let {
                        Handler(it).postDelayed({
                            val intent = Intent(
                                this@PasswordSetActivity,
                                IntroActivity::class.java
                            )
                            startActivity(intent)
                        }, 2500)
                    }
                    Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                    Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                    Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
                }

            })
        }catch (e: JSONException){
            val toast = Toast.makeText(
                this,
                "Kesalahan Header",
                Toast.LENGTH_LONG
            )
            toast.show()
        }
    }

    private fun postResetPassword(context: PasswordSetActivity){
        val body = JSONObject()
        val sharedPreference: Preferences = Preferences(this)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")
        val penggunaWA: String? = sharedPreference.getValueString("penggunaWA")
        val penggunaNoTelp: String? = sharedPreference.getValueString("noPenggunaReset")

        //deklarasi value password from view
        val inputPasswordA: TextInputEditText = context.findViewById(R.id.passwordA)
        val inputPasswordB: TextInputEditText = context.findViewById(R.id.passwordB)
        if(boolKode == "False" ){
            if (retrivedToken == "" || retrivedToken == null){
                body.put("nomor_telepon", penggunaNoTelp)
            }else{
                body.put("nomor_telepon", penggunaWA)
            }
        }else{
            body.put("nomor_telepon", penggunaNoTelp)
            sharedPreference.save("TOKEN", "")
        }


        body.put("password", inputPasswordA.text.toString())
        body.put("konfirmasi_password", inputPasswordB.text.toString())
        ApiService.postPasswordReset(body).getAsJSONObject(object : JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                if(response?.getString("message").equals("Password berhasil direset")){
                    if (retrivedToken == "" || retrivedToken == null){
                        val toast = Toast.makeText(
                            this@PasswordSetActivity,
                            "Password berhasil di reset",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                        Looper.myLooper()?.let {
                            Handler(it).postDelayed({
                                val intent = Intent(
                                    this@PasswordSetActivity,
                                    LoginActivity::class.java
                                )
                                startActivity(intent)
                            }, 3000)
                        }
                    }else{
                        val toast = Toast.makeText(
                            this@PasswordSetActivity,
                            "Password berhasil di reset",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                        Looper.myLooper()?.let {
                            Handler(it).postDelayed({
                                val intent = Intent(
                                    this@PasswordSetActivity,
                                    PengaturanActivity::class.java
                                )
                                startActivity(intent)
                            }, 3000)
                        }
                    }
                }
            }

            override fun onError(anError: ANError?) {
                Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
            }

        })
    }

    override fun onBackPressed() {
        val sharedPreference: Preferences = Preferences(this)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")
        if (retrivedToken == "" ||retrivedToken == null ){
            startActivity(Intent(this@PasswordSetActivity, IntroActivity::class.java))
        }else{
//            val mIntent = Intent(this, DashboardActivity::class.java)
//            val mBundle = Bundle()
//            mBundle.putString("penggunaAktif", "true")
//            mIntent.putExtras(mBundle)
//            startActivity(mIntent)
            startActivity(Intent(this@PasswordSetActivity, IntroActivity::class.java))
        }
    }

}