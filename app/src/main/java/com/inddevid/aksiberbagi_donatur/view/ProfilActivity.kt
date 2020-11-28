package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
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
        if(alamatValue == ""){
            penggunaAlamat.text = "--"
        }else{
            penggunaAlamat.text = alamatValue
        }
            //Email donatur
        val penggunaProfesi : TextView = findViewById(R.id.profilProfesi)
        val profesiValue: String? = sharedPreference.getValueString("penggunaProfesi")
        if(profesiValue == ""){
            penggunaProfesi.text = "--"
        }else{
            penggunaProfesi.text = profesiValue
        }

        //deklarasi layoutUbah
        val layoutUbah : ConstraintLayout = findViewById(R.id.layoutUbah)
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
}