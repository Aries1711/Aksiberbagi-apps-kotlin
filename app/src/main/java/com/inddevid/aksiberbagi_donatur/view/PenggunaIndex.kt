package com.inddevid.aksiberbagi_donatur.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.PenggunaSettings
import com.inddevid.aksiberbagi_donatur.presenter.PenggunaSettingsAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PenggunaIndex.newInstance] factory method to
 * create an instance of this fragment.
 */
class PenggunaIndex : Fragment() {
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

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_pengguna_index, container, false)
        var listView: ListView = view.findViewById(R.id.listViewPengguna)
        listView.divider = this.resources.getDrawable(R.drawable.listview_divider_transparent)
        var list = mutableListOf<PenggunaSettings>()
        list.add(PenggunaSettings("pengaturan", R.drawable.p_settings))
        list.add(PenggunaSettings("Bantuan", R.drawable.p_help))
        list.add(PenggunaSettings("Tentang AksiBerbagi", R.drawable.p_akbersettings))
        list.add(PenggunaSettings("Syarat dan Ketentuan", R.drawable.p_agreement))
        list.add(PenggunaSettings("Rating Kami", R.drawable.p_akberrating))
        list.add(PenggunaSettings("Logout", R.drawable.p_logout))

        listView.adapter = PenggunaSettingsAdapter(requireActivity(), R.layout.pengguna_settings_row1,list)

        listView.setOnItemClickListener{ parent:AdapterView<*>, view:View, position:Int, id:Long ->
            if (position == 0){
                Toast.makeText(requireActivity(), "Pengaturan", Toast.LENGTH_LONG).show()
            }
            if (position == 1){
                Toast.makeText(requireActivity(), "Bantuan", Toast.LENGTH_LONG).show()
            }
            if (position == 2){
                Toast.makeText(requireActivity(), "Tentang Kami", Toast.LENGTH_LONG).show()
            }
            if (position == 3){
                Toast.makeText(requireActivity(), "Syarat dan Ketentuan", Toast.LENGTH_LONG).show()
            }
            if (position == 4){
                Toast.makeText(requireActivity(), "Rating Kami", Toast.LENGTH_LONG).show()
            }
            if (position == 5){
                Toast.makeText(requireActivity(), "Keluar mang ?", Toast.LENGTH_LONG).show()
            }
        }

        val toolbar: Toolbar = view.findViewById(R.id.upAppbar)
        toolbar.inflateMenu(R.menu.pengguna_upbar_menu)
        toolbar.title = "Akun"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        return view
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PenggunaIndex.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PenggunaIndex().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
