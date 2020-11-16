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

        // set val for arraylist date donasi saya
        val dayA:String = "SEN"
        val dateA:String = "21"
        val dayB:String = "SEL"
        val dateB:String = "22"
        val dayC:String = "RAB"
        val dateC:String = "23"
        val dayD:String = "KAM"
        val dateD:String = "24"
        val dayE:String = "JUM"
        val dateE:String = "25"
        val dayF:String = "SAB"
        val dateF:String = "26"
        val dayG:String = "MIN"
        val dateG:String = "27"
        // declare the arraylist as arraydate for date donasi saya
        val arrayDate = ArrayList<DateDonasiSaya>()
        arrayDate.add(DateDonasiSaya(dayA, dateA))
        arrayDate.add(DateDonasiSaya(dayB, dateB))
        arrayDate.add(DateDonasiSaya(dayC, dateC))
        arrayDate.add(DateDonasiSaya(dayD, dateD))
        arrayDate.add(DateDonasiSaya(dayE, dateE))
        arrayDate.add(DateDonasiSaya(dayF, dateF))
        arrayDate.add(DateDonasiSaya(dayG, dateG))
        val myAdapterA = DateDonasiSayaAdapter(arrayDate, requireActivity())

        // set val for arraylist card donasi saya
        val imageUrl:String = "https://aksiberbagi.com/storage/program/Raih%20Keutamaan%20Bulan%20Muharram;%20Perbanyak%20Amal%20Shalih-banner.jpg"
        val titleCardDonasi:String = "Sedekah Air untuk Pesantren Pelosok dan ..."
        val titleCardDonasiA:String = "Oke"
        val paymentDonasi:String = "Gopay"
        val timePayment:String = "1 jam lalu"
        val donasiSum:String = "Rp 100.789"
        val donasiSayaStat:String = "Berhasil"
        //declare the arraylist for card donasi saya
        val arrayList = ArrayList<CardDonasiSaya>()
        arrayList.add(CardDonasiSaya(titleCardDonasi,paymentDonasi,donasiSum,timePayment,donasiSayaStat,imageUrl))
        arrayList.add(CardDonasiSaya(titleCardDonasiA,paymentDonasi,donasiSum,timePayment,donasiSayaStat,imageUrl))
        arrayList.add(CardDonasiSaya(titleCardDonasi,paymentDonasi,donasiSum,timePayment,donasiSayaStat,imageUrl))
        val myAdapterB = RecyclerDonasiSayaAdapter(arrayList, requireActivity())

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_donasi_saya_index , container, false)
        val toolbar: Toolbar = view.findViewById(R.id.upAppbar)
        toolbar.inflateMenu(R.menu.donasisaya_upbar_menu)
        toolbar.title = "Donasi Saya"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        //tombol lihat semua donasi
        val btnLihatSemua : Button = view.findViewById(R.id.lihatSemua)

        btnLihatSemua.setOnClickListener { startActivity(Intent(requireActivity(), SemuaDonasiSayaActivity::class.java)) }

        //inflate the recycler for Date donasi saya
        var mainMenuA = view.findViewById(R.id.recyclerDonasiSayaDate) as RecyclerView
        mainMenuA.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
        mainMenuA.adapter = myAdapterA
        //inflate the recycler for cardview donasi saya
        var mainMenuB = view.findViewById(R.id.recyclerDonasiSaya) as RecyclerView
        mainMenuB.layoutManager = LinearLayoutManager(requireActivity())
        mainMenuB.adapter = myAdapterB


        return view
    }

    private fun getKoneksi(
        tokenValue: String?
    ){
        val header: String? = tokenValue
        ApiService.getKoneksi(header).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                getDonasiSayaAtribut(tokenValue)
            }

            override fun onError(anError: ANError?) {
                refreshToken(tokenValue)
            }

        })
    }

    private fun refreshToken(
        tokenValue: String?
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
                                getDonasiSayaAtribut(token)
                            }
                        } else if (response?.getString("message")
                                .equals("Token expired berhasil di refresh")
                        ) {
                            val token: String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getDonasiSayaAtribut(token)
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

    private fun getDonasiSayaAtribut(tokenValue: String?){
        ApiService.getDonasiSaya(tokenValue).getAsJSONObject(object: JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                TODO("Not yet implemented")
            }

            override fun onError(anError: ANError?) {
                TODO("Not yet implemented")
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