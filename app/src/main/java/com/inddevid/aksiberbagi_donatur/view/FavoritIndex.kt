package com.inddevid.aksiberbagi_donatur.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.CardHorizontalRecycler
import com.inddevid.aksiberbagi_donatur.presenter.RecyclerHorizontalAdapter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoritIndex.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritIndex : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var backButtonCount = 0

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
        // get val for arraylist
        val imageUrl:String = "https://aksiberbagi.com/storage/program/Sedekah%20Air%20untuk%20Pesantren%20dan%20Masjid%20-%20Alirkan%20Pahala%20Tak%20Terputus-banner.jpg"
        val titleCard:String = "Bangun Rumah Di Surga dengan menyantuni anak yatim"
        val volunteerCard:String = "AksiBerbagi.com"
        val moneyCard:String = "Rp 162.700.000"
        val dayCard:String = "17"

        // inflate the list cardview for favorite program
        val arrayList = ArrayList<CardHorizontalRecycler>()
        arrayList.add(CardHorizontalRecycler(titleCard, volunteerCard , moneyCard, dayCard, imageUrl ))
        arrayList.add(CardHorizontalRecycler(titleCard, volunteerCard , moneyCard, dayCard, imageUrl ))
        val myAdapter = RecyclerHorizontalAdapter(arrayList, requireActivity())

        // Inflate the layout for this fragment
        val title = "Favorit"
        val view: View = inflater.inflate(R.layout.fragment_favorit_index, container, false)
        val toolbar: Toolbar = view.findViewById(R.id.upAppbar)
        toolbar.inflateMenu(R.menu.favorit_upbar_menu)
        toolbar.title = title
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        var mainMenu = view.findViewById(R.id.recyclerFavorit) as RecyclerView
        mainMenu.layoutManager = LinearLayoutManager(requireActivity())
        mainMenu.adapter = myAdapter

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavoritIndex.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoritIndex().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}