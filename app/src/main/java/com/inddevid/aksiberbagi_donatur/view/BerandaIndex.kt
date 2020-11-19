package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.*
import com.inddevid.aksiberbagi_donatur.presenter.*
import com.inddevid.aksiberbagi_donatur.services.ApiService
import com.inddevid.aksiberbagi_donatur.services.Preferences
import kotlinx.android.synthetic.main.fragment_beranda_index.view.*
import org.json.JSONException
import org.json.JSONObject

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
    private val TAG = "Fragment Beranda"
    private val arrayLelang = ArrayList<BerandaLelang>()
    private val arrayRekomendasi = ArrayList<BerandaProgramPilihan>()
    private val arrayLaporan = ArrayList<BerandaLaporan>()
    private val arrayProgram = ArrayList<BerandaProgramAll>()
    private var cekKoneksi : String = "testing"
    private var backButtonCount = 0
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
        //retrieved token from preseference
        val sharedPreference: Preferences = Preferences(requireContext())
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_beranda_index, container, false)

        view.berandaSliderBanner.adapter = berandaSlideAdapter
        setupIndicators(view)
        setCurrentIndicator(0, view)

        view.berandaSliderBanner.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position, view)
            }
        })

        // tombol cari appbar
        val searcBtn: ImageView = view.findViewById(R.id.searchImageButton)
        searcBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireActivity(), SearchActivity::class.java))
        })

        //tombol gabung donasi rutin
        val btnGbgDonRut: CardView = view.findViewById(R.id.searchBantu)
        btnGbgDonRut.setOnClickListener{ startActivity(
            Intent(
                requireActivity(),
                DonasiRutinActivity::class.java
            )
        ) }

        // tombol submenu beranda a-d
        // tombol lelang baik
        val imageSubA : ImageView = view.findViewById(R.id.subA)
        Glide.with(requireActivity()).load(R.drawable.submenuc).into(imageSubA)
        imageSubA.setOnClickListener {
            startActivity(Intent(requireActivity(), LelangActivity::class.java))
            }
        // tombol zakat
        val imageSubB : ImageView = view.findViewById(R.id.subB)
        Glide.with(requireActivity()).load(R.drawable.submenub).into(imageSubB)
        imageSubB.setOnClickListener {
            startActivity(Intent(requireActivity(), ZakatActivity::class.java))
        }
        // tombol publik ajukan
        val imageSubC : ImageView = view.findViewById(R.id.subC)
        Glide.with(requireActivity()).load(R.drawable.submenud).into(imageSubC)
        imageSubC.setOnClickListener {
            startActivity(Intent(requireActivity(), PublikAjukanActivity::class.java))
        }
        // tombol sapa kami
        val imageSubD : ImageView = view.findViewById(R.id.subD)
        Glide.with(requireActivity()).load(R.drawable.submenue).into(imageSubD)
        imageSubD.setOnClickListener {
            startActivity(Intent(requireActivity(), SapaActivity::class.java))
        }

        getKoneksi(retrivedToken, view)

        //set button lihat semua
        val btnLihatAllProgram : FrameLayout = view.findViewById(R.id.frameButtonLihatSemua)
        btnLihatAllProgram.setOnClickListener{ startActivity(
            Intent(
                requireActivity(),
                ProgramDetailActivity::class.java
            )
        )}
        //set banner for footer beranda
        var imgBannerUrl: String = "https://aksiberbagi.com/storage/program/Jumat%20Berkah%20Bersedekah%20Jariyah%20Atas%20Nama%20Keluarga-banner.jpeg"
        return view
    }

    private fun setupIndicators(view: View){
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

    private fun getKoneksi(tokenValue: String?, view: View){
        val header : String? = tokenValue
        ApiService.getKoneksi(header).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                //inflate view card Lelang baik
                var mainMenuLelang = view.findViewById(R.id.recyclerLelangBaik) as RecyclerView
                getLelangBaik(tokenValue, mainMenuLelang, view)

                //inflate horizontal program Rekomendasi
                var mainMenuPilihan = view.findViewById(R.id.recyclerProgramPilihan) as RecyclerView
                getProgramRekomendasi(tokenValue, mainMenuPilihan)

                //inflate horizontal laporan
                var mainMenuLaporan = view.findViewById(R.id.recyclerLaporan) as RecyclerView
                getLaporanTerbaru(tokenValue, mainMenuLaporan)
                //button lihat semua laporan
                val btnLihatLaporan: FrameLayout = view.findViewById(R.id.frameButtonSemuaLaporan)
                btnLihatLaporan.setOnClickListener {
                    startActivity(
                        Intent(
                            requireActivity(),
                            SemuaLaporanActivity::class.java
                        )
                    )
                }

                //inflate vertical program All
                var mainMenuAll = view.findViewById(R.id.recyclerProgramAll) as RecyclerView
                getListProgram(tokenValue, mainMenuAll)

                val imageBanner: ImageView = view.findViewById(R.id.berandaBanner)
                val titleBanner: TextView = view.findViewById(R.id.bannerBerandaTitle)
                getBannerFooter(tokenValue, imageBanner, titleBanner)

            }

            override fun onError(anError: ANError?) {
                refreshToken(tokenValue, view)
            }

        })
    }

    private fun getLelangBaik(tokenValue: String?, view: RecyclerView, viewT: View){
        val header : String? = tokenValue
        ApiService.getLelang(header).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                val jsonArray = response?.getJSONArray("data")
                val timer = response?.getString("timer")!!.toInt()
                if (jsonArray?.length()!! > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val nominalItem = item?.getJSONObject("nominal")
                        val img: String? = item?.getString("gambar_url")
                        val nominal: Double? =
                            nominalItem?.getString("nominal_flash_sale")!!.toDouble()
                        val stok: String? = item?.getString("stok")
                        arrayLelang.add(BerandaLelang(img, stok?.toInt(), nominal))
                    }
                    val myAdapterLelang = BerandaLelangAdapter(arrayLelang, requireActivity())
                    view.layoutManager = LinearLayoutManager(
                        requireActivity(),
                        RecyclerView.HORIZONTAL,
                        false
                    )
                    view.adapter = myAdapterLelang
                    //
                    viewT.normalCountDownView.timerTextBackgroundTintColor = ContextCompat.getColor(
                        requireActivity(),
                        R.color.colorInputStrokeBlue
                    )
                    viewT.normalCountDownView.initTimer(timer)
                    viewT.normalCountDownView.startTimer()
                }
            }

            override fun onError(anError: ANError?) {

            }

        })
    }

    private fun getProgramRekomendasi(tokenValue: String?, view: RecyclerView){
        val header : String? = tokenValue
        ApiService.getRekomendasi(header).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                val jsonArray = response?.getJSONArray("data")
                if (jsonArray?.length()!! > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val program = item?.getJSONObject("program")
                        val id: String? = program?.getString("tblprogram_id")
                        val img: String? = program?.getString("thumbnail_url")
                        val judul: String? = program?.getString("tblprogram_judul")
                        val donasi: String? = program?.getString("capaian_donasi")
                        val sisahari: String? = program?.getString("sisa_hari")
                        val tanggalMulai: String? = program?.getString("tanggal_mulai_donasi")
                        val targetNominal: String? =
                            program?.getString("tblprogram_isiantargetnominal")
                        var progressProgram: Int? = program?.getInt("progress")
                        var targetDonasi: String?
                        if (targetNominal == "null" || targetNominal == "0") {
                            targetDonasi = "100"
                        } else {
                            targetDonasi = program?.getString("target_nominal")
                        }
                        arrayRekomendasi.add(
                            BerandaProgramPilihan(
                                id,
                                img,
                                judul,
                                donasi,
                                sisahari,
                                tanggalMulai,
                                progressProgram,
                                targetDonasi
                            )
                        )
                    }
                    val myAdapterPilihan = BerandaProgramPilihanAdapter(
                        arrayRekomendasi,
                        requireActivity()
                    )
                    view.layoutManager = LinearLayoutManager(
                        requireActivity(),
                        RecyclerView.HORIZONTAL,
                        false
                    )
                    view.adapter = myAdapterPilihan
                }

            }

            override fun onError(anError: ANError?) {
                Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
            }

        })
    }

    private fun getLaporanTerbaru(tokenValue: String?, view: RecyclerView){
        val header: String? = tokenValue
        ApiService.getLaporan(header).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                val jsonArray = response?.getJSONArray("data")
                if (jsonArray?.length()!! > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val img: String? =
                            "https://aksiberbagi.com/storage/program/berita/" + item?.getString(
                                "tblupdateprogram_file"
                            )
                        val judul: String? = item?.getString("tblupdateprogram_judul")
                        val relawan: String? = item?.getString("tblcabang_nama")
                        val date: String? = item?.getString("tanggal_laporan")
                        arrayLaporan.add(BerandaLaporan(img, judul, relawan, date))
                        val myAdapterLaporan = BerandaLaporanAdapter(
                            arrayLaporan,
                            requireActivity()
                        )
                        view.layoutManager = LinearLayoutManager(
                            requireActivity(),
                            RecyclerView.HORIZONTAL,
                            false
                        )
                        view.adapter = myAdapterLaporan
                    }
                }
            }

            override fun onError(anError: ANError?) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun getListProgram(tokenValue: String?, view: RecyclerView){
        val header:String? = tokenValue
        ApiService.getProgramTerbaru(header).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                val jsonArray = response?.getJSONArray("data")
                if (jsonArray?.length()!! > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val idProgram = item?.getString("tblprogram_id")
                        val img = item?.getString("thumbnail_url")
                        val judul = item?.getString("tblprogram_judul")
                        val volunter: String? = "Aksiberbagi.com"
                        val capaian = item?.getString("capaian_donasi")
                        val sisaHari = item?.getString("sisa_hari")
                        val startProgram = item?.getString("tanggal_mulai_donasi")
                        val targetNominal: String? =
                            item?.getString("tblprogram_isiantargetnominal")
                        var progressProgram: Int? = item?.getInt("progress")
                        var targetDonasi: String?
                        if (targetNominal == "null" || targetNominal == "0") {
                            targetDonasi = "100"
                        } else {
                            targetDonasi = item?.getString("target_nominal")
                        }
                        arrayProgram.add(
                            BerandaProgramAll(
                                idProgram,
                                img,
                                judul,
                                volunter,
                                capaian,
                                sisaHari,
                                startProgram,
                                progressProgram,
                                targetDonasi
                            )
                        )
                        val myAdapterAll = BerandaProgramAllAdapter(arrayProgram, requireActivity())
                        view.layoutManager = LinearLayoutManager(requireActivity())
                        view.adapter = myAdapterAll
                    }
                }
            }

            override fun onError(anError: ANError?) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getBannerFooter(tokenValue: String?, image: ImageView, title: TextView){
        val header: String? = tokenValue
        ApiService.getBanner(header).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                if (response?.getString("message").equals("Data banner berhasil diambil")) {
                    var imgUrl: String? = response?.getString("data")
                    Glide.with(requireActivity()).load(imgUrl).into(image)
                    title.text = response?.getString("title")
                }
            }

            override fun onError(anError: ANError?) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun refreshToken(tokenValue: String?, view: View){
        val header : String? = tokenValue
        val sharedPreference: Preferences = Preferences(requireContext())
        try {
            ApiService.postRefreshToken(header).getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        if (response?.getString("message").equals("Refresh berhasil")) {
                            val token: String? = response?.getString("token")
                            //save token
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(token, view)
                            }
                        } else if (response?.getString("message")
                                .equals("Token expired berhasil di refresh")
                        ) {
                            val token: String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(token, view)
                            }
                        } else {
                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                    val intent = Intent(requireContext(), IntroActivity::class.java)
                                    startActivity(intent)
                                }, 2500)
                            }
                        }

                    } catch (e: JSONException) {
                        val toast = Toast.makeText(
                            requireContext(),
                            "Invalid Json",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                }

                override fun onError(anError: ANError?) {
                    Looper.myLooper()?.let {
                        Handler(it).postDelayed({
                            val intent = Intent(requireContext(), IntroActivity::class.java)
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
                requireContext(),
                "Kesalahan Header",
                Toast.LENGTH_LONG
            )
            toast.show()
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

