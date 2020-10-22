package com.inddevid.aksiberbagi_donatur.view

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
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.services.ApiService
import com.inddevid.aksiberbagi_donatur.services.Preferences
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


        Log.d(TAG, "OnCatchTokenPreference $retrivedToken")

        if (retrivedToken != null){
            val toast = Toast.makeText(
                applicationContext,
                "Token tidak Kosong $retrivedToken",
                Toast.LENGTH_LONG
            )
            toast.show()
            refreshToken(retrivedToken)
        }else{
            //redirect to intro page
            Looper.myLooper()?.let {
                Handler(it).postDelayed({
                    val intent = Intent(this@SplashActivity, IntroActivity::class.java)
                    startActivity(intent)
                }, 2500)
            }

        }

    }

    private fun refreshToken(tokenValue: String){
        val header : String = tokenValue
        val sharedPreference: Preferences = Preferences(this)
        try {
            ApiService.postRefreshToken(header).getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        if (response?.getString("message").equals("Refresh berhasil")){
                            val token : String? = response?.getString("token")
                            Log.d(TAG, "OnErrorCode $token")
                            //save token
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                //redirect to Beranda page
                                Looper.myLooper()?.let {
                                    Handler(it).postDelayed({
                                        val intent = Intent(this@SplashActivity, DashboardActivity::class.java)
                                        startActivity(intent)
                                    }, 2500)
                                }
                            }else{
                                Looper.myLooper()?.let {
                                    Handler(it).postDelayed({
                                        val intent = Intent(this@SplashActivity, IntroActivity::class.java)
                                        startActivity(intent)
                                    }, 2500)
                                }
                            }
                        }else if(response?.getString("message").equals("Token expired.")){
                            val toast = Toast.makeText(
                                applicationContext,
                                "Token Expired",
                                Toast.LENGTH_LONG
                            )
                            toast.show()
                        }else{
                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                    val intent = Intent(this@SplashActivity, IntroActivity::class.java)
                                    startActivity(intent)
                                }, 2500)
                            }
                        }

                    }catch (e : JSONException){
                        val toast = Toast.makeText(
                            applicationContext,
                            "Invalid Json",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                }
                override fun onError(anError: ANError?) {
                    val toast = Toast.makeText(
                        applicationContext,
                        "Koneksi Error",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                    Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                    Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
                }

            })
        }catch (e: JSONException){
            val toast = Toast.makeText(
                applicationContext,
                "Kesalahan Header",
                Toast.LENGTH_LONG
            )
            toast.show()
        }
    }
}

