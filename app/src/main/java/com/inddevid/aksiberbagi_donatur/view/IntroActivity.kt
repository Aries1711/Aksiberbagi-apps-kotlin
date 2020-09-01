package com.inddevid.aksiberbagi_donatur.view

import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.inddevid.aksiberbagi_donatur.R
import kotlinx.android.synthetic.main.intro_activity.*

class IntroActivity : AppCompatActivity() {
    private val introSliderAdapter = IntroSliderAdapter(
        listOf(
            IntroSlide(
                    "#semangatKebaikan",
                "Kita bersama untuk memberikan manfaat kepada saudara kita yang membutuhkan",
                R.drawable.intro1
            ),
            IntroSlide(
                "#kebaikanRutin",
                "Dengan fitur Donasi rutin aksiberbagi menyediakan sarana kebaikan berkesinambungan",
                R.drawable.intro2
            ),IntroSlide(
                "#semangatBerbagi",
                "Dengan berbagi kita dapat meningkatkan kebahagian kita",
                R.drawable.intro3
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