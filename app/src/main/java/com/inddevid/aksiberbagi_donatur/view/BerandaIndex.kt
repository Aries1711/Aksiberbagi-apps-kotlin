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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.BerandaLaporan
import com.inddevid.aksiberbagi_donatur.model.BerandaProgramAll
import com.inddevid.aksiberbagi_donatur.model.BerandaProgramPilihan
import com.inddevid.aksiberbagi_donatur.model.BerandaSlideBanner
import com.inddevid.aksiberbagi_donatur.presenter.BerandaLaporanAdapter
import com.inddevid.aksiberbagi_donatur.presenter.BerandaProgramAllAdapter
import com.inddevid.aksiberbagi_donatur.presenter.BerandaProgramPilihanAdapter
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
                R.drawable.banner_d
            ),
            BerandaSlideBanner(
                R.drawable.banner_g
            ),
            BerandaSlideBanner(
                R.drawable.banner_h
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
        //deklarasi variabel sementara untuk array program pilihan (Recycler view)
        val imageUrl:String = "https://aksiberbagi.com/storage/program/Raih%20Keutamaan%20Bulan%20Muharram;%20Perbanyak%20Amal%20Shalih-banner.jpg"
        val titleCardPilihan:String = "Sedekah Air untuk Pesantren Pelosok dan ..."
        val donasiFund:String = "100.789"
        val donasiDayFund:String = "37 hari lagi"
        val arrayPilihan = ArrayList<BerandaProgramPilihan>()
        arrayPilihan.add(BerandaProgramPilihan(imageUrl,titleCardPilihan,donasiFund,donasiDayFund))
        arrayPilihan.add(BerandaProgramPilihan(imageUrl,titleCardPilihan,donasiFund,donasiDayFund))
        arrayPilihan.add(BerandaProgramPilihan(imageUrl,titleCardPilihan,donasiFund,donasiDayFund))
        val myAdapterPilihan = BerandaProgramPilihanAdapter(arrayPilihan, requireActivity())

        //deklarasi variabel sementara untuk array laporan (Recycler view)
        val imageReportUrl:String = "https://aksiberbagi.com/storage/program/berita/851c06263eaaeea0a3dd445cc823874c_WhatsApp%20Image%202020-06-20%20at%2011.49.08.jpeg"
        val titleCardReport:String = "Sedekah air untuk pesantren dan .."
        val summariCardReport:String = "Telah Disalurkan Air ke Ponpes As-syifa"
        val locationReport:String = "Situbondo, Jawa Timur."
        val dateReport:String = "27 Sep"
        val arrayLaporan = ArrayList<BerandaLaporan>()
        arrayLaporan.add(BerandaLaporan(imageReportUrl,titleCardReport,locationReport,dateReport))
        arrayLaporan.add(BerandaLaporan(imageReportUrl,titleCardReport,locationReport,dateReport))
        arrayLaporan.add(BerandaLaporan(imageReportUrl,titleCardReport,locationReport,dateReport))
        val myAdapterLaporan = BerandaLaporanAdapter(arrayLaporan,requireActivity())

        //deklarasi variabel sementara untuk array ProgramAll (Recycler view)
        val imageProgram:String = "https://aksiberbagi.com/storage/program/Sedekah%20Terbaik%20untuk%20Anak%20Yatim-banner.jpg"
        val titleAll:String = "Sedekah Terbaik Untuk Anak Yatim"
        val titleSummary:String = "Sesurga bersama Rasulullah"
        val volunteer:String = "Aksiberbagi.com"
        val fundAll:String = "765.987.079"
        val dayAll:String = "37 hari lagi"
        val arrayProgramAll = ArrayList<BerandaProgramAll>()
        arrayProgramAll.add(BerandaProgramAll(imageProgram, titleAll,titleSummary,volunteer,fundAll/**,dayAll*/))
        arrayProgramAll.add(BerandaProgramAll(imageProgram, titleAll,titleSummary,volunteer,fundAll/**,dayAll*/))
        arrayProgramAll.add(BerandaProgramAll(imageProgram, titleAll,titleSummary,volunteer,fundAll/**,dayAll*/))
        val myAdapterAll = BerandaProgramAllAdapter(arrayProgramAll,requireActivity())


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

        //        tombol cari
        val btnGbgDonRut: CardView = view.findViewById(R.id.searchBantu)
        btnGbgDonRut.setOnClickListener(View.OnClickListener {
            Toast.makeText(requireActivity(), "Mau Cari Sesuatu mang ?", Toast.LENGTH_LONG).show()
        })

        //        tombol submenu beranda a-d
        val imageSubA : ImageView = view.findViewById(R.id.subA)
        Glide.with(requireActivity()).load(R.drawable.submenuc).into(imageSubA)
        val imageSubB : ImageView = view.findViewById(R.id.subB)
        Glide.with(requireActivity()).load(R.drawable.submenub).into(imageSubB)
        val imageSubC : ImageView = view.findViewById(R.id.subC)
        Glide.with(requireActivity()).load(R.drawable.submenud).into(imageSubC)
        val imageSubD : ImageView = view.findViewById(R.id.subD)
        Glide.with(requireActivity()).load(R.drawable.submenue).into(imageSubD)

    //inflate view LelangBaik
        view.normalCountDownView.timerTextBackgroundTintColor = ContextCompat.getColor(requireActivity(), R.color.colorInputStrokeBlue)
        view.normalCountDownView.initTimer(3600)
        view.normalCountDownView.startTimer()

    //inflate horizontal program pilihan
        var mainMenuPilihan = view.findViewById(R.id.recyclerProgramPilihan) as RecyclerView
        mainMenuPilihan.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
        mainMenuPilihan.adapter = myAdapterPilihan

     //inflate horizontal laporan
        var mainMenuLaporan = view.findViewById(R.id.recyclerLaporan) as RecyclerView
        mainMenuLaporan.layoutManager = LinearLayoutManager(requireActivity(),RecyclerView.HORIZONTAL,false)
        mainMenuLaporan.adapter = myAdapterLaporan

     //inflate vertical program All
        var mainMenuAll = view.findViewById(R.id.recyclerProgramAll) as RecyclerView
        mainMenuAll.layoutManager = LinearLayoutManager(requireActivity())
        mainMenuAll.adapter =myAdapterAll

    //set banner for footer beranda
        var imgBannerUrl: String = "https://aksiberbagi.com/storage/program/Jumat%20Berkah%20Bersedekah%20Jariyah%20Atas%20Nama%20Keluarga-banner.jpeg"
        val imageBanner :ImageView = view.findViewById(R.id.berandaBanner)
        Glide.with(requireActivity()).load(imgBannerUrl).into(imageBanner)

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