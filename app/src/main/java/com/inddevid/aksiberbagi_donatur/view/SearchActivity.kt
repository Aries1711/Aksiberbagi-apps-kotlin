package com.inddevid.aksiberbagi_donatur.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.facebook.shimmer.ShimmerFrameLayout
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.BerandaProgramAll
import com.inddevid.aksiberbagi_donatur.presenter.ProgramAllAdapter
import com.inddevid.aksiberbagi_donatur.services.ApiError
import com.inddevid.aksiberbagi_donatur.services.ApiService
import com.inddevid.aksiberbagi_donatur.services.Preferences
import org.json.JSONException
import org.json.JSONObject

class SearchActivity : AppCompatActivity() {
    private val TAG = "Search activity"
    private val arrayProgramSearch = ArrayList<BerandaProgramAll>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        val sharedPreference: Preferences = Preferences(this)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")

        val btnBackSearch : Button = findViewById(R.id.backSearchBtn)
        val btnClearSearch : Button = findViewById(R.id.clearSearchField)
        val fieldSearch : EditText = findViewById(R.id.searchField)

        hidden(btnClearSearch)
        openSoftKeyboard(this, fieldSearch)

        btnClearSearch.setOnClickListener{
            fieldSearch.clear()
            hidden(btnClearSearch)
        }

        btnBackSearch.setOnClickListener{
            startActivity(Intent(this@SearchActivity, DashboardActivity::class.java))
        }

        fieldSearch.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                val query = fieldSearch.text.toString()
                if(query != ""){
                    show(btnClearSearch)
                    val shimmer: ShimmerFrameLayout = findViewById(R.id.shimmerProgramSearch)
                    shimmer.startShimmer()
                    shimmer.visibility = View.VISIBLE
                    val layoutViewFalse: LinearLayout = findViewById(R.id.layoutSearchFalse)
                    layoutViewFalse.visibility = View.GONE
                    val layoutView: LinearLayout = findViewById(R.id.layoutSearchTrue)
                    layoutView.visibility = View.GONE
                    getKoneksi(retrivedToken, this@SearchActivity)
                }else{
                    val shimmer: ShimmerFrameLayout = findViewById(R.id.shimmerProgramSearch)
                    shimmer.stopShimmer()
                    shimmer.visibility = View.GONE
                    hidden(btnClearSearch)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                hidden(btnClearSearch)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
    }


    private fun getKoneksi(tokenValue: String?, context: SearchActivity){
        ApiService.getKoneksi(tokenValue).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                cariProgram(tokenValue,context)
            }

            override fun onError(anError: ANError?) {
                val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                if (apiError?.message == "Token expired"){
                    refreshToken(tokenValue, context)
                    val toast = Toast.makeText(this@SearchActivity,"Cobalah beberapa saat lagi",
                        Toast.LENGTH_LONG)
                    toast.show()
                }
            }

        })
    }

    private fun refreshToken(
        tokenValue: String?,
        context: SearchActivity
    ){
        val header : String? = tokenValue
        val sharedPreference: Preferences = Preferences(this)
        try {
            ApiService.postRefreshToken(header).getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        if (response?.getString("message").equals("Refresh berhasil")) {
                            val token: String? = response?.getString("token")
                            //save token
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(tokenValue, context)
                            }
                        } else if (response?.getString("message")
                                .equals("Token expired berhasil di refresh")
                        ) {
                            val token: String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(tokenValue, context)
                            }
                        } else {
                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                    val intent = Intent(
                                        this@SearchActivity,
                                        IntroActivity::class.java
                                    )
                                    startActivity(intent)
                                }, 2500)
                            }
                        }

                    } catch (e: JSONException) {
                        val toast = Toast.makeText(
                            this@SearchActivity,
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
                                this@SearchActivity,
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
                this,
                "Kesalahan Header",
                Toast.LENGTH_LONG
            )
            toast.show()
        }
    }

    private fun cariProgram(
        tokenValue: String?,
        context: SearchActivity){
        val fieldSearch : EditText = context.findViewById(R.id.searchField)
        val word = fieldSearch.text.toString()
        ApiService.getProgramWord(tokenValue, word).getAsJSONObject(object : JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                if(response?.getString("message").equals("Program berdasarkan judul didapatkan")){
                    val jsonArray =response?.getJSONArray("data")
                    if (jsonArray?.length()!! > 0){
                        for(i in 0 until jsonArray.length()){
                            val item = jsonArray.getJSONObject(i)
                            val idProgram = item?.getString("tblprogram_id")
                            val img = item?.getString("thumbnail_url")
                            val judul = item?.getString("tblprogram_judul")
                            val volunter: String? = "Aksiberbagi.com"
                            val capaian = item?.getString("capaian_donasi")
                            val sisaHari = item?.getString("sisa_hari")
                            val startProgram = item?.getString("tanggal_mulai_donasi")
                            val targetNominal: String? = item?.getString("tblprogram_isiantargetnominal")
                            var progressProgram: Int? = item?.getInt("progress")
                            var targetDonasi : String?
                            if (targetNominal == "null" || targetNominal == "0" ){
                                targetDonasi = "100"
                            }else{
                                targetDonasi = item?.getString("target_nominal")
                            }
                            val nav = "ProgramAll"
                            arrayProgramSearch.add(BerandaProgramAll(idProgram,img,judul,volunter,capaian,sisaHari, startProgram,progressProgram,targetDonasi, nav))
                            val myAdapterAll = ProgramAllAdapter(arrayProgramSearch,this@SearchActivity)
                            val view = context.findViewById<RecyclerView>(R.id.recyclerProgramSearch)
                            view.layoutManager = LinearLayoutManager(this@SearchActivity)
                            view.adapter = myAdapterAll
                            val layoutView = context.findViewById<LinearLayout>(R.id.layoutSearchTrue)
                            layoutView.visibility = View.VISIBLE
                            val shimmer = context.findViewById<ShimmerFrameLayout>(R.id.shimmerProgramSearch)
                            shimmer.stopShimmer()
                            shimmer.visibility = View.GONE
                        }
                    }
                }else if(response?.getString("message").equals("Program berdasarkan judul tidak ada")){
                    val shimmer = context.findViewById<ShimmerFrameLayout>(R.id.shimmerProgramSearch)
                    shimmer.stopShimmer()
                    shimmer.visibility = View.GONE
                    val layoutView = context.findViewById<LinearLayout>(R.id.layoutSearchFalse)
                    layoutView.visibility = View.VISIBLE
                }
            }

            override fun onError(anError: ANError?) {
                val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                if (apiError?.message == "Token expired"){
                    refreshToken(tokenValue, context)
                    val toast = Toast.makeText(this@SearchActivity,"Cobalah beberapa saat lagi",
                        Toast.LENGTH_LONG)
                    toast.show()
                }

                Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
            }

        } )


    }


    fun hidden(view: View){
        view.visibility = View.INVISIBLE
    }

    fun show(view: View){
        view.visibility = View.VISIBLE
    }

    fun openSoftKeyboard(context: Context, view: View) {
        view.requestFocus()
        // open the soft keyboard
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    fun EditText.clear() {
        text.clear()
    }

}