package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.presenter.CustomDialogBatal
import com.inddevid.aksiberbagi_donatur.services.Preferences

class ProfilActivity : AppCompatActivity() {

    private var UbahAktif: String = "False"

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
        val inputNama: TextInputEditText = findViewById(R.id.namaLengkapInput)
        val inputEmail: TextInputEditText = findViewById(R.id.emailInput)
        val inputAlamat: TextInputEditText = findViewById(R.id.alamatInput)

        if(namaValue != "" || namaValue != "null"){
            inputNama.text = namaValue?.toEditable()
        }
        if(emailValue != "" || emailValue != "null"){
            inputEmail.text = emailValue?.toEditable()
        }
        if (alamatValue != "" || alamatValue != "null"){
            inputAlamat.text = alamatValue?.toEditable()
        }

        val profesiItems = listOf("Pilih Profesi","Dokter", "Guru", "Wiraswasta", "PNS", "Lainnya")
        val adapterProfesi = ArrayAdapter(this, R.layout.list_pilih_program_dropdown, profesiItems)
        val spinnerProfesi : Spinner = findViewById(R.id.spinerProfesiInput)
        spinnerProfesi.adapter = adapterProfesi
        var profesiPilihan = ""
        spinnerProfesi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               profesiPilihan = profesiItems[position]
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
    }

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
}