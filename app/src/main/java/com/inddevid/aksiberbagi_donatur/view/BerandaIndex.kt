package com.inddevid.aksiberbagi_donatur.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.BerandaSlideBanner
import com.inddevid.aksiberbagi_donatur.presenter.BerandaSlideAdapter
import kotlinx.android.synthetic.main.fragment_beranda_index.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BerandaIndex.newInstance] factory method to
 * create an instance of this fragment.
 */
class BerandaIndex : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var berandaSlideAdapter = BerandaSlideAdapter(
        listOf(
            BerandaSlideBanner(
                R.drawable.banner_a
            ),
            BerandaSlideBanner(
                R.drawable.banner_a
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_beranda_index , container, false)

        view.berandaSliderBanner.adapter = berandaSlideAdapter
        setupIndicators( view )
        setCurrentIndicator(0 , view)

        view.berandaSliderBanner.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position, view)
            }
        })

        val imageDonasiRutin : ImageView = view.findViewById(R.id.donasiRutinLogo)
        Glide.with(requireActivity()).load(R.drawable.donasi_rutin_icon).into(imageDonasiRutin)

        val btnGbgDonRut: CardView = view.findViewById(R.id.cardGabungDonasiRutin)
        btnGbgDonRut.setOnClickListener(View.OnClickListener {
            Toast.makeText(requireActivity(), "Gabung Donasi Rutin ?", Toast.LENGTH_LONG).show()
        })

        return view
    }

    private fun setupIndicators( view: View){
        val indicators = arrayOfNulls<ImageView>(berandaSlideAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8, 0, 8, 0)
        for(i in indicators.indices)
        {
            indicators[i] = ImageView(activity?.applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.indicator_inactive
                    )
//                    activity?.applicationContext?.let {
//                        ContextCompat.getDrawable(
//                            it,
//                            R.drawable.indicator_inactive
//                        )
//                    }
                )
                this?.layoutParams = layoutParams
            }
            view.indicatorSliderBanner.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int, view: View){
        val childCount = view.indicatorSliderBanner.childCount
        for (i in 0 until childCount)
        {
            val imageView = view.indicatorSliderBanner[i] as ImageView
            if (i == index)
            {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.indicator_active
                    )
                )
            }else{
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BerandaIndex.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BerandaIndex().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}