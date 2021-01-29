package com.aksiberbagi.donatur.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.presenter.CustomDialogBatal
import com.aksiberbagi.donatur.services.ApiError
import com.aksiberbagi.donatur.services.ApiService
import com.aksiberbagi.donatur.services.Preferences
import org.json.JSONException
import org.json.JSONObject

class ProfilActivity : AppCompatActivity() {
    private val TAG = "Profil Activity"
    private var UbahAktif: String = "False"
    private var spinnerProfesiValue: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profil_activity)


        val sharedPreference: Preferences = Preferences(this)
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")

        val toolbar: Toolbar = findViewById(R.id.upAppbarProfil)
        toolbar.inflateMenu(R.menu.pengguna_upbar_menu)
        toolbar.title = "Profil"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        //deklarasi layoutUtama
        val layoutUtama : ConstraintLayout = findViewById(R.id.layoutUtama)
            //nama donatur
        val penggunaNamaText : TextView = findViewById(R.id.profilNama)
        val namaValue: String? = sharedPreference.getValueString("penggunaNAMA")
        penggunaNamaText.text = namaValue

            //Alamat donatur
        val penggunaAlamat : TextView = findViewById(R.id.profilAlamat)
        val alamatValue: String? = sharedPreference.getValueString("penggunaAlamat")
        val alamatStatus: ImageView = findViewById(R.id.alamatStatus)
        if(alamatValue == "" || alamatValue == "null"){
            penggunaAlamat.text = "--"
            alamatStatus.visibility = View.GONE
        }else{
            penggunaAlamat.text = alamatValue
        }

            //Email donatur
        val penggunaEmail : TextView = findViewById(R.id.profilEmail)
        val emailValue: String? = sharedPreference.getValueString("penggunaEmail")
        val emailStatus: ImageView = findViewById(R.id.emailStatus)
        if(emailValue == "" || emailValue == "null"){
            penggunaEmail.text = "--"
            emailStatus.visibility = View.GONE
        }else{
            penggunaEmail.text = emailValue
        }
            //Profesi donatur
        val penggunaProfesi : TextView = findViewById(R.id.profilProfesi)
        val profesiValue: String? = sharedPreference.getValueString("penggunaProfesi")
        val profesiStatus: ImageView = findViewById(R.id.profesiStatus)
        if(profesiValue == "" || profesiValue == "null"){
            penggunaProfesi.text = "--"
            profesiStatus.visibility = View.GONE
        }else{
            penggunaProfesi.text = profesiValue
        }

        //deklarasi layoutUbah
        val layoutUbah : ConstraintLayout = findViewById(R.id.layoutUbah)
        val layoutInputNama : TextInputLayout = findViewById(R.id.namaLengkapInputLayout)
        val inputNama: TextInputEditText = findViewById(R.id.namaLengkapInput)
        val layoutInputEmail : TextInputLayout = findViewById(R.id.emailInputLayout)
        val inputEmail: TextInputEditText = findViewById(R.id.emailInput)
        val layoutInputAlamat : TextInputLayout = findViewById(R.id.alamatInputLayout)
        val inputAlamat: TextInputEditText = findViewById(R.id.alamatInput)
        val btnSubmitUbah: Button = findViewById(R.id.btnSubmitUbah)

        if(namaValue == "" || namaValue == "null" || namaValue == null){
            val value = "-"
            inputEmail.text = value.toEditable()
        }else{
            inputNama.text = namaValue?.toEditable()
        }

        if(emailValue == "" || emailValue == "null" || emailValue == null){
            val value = "-"
            inputEmail.text = value.toEditable()
        }else{
            inputEmail.text = emailValue?.toEditable()
        }

        if (alamatValue == "" || alamatValue == null || alamatValue == "null"){
            val value = "-"
            inputAlamat.text = value.toEditable()
        }else{
            inputAlamat.text = alamatValue?.toEditable()
        }

        val profesiItems = listOf("Pilih profesi","Dokter", "Guru", "Wiraswasta", "PNS", "Freelancer" , "Lainnya")
        val adapterProfesi = ArrayAdapter(this, R.layout.list_pilih_program_dropdown, profesiItems)
        val spinnerProfesi : Spinner = findViewById(R.id.spinerProfesiInput)
        spinnerProfesi.adapter = adapterProfesi
        spinnerProfesi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerProfesiValue = profesiItems[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }



        val btnUbahData: Button = findViewById(R.id.btnUbahProfil)
        btnUbahData.setOnClickListener {
            layoutUtama.visibility = View.GONE
            layoutUbah.visibility = View.VISIBLE
            toolbar.title = "Ubah Profil"
            UbahAktif = "True"
        }

        toolbar.setNavigationOnClickListener{

            if(UbahAktif == "True"){
                UbahAktif = "False"
                var dialog = CustomDialogBatal()
                supportFragmentManager.let { dialog.show(it, "dialogBatal") }
            }else{
                val mIntent = Intent(this, DashboardActivity::class.java)
                val mBundle = Bundle()
                mBundle.putString("penggunaAktif", "true" )
                mIntent.putExtras(mBundle)
                startActivity(mIntent)
            }
        }

        btnSubmitUbah.setOnClickListener {
            val namaInput = inputNama.text.toString()
            val emailInput = inputEmail.text.toString()
            val alamatInput = inputAlamat.text.toString()
            if (namaInput == "" || namaInput == "-"){
                layoutInputNama.requestFocus()
                layoutInputNama.boxStrokeColor = ContextCompat.getColor(
                    this@ProfilActivity,
                    R.color.error_color
                )
                layoutInputNama.helperText = "Nama tidak boleh kosong"
                return@setOnClickListener
            }

            if (emailInput == "" || emailInput == "-"){
                layoutInputEmail.requestFocus()
                layoutInputEmail.boxStrokeColor = ContextCompat.getColor(
                    this@ProfilActivity,
                    R.color.error_color
                )
                layoutInputEmail.helperText = "Email tidak boleh kosong"
                return@setOnClickListener
            }

            if (alamatInput == "" || alamatInput == "-"){
                layoutInputAlamat.requestFocus()
                layoutInputAlamat.boxStrokeColor = ContextCompat.getColor(
                    this@ProfilActivity,
                    R.color.error_color
                )
                layoutInputAlamat.helperText = "Alamat tidak boleh kosong"
                return@setOnClickListener
            }

            if(spinnerProfesiValue == "" || spinnerProfesiValue == "Pilih profesi"){
                val helperSpinner: TextView = findViewById(R.id.helperSpinner)
                helperSpinner.visibility = View.VISIBLE
                return@setOnClickListener
            }
            getKoneksi(retrivedToken, this)
        }
    }

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    private fun getKoneksi(tokenValue: String?, context: ProfilActivity){
        ApiService.getKoneksi(tokenValue).getAsJSONObject(object: JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                putUbahProfil(tokenValue, context)
            }

            override fun onError(anError: ANError?) {
                val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                if(anError?.errorDetail!!.equals("connectionError")){
                    val toast = Toast.makeText(
                        this@ProfilActivity,
                        "Ada masalah dengan Koneksi Internet Anda",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    return
                }else{
                    refreshToken(tokenValue,context)
                }
            }
        })
    }

    private fun refreshToken(tokenValue: String?, context: ProfilActivity){
        val header : String? = tokenValue
        val sharedPreference: Preferences = Preferences(this@ProfilActivity)
        try {
            ApiService.postRefreshToken(header).getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        if (response?.getString("message").equals("Refresh berhasil")){
                            val token : String? = response?.getString("token")
                            //save token
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(token, context)
                            }
                        }else if(response?.getString("message").equals("Token expired berhasil di refresh")){
                            val token : String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(token, context)
                            }
                        }else{
                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                    val intent = Intent(this@ProfilActivity, IntroActivity::class.java)
                                    startActivity(intent)
                                }, 2500)
                            }
                        }

                    }catch (e : JSONException){
                        val toast = Toast.makeText(
                            this@ProfilActivity,
                            "Invalid Json",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                }
                override fun onError(anError: ANError?) {
                    Looper.myLooper()?.let {
                        Handler(it).postDelayed({
                            val intent = Intent(this@ProfilActivity, IntroActivity::class.java)
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
                this@ProfilActivity,
                "Kesalahan Header",
                Toast.LENGTH_LONG
            )
            toast.show()
        }
    }

    private fun putUbahProfil(tokenValue: String?, context: ProfilActivity){
        val sharedPreference: Preferences = Preferences(this)

        val inputNama: TextInputEditText = context.findViewById(R.id.namaLengkapInput)
        val inputAlamat: TextInputEditText = context.findViewById(R.id.alamatInput)
        val inputEmail: TextInputEditText = context.findViewById(R.id.emailInput)

        val body = JSONObject()
        body.put("tbldonatur_nama", inputNama.text.toString())
        body.put("tbldonatur_email", inputEmail.text.toString())
        body.put("tbldonatur_alamat", inputAlamat.text.toString())
        body.put("tbldonatur_pekerjaan", spinnerProfesiValue)
        ApiService.putProfil(tokenValue, body).getAsJSONObject(object: JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                if(response?.getString("message").equals("Data pengguna berhasil diubah")){
                    val toast = Toast.makeText(
                        this@ProfilActivity,
                        "Ubah data profil berhasil",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    val jsonArray = response?.getJSONArray("data")
                    val data = jsonArray?.getJSONObject(0)
                    val donaturNama = data?.getString("tbldonatur_nama")
                    val donaturAlamat = data?.getString("tbldonatur_alamat")
                    val donaturEmail = data?.getString("tbldonatur_email")
                    val donaturProfesi = data?.getString("tbldonatur_pekerjaan")
                    sharedPreference.save("penggunaNAMA", donaturNama )
                    sharedPreference.save("penggunaAlamat", donaturAlamat )
                    sharedPreference.save("penggunaProfesi", donaturProfesi )
                    sharedPreference.save("penggunaEmail", donaturEmail )

                    val layoutUbah: ConstraintLayout = context.findViewById(R.id.layoutUbah)
                    val layoutUtama: ConstraintLayout = context.findViewById(R.id.layoutUtama)
                    layoutUbah.visibility = View.GONE
                    layoutUtama.visibility = View.VISIBLE
                }
            }

            override fun onError(anError: ANError?) {
                Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
            }

        })
    }

}