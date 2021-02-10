package com.aksiberbagi.donatur.view

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.model.PenggunaSettings
import com.aksiberbagi.donatur.presenter.CustomDialogLogout
import com.aksiberbagi.donatur.presenter.PenggunaSettingsAdapter
import com.aksiberbagi.donatur.services.Preferences

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PenggunaIndex.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("DEPRECATION")
class PenggunaIndex : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var backButtonCount = 0
    private val TAG = "Pengguna index"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_pengguna_index, container, false)
        val sharedPreference: Preferences = Preferences(requireContext())
        val toolbar: Toolbar = view.findViewById(R.id.upAppbar)
        toolbar.inflateMenu(R.menu.pengguna_upbar_menu)
        toolbar.title = "Akun"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        var listView: ListView = view.findViewById(R.id.listViewPengguna)
        listView.divider = this.resources.getDrawable(R.drawable.listview_divider_transparent)
        var list = mutableListOf<PenggunaSettings>()
        list.add(PenggunaSettings("Pengaturan", R.drawable.p_settings))
        list.add(PenggunaSettings("Bantuan", R.drawable.p_help))
        list.add(PenggunaSettings("Tentang AksiBerbagi", R.drawable.p_akbersettings))
        list.add(PenggunaSettings("Syarat dan Ketentuan", R.drawable.p_agreement))
        list.add(PenggunaSettings("Rating Kami", R.drawable.p_akberrating))
        list.add(PenggunaSettings("Logout", R.drawable.p_logout))

        listView.adapter = PenggunaSettingsAdapter(requireActivity(), R.layout.pengguna_settings_row1,list)

        listView.setOnItemClickListener{ _: AdapterView<*>, _:View, position:Int, _:Long ->
            if (position == 0){
                startActivity(Intent(requireActivity(),PengaturanActivity::class.java))
            }
            if (position == 1){
                startActivity(Intent(requireActivity(),BantuanActivity::class.java))
            }
            if (position == 2){
                startActivity(Intent(requireActivity(),TentangActivity::class.java))
            }
            if (position == 3){
                startActivity(Intent(requireActivity(),SyaratKetentuanActivity::class.java))
            }
            if (position == 4){
                val packageName = "com.aksiberbagi.donatur"
                val uri: Uri = Uri.parse("market://details?id=$packageName")
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)

                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                try {
                    startActivity(goToMarket)
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
                }
            }
            if (position == 5){
                var dialog = CustomDialogLogout()
                fragmentManager?.let { dialog.show(it, "customDialog") }
            }
        }

        val penggunaNama = sharedPreference.getValueString("penggunaNAMA")
        val penggunaAlamat = sharedPreference.getValueString("penggunaAlamat")
        val penggunaProfesi = sharedPreference.getValueString("penggunaProfesi")
        val penggunaEmail = sharedPreference.getValueString("penggunaEmail")
//        val toast = Toast.makeText(requireContext(), "Nama '$penggunaNama' n/ Alamat '$penggunaAlamat' n/ Profesi '$penggunaProfesi' n/ Email '$penggunaEmail' n/  ",Toast.LENGTH_LONG)
//        toast.show()
        var totalProfil = 1
        if(penggunaAlamat !=  "null" ){
            totalProfil = 2
        }
        if(penggunaProfesi !=  "null" ){
            totalProfil = 3
        }
        if(penggunaEmail != "null"){
            totalProfil = 4
        }


        var profilProgress: Double = (totalProfil.toDouble() / 4) * 100

        Log.d(TAG, " total profil = $profilProgress")
        val layoutProfil : LinearLayout = view.findViewById(R.id.pengaturanProfil)
        layoutProfil.setOnClickListener{
            startActivity(Intent(requireContext(), ProfilActivity::class.java))
        }

        val penggunaNamaValue = sharedPreference.getValueString("penggunaNAMA")
        val penggunaTotalDonasiValue = sharedPreference.getValueString("penggunaTotalDonasi")

        val penggunaText : TextView = view.findViewById(R.id.penggunaNamaText)
        penggunaText.text = penggunaNamaValue
        val totalDonasiText : TextView = view.findViewById(R.id.penggunaTotalDonasiText)
        totalDonasiText.text = "Rp $penggunaTotalDonasiValue"
        val progresText : TextView = view.findViewById(R.id.progressText)
        val progressFix = profilProgress.toInt()
        progresText.text = "$progressFix %"
        val progressProfil : ProgressBar = view.findViewById(R.id.progressBar)
        progressProfil.progress = progressFix

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
