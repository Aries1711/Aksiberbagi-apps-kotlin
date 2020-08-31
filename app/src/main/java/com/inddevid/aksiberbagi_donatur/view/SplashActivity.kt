package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.inddevid.aksiberbagi_donatur.R


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        //declare the animation
        val fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        val imageLogo: ImageView = findViewById(R.id.splashImage_id);
        imageLogo.startAnimation(fade_in)

        //code for noActionbar
        supportActionBar?.hide()
        //redirect to intro page
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                val intent = Intent (this@SplashActivity, IntroActivity::class.java)
                startActivity(intent)
            }, 2500)
        }
    }
}

