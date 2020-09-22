package com.inddevid.aksiberbagi_donatur.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.CardDonasiSaya
import com.inddevid.aksiberbagi_donatur.model.DateDonasiSaya
import com.inddevid.aksiberbagi_donatur.presenter.DateDonasiSayaAdapter
import com.inddevid.aksiberbagi_donatur.presenter.RecyclerDonasiSayaAdapter


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
        val donasiSum:String = "100.789"
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
        //inflate the recycler for cardview donasi saya
        var mainMenuA = view.findViewById(R.id.recyclerDonasiSayaDate) as RecyclerView
        mainMenuA.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
        mainMenuA.adapter = myAdapterA
        var mainMenuB = view.findViewById(R.id.recyclerDonasiSaya) as RecyclerView
        mainMenuB.layoutManager = LinearLayoutManager(requireActivity())
        mainMenuB.adapter = myAdapterB


        return view
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