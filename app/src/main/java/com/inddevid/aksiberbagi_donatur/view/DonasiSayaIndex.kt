package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.CardDonasiSaya
import com.inddevid.aksiberbagi_donatur.model.DateDonasiSaya
import com.inddevid.aksiberbagi_donatur.presenter.DateDonasiSayaAdapter
import com.inddevid.aksiberbagi_donatur.presenter.RecyclerDonasiSayaAdapter
import com.inddevid.aksiberbagi_donatur.services.ApiService
import com.inddevid.aksiberbagi_donatur.services.Preferences
import org.json.JSONException
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DonasiSayaIndex.newInstance] factory method to
 * create an instance of this fragment.
 */
class DonasiSayaIndex : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val TAG = "Fragment Donasi Saya"
    private var backButtonCount = 0
    private val arrayDonasi = ArrayList<CardDonasiSaya>()
    private val arrayDate = ArrayList<DateDonasiSaya>()
    private val arrayHari: ArrayList<String> = arrayListOf("SEN", "SEL", "RAB", "KAM", "JUM", "SAB", "MIN")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //set preferences
        val sharedPreference: Preferences = Preferences(requireContext())
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")


        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_donasi_saya_index , container, false)
        val toolbar: Toolbar = view.findViewById(R.id.upAppbar)
        toolbar.inflateMenu(R.menu.donasisaya_upbar_menu)
        toolbar.title = "Donasi Saya"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        //tombol lihat semua donasi
        val btnLihatSemua : Button = view.findViewById(R.id.lihatSemua)

        btnLihatSemua.setOnClickListener { startActivity(Intent(requireActivity(), SemuaDonasiSayaActivity::class.java)) }

        getKoneksi(retrivedToken, view)
        return view
    }

    private fun getKoneksi(
        tokenValue: String?,
        view: View
    ){
        val header: String? = tokenValue
        ApiService.getKoneksi(header).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                getDonasiSayaAtribut(tokenValue, view)
            }

            override fun onError(anError: ANError?) {
                refreshToken(tokenValue,view)
            }

        })
    }

    private fun refreshToken(
        tokenValue: String?,
        view: View
    ){
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
                                getDonasiSayaAtribut(token, view)
                            }
                        } else if (response?.getString("message")
                                .equals("Token expired berhasil di refresh")
                        ) {
                            val token: String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getDonasiSayaAtribut(token, view)
                            }
                        } else {
                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                    val intent = Intent(
                                        requireContext(),
                                        IntroActivity::class.java
                                    )
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
                            val intent = Intent(
                                requireContext(),
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
                requireContext(),
                "Kesalahan Header",
                Toast.LENGTH_LONG
            )
            toast.show()
        }
    }

    private fun getDonasiSayaAtribut(
        tokenValue: String?,
        view: View
    ){
        ApiService.getDonasiSaya(tokenValue).getAsJSONObject(object: JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                val headerWeek = response?.getString("header_week")
                var headerText = view.findViewById<TextView>(R.id.monthYears)
                headerText.text = headerWeek

                val weekStart = response?.getString("weekStart")!!.toInt()
                val weekEnd = response?.getString("weekEnd")!!.toInt()
                val dateNow = response?.getString("dayActive")
                val dayDonasi = response?.getJSONArray("array_tgl_adadonasi")
                val arrayDayDonasi : ArrayList<String> = arrayListOf()

                for(i in 0 until dayDonasi.length()){
                    val item = dayDonasi[i].toString()
                    arrayDayDonasi.add(item)
                }

                for(i in 0 until 7){
                    val tanggal = weekStart + i
                    arrayDate.add(DateDonasiSaya(arrayHari[i], "$tanggal", dateNow, arrayDayDonasi))
                }

                val myAdapterA = DateDonasiSayaAdapter(arrayDate, requireActivity())
                //inflate the recycler for Date donasi saya
                var mainMenuA = view.findViewById(R.id.recyclerDonasiSayaDate) as RecyclerView
                mainMenuA.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
                mainMenuA.adapter = myAdapterA

                val nominalHariIni = response?.getString("nominal_hari_ini")
                var nominalHariInitext = view.findViewById<TextView>(R.id.nominalDonasiHariIni)
                nominalHariInitext.text = "Rp. $nominalHariIni"

                val nominalMingguIni = response?.getString("nominal_minggu_ini")
                var nominalMingguIniText = view.findViewById<TextView>(R.id.nominalDonasiMingguIni)
                var tanggalMingguIniText = view.findViewById<TextView>(R.id.tanggalDonasiCard)
                nominalMingguIniText.text = "Rp. $nominalMingguIni"
                tanggalMingguIniText.text = "$weekStart / $weekEnd $headerWeek"

                val totalJumlahDonasi = response?.getString("total_donasi_jumlah")
                var totalJumlahDonasiText = view.findViewById<TextView>(R.id.totalJumlahDonasiText)
                totalJumlahDonasiText.text = "Dari $totalJumlahDonasi X donasi"

                val totalNominalDonasi = response?.getString("total_donasi_nominal")
                var totalNominalDonasiText = view.findViewById<TextView>(R.id.totalNominalDonasiText)
                totalNominalDonasiText.text = "Rp. $totalNominalDonasi"
                // get 5 donasi terakhir user
                val invoiceDonasi = response?.getJSONArray("data")
                if (invoiceDonasi?.length()!! > 0){
                    for(i in 0 until 5){
                        val item = invoiceDonasi.getJSONObject(i)
                        val program = item?.getJSONObject("program")
                        val judulProgram = program?.getString("tblprogram_judul")
                        val bank = item?.getJSONObject("bank")
                        val namaBank = bank?.getString("tblbank_nama")
                        val nominaldonasi = item?.getString("tbldonasi_nominal")
                        val waktuDonasi = item?.getString("tbldonasi_tglinsert")
                        val donasiStatus = item?.getString("tbldonasi_status")
                        val gambarProgram = program?.getString("tblprogram_file")
                        arrayDonasi.add(CardDonasiSaya(judulProgram,namaBank,nominaldonasi,waktuDonasi,donasiStatus,gambarProgram))
                    }
                    //inflate the recycler for cardview donasi saya
                    val myAdapterB = RecyclerDonasiSayaAdapter(arrayDonasi, requireActivity())
                    var mainMenuB = view.findViewById(R.id.recyclerDonasiSaya) as RecyclerView
                    mainMenuB.layoutManager = LinearLayoutManager(requireActivity())
                    mainMenuB.adapter = myAdapterB
                }

            }

            override fun onError(anError: ANError?) {
                val toast = Toast.makeText(requireContext(), "ada Kesalahan ", Toast.LENGTH_LONG)
                toast.show()
            }

        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DonasiSayaIndex.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DonasiSayaIndex().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}