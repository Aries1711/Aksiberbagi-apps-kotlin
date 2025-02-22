package com.aksiberbagi.donatur.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.services.ApiError
import com.aksiberbagi.donatur.services.ApiService
import com.aksiberbagi.donatur.services.Preferences
import org.json.JSONException
import org.json.JSONObject


class SplashActivity : AppCompatActivity() {
    private val TAG = "SplashActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        //declare the animation
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        val imageLogo: ImageView = findViewById(R.id.splashImage_id);
        imageLogo.startAnimation(fadeIn)

        //code for noActionbar
        supportActionBar?.hide()

        //cek if user have token or not
        val sharedPreference: Preferences = Preferences(this)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")

        var keyFirebase: String? = "nothing"

        if (intent.hasExtra("keyFirebase")) {
            keyFirebase = intent.getStringExtra("keyFirebase")!!.toString()
        }

        if (retrivedToken != null) {
            refreshToken(retrivedToken, keyFirebase!!)
        } else {
            //redirect to intro page
            Looper.myLooper()?.let {
                Handler(it).postDelayed({
                    val intent = Intent(this@SplashActivity, IntroActivity::class.java)
                    startActivity(intent)
                }, 2500)
            }
        }

    }

    private fun refreshToken(tokenValue: String, keyRedirect: String) {
        val header: String = tokenValue
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
                                //kondisi untuk redirect page
                                if (keyRedirect == "donasi rutin true") {
                                    //redirect to Beranda page
                                    Looper.myLooper()?.let {
                                        Handler(it).postDelayed({
                                            val intent = Intent(this@SplashActivity, DonasiRutinActivity::class.java)
                                            val mBundle = Bundle()
                                            mBundle.putString("keyFirebase", "donasi rutin true" )
                                            intent.putExtras(mBundle)
                                            startActivity(intent)
                                        }, 2500)
                                    }
                                } else {
                                    //redirect to Beranda page
                                    Looper.myLooper()?.let {
                                        Handler(it).postDelayed({
                                            val intent = Intent(
                                                this@SplashActivity,
                                                DashboardActivity::class.java
                                            )
                                            startActivity(intent)
                                        }, 2500)
                                    }
                                }
                            }
                        } else if (response?.getString("message")
                                .equals("Token expired berhasil di refresh")
                        ) {
                            val token: String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                //kondisi untuk redirect page
                                if (keyRedirect == "donasi rutin true") {
                                    //redirect to Beranda page
                                    Looper.myLooper()?.let {
                                        Handler(it).postDelayed({
                                            val intent = Intent(this@SplashActivity, DonasiRutinActivity::class.java)
                                            val mBundle = Bundle()
                                            mBundle.putString("keyFirebase", "donasi rutin true" )
                                            intent.putExtras(mBundle)
                                            startActivity(intent)
                                        }, 2500)
                                    }
                                } else {
                                    //redirect to Beranda page
                                    Looper.myLooper()?.let {
                                        Handler(it).postDelayed({
                                            val intent = Intent(
                                                this@SplashActivity,
                                                DashboardActivity::class.java
                                            )
                                            startActivity(intent)
                                        }, 2500)
                                    }
                                }
                            }
                        } else {
                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                    val intent =
                                        Intent(this@SplashActivity, IntroActivity::class.java)
                                    startActivity(intent)
                                }, 2500)
                            }
                        }

                    } catch (e: JSONException) {
                        val toast = Toast.makeText(
                            applicationContext,
                            "Invalid Json",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                }

                override fun onError(anError: ANError?) {

                    val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                    if (anError?.errorDetail!!.equals("connectionError")) {
                        val toast = Toast.makeText(
                            this@SplashActivity,
                            "Ada masalah dengan Koneksi Internet Anda",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                        return
                    } else {
                        Looper.myLooper()?.let {
                            Handler(it).postDelayed({
                                val intent = Intent(this@SplashActivity, IntroActivity::class.java)
                                startActivity(intent)
                            }, 2500)
                        }
                    }
                    Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                    Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                    Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
                }

            })
        } catch (e: JSONException) {
            val toast = Toast.makeText(
                applicationContext,
                "Kesalahan Header",
                Toast.LENGTH_LONG
            )
            toast.show()
        }
    }
}

