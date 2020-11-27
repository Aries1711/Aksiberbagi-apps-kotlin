package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.facebook.shimmer.ShimmerFrameLayout
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.BerandaProgramAll
import com.inddevid.aksiberbagi_donatur.presenter.ProgramFavoritAdapter
import com.inddevid.aksiberbagi_donatur.services.ApiError
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
 * Use the [FavoritIndex.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritIndex : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val arrayProgramAll = ArrayList<BerandaProgramAll>()
    private val TAG = "Program Favorit"

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

        // Inflate the layout for this fragment
        val title = "Favorit"
        val view: View = inflater.inflate(R.layout.fragment_favorit_index, container, false)
        val toolbar: Toolbar = view.findViewById(R.id.upAppbar)
        toolbar.inflateMenu(R.menu.favorit_upbar_menu)
        toolbar.title = title
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        val shimmerLayout: ShimmerFrameLayout = view.findViewById(R.id.shimmerProgramFavorit)
        shimmerLayout.startShimmer()
        val recyclerFavorit = view.findViewById<RecyclerView>(R.id.recyclerProgramFavorit)
        recyclerFavorit.visibility = View.GONE
        val sharedPreference: Preferences = Preferences(requireContext())
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")
        getKoneksi(retrivedToken, view)

        return view
    }

    private fun getKoneksi(tokenValue: String?, view: View){
        ApiService.getKoneksi(tokenValue).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                getAllProgramFavorit(tokenValue, view)
            }

            override fun onError(anError: ANError?) {
                refreshToken(tokenValue,view)
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
                        if (response?.getString("message").equals("Refresh berhasil")){
                            val token : String? = response?.getString("token")
                            //save token
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(token, view)
                            }
                        }else if(response?.getString("message").equals("Token expired berhasil di refresh")){
                            val token : String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(token, view)
                            }
                        }else{
                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                    val intent = Intent(requireContext(), IntroActivity::class.java)
                                    startActivity(intent)
                                }, 2500)
                            }
                        }

                    }catch (e : JSONException){
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

    private fun getAllProgramFavorit(tokenValue: String?, view: View){
        ApiService.getProgramFavorit(tokenValue).getAsJSONObject(object : JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                val shimmer = view.findViewById<ShimmerFrameLayout>(R.id.shimmerProgramFavorit)
                if(response?.getString("message").equals("Program favorit saya berhasil didapatkan")){
                    val jsonArray =response?.getJSONArray("data")
                    if (jsonArray?.length()!! > 0){
                        for(i in 0 until jsonArray.length()){
                            val item = jsonArray.getJSONObject(i)
                            val dataProgram = item?.getJSONObject("program")
                            val idProgram = dataProgram?.getString("tblprogram_id")
                            val img = dataProgram?.getString("thumbnail_url")
                            val judul = dataProgram?.getString("tblprogram_judul")
                            val volunter: String? = "Aksiberbagi.com"
                            val capaian = dataProgram?.getString("capaian_donasi")
                            val sisaHari = dataProgram?.getString("sisa_hari")
                            val startProgram = dataProgram?.getString("tanggal_mulai_donasi")
                            val targetNominal: String? = dataProgram?.getString("tblprogram_isiantargetnominal")
                            var progressProgram: Int? = dataProgram?.getInt("progress")
                            var targetDonasi : String?
                            if (targetNominal == "null" || targetNominal == "0" ){
                                targetDonasi = "100"
                            }else{
                                targetDonasi = dataProgram?.getString("target_nominal")
                            }
                            arrayProgramAll.add(BerandaProgramAll(idProgram,img,judul,volunter,capaian,sisaHari, startProgram,progressProgram,targetDonasi))
                            val myAdapterAll = ProgramFavoritAdapter(arrayProgramAll,requireContext())
                            val recyclerFavorit = view.findViewById<RecyclerView>(R.id.recyclerProgramFavorit)
                            recyclerFavorit.layoutManager = LinearLayoutManager(requireContext())
                            recyclerFavorit.adapter = myAdapterAll
                            recyclerFavorit.visibility = View.VISIBLE
                            shimmer.stopShimmer()
                            shimmer.visibility = View.GONE
                        }
                    }
                }else{
                    val layoutKosong  = view.findViewById<LinearLayout>(R.id.layoutFavoritKosong)
                    layoutKosong.visibility = View.VISIBLE
                    shimmer.stopShimmer()
                    shimmer.visibility = View.GONE
                }

            }

            override fun onError(anError: ANError?) {
                val shimmer = view.findViewById<ShimmerFrameLayout>(R.id.shimmerProgramFavorit)
                val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                if(apiError?.message == "Program favorit tidak ditemukan"){
                    val layoutKosong  = view.findViewById<LinearLayout>(R.id.layoutFavoritKosong)
                    layoutKosong.visibility = View.VISIBLE
                    shimmer.stopShimmer()
                    shimmer.visibility = View.GONE
                }
                Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
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