package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.inddevid.aksiberbagi_donatur.R

class DonasiRutinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donasi_rutin_activity)
        val toolbar: Toolbar = findViewById(R.id.upAppbarDonasiRutin)
        toolbar.title = "Donasi Rutin"
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener{
            val mIntent = Intent(this, DashboardActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("berandaAktif", "true")
            mIntent.putExtras(mBundle)
            startActivity(mIntent)}

        // set pilihan program dropdown
        val items = listOf("Wujudkan Pondok Pesantren Tahfidz Qur’an Hadist Internasional Pekanbaru, Riau", "Simpan Hartamu dilangit, Sedekah Jariyah Atas Nama Keluarga", "Bangun Rumah di Surga: Sedekah Jariah Renovasi Masjid Pelosok", "Android")
        val adapterProgram = ArrayAdapter(this, R.layout.list_pilih_program_dropdown, items)
        val dropDownProgram : AutoCompleteTextView = findViewById(R.id.pilihProgram)
        dropDownProgram.setAdapter(adapterProgram)

        val frekuensiItems = listOf("Harian", "Mingguan", "Bulanan")
        val adapterFrekuensi = ArrayAdapter(this, R.layout.list_pilih_program_dropdown, frekuensiItems)
        val dropDownFrekuensi : AutoCompleteTextView = findViewById(R.id.pilihFrekuensi)
        dropDownFrekuensi.setAdapter(adapterFrekuensi)

        //set val linearLayout untuk frekuensi yang dipilih
        var layoutMingguan: LinearLayout = findViewById(R.id.layoutFrekuensiMingguan)
        var layoutBulanan: LinearLayout = findViewById(R.id.layoutFrekuensiBulanan)

//        dropDownFrekuensi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(p0: AdapterView<frekuensiItems>?, p1: View?, p2: Int, p3: Long) {
////                if (frekuensiItems[p2] == "Mingguan"){
////                    show(layoutMingguan)
////                }else if(frekuensiItems[p2] == "Bulanan"){
////                    show(layoutBulanan)
////                }else{
////                    //
////                }
//                Toast.makeText(this@DonasiRutinActivity, "Bisa", Toast.LENGTH_LONG).show()
//            }
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                //
//            }
//
//        }
    }
    fun hidden(view: View){
        view.visibility = View.INVISIBLE
    }

    fun show(view: View){
        view.visibility = View.VISIBLE
    }
}