package com.aksiberbagi.donatur.view

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.model.IntroSlide
import com.aksiberbagi.donatur.presenter.IntroSliderAdapter
import kotlinx.android.synthetic.main.intro_activity.*

class IntroActivity : AppCompatActivity() {
    private var countBack = 0
    private val introSliderAdapter = IntroSliderAdapter(
        listOf(
            IntroSlide(
                "#SemangatBerbagi",
                "Kita bersama memberikan manfaat kepada saudara kita yang membutuhkan",
                R.drawable.intro_a
            ),IntroSlide(
                "#SemangatIndonesia",
                "Awali pagimu dengan berbagi kebaikan kepada sesama",
                R.drawable.intro_b
            ),IntroSlide(
                "#KolaborasiKebaikan",
                "Donasi mudah mulai dari 1.000 rupiah",
                R.drawable.intro_c
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro_activity)
        //code for noActionBar
        supportActionBar?.hide()

        introSliderViewPager.adapter = introSliderAdapter
        setupIndicators()
        setCurrentIndicator(0)
        introSliderViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        val loginBtn: Button = findViewById(R.id.login_btn)
        val signupBtn: Button = findViewById(R.id.signup_btn)
        loginBtn.setOnClickListener{ startActivity(Intent(this@IntroActivity, LoginActivity::class.java))}
        signupBtn.setOnClickListener{ startActivity(Intent(this@IntroActivity, SignUpActivity::class.java))}
    }

    override fun onBackPressed() {
        countBack++
        if (countBack <= 1) {
            val toast = Toast.makeText(
                applicationContext,
                "tekan dua kali untuk keluar",
                Toast.LENGTH_LONG
            )
            toast.show()
        } else if (countBack > 1) {
            super.onBackPressed()
            finishAffinity()
            finish()
        }
    }

    private fun setupIndicators(){
        val indicators = arrayOfNulls<ImageView>(introSliderAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for(i in indicators.indices)
        {
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            indicatorContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int){
        val childCount = indicatorContainer.childCount
        for (i in 0 until childCount)
        {
            val imageView = indicatorContainer[i] as ImageView
            if (i == index)
            {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                    R.drawable.indicator_active
                    )
                )
            }else{
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }

}