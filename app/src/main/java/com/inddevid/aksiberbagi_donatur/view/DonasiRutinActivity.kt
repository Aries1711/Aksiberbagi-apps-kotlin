package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.inddevid.aksiberbagi_donatur.R

class DonasiRutinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donasi_rutin_activity)
        val toolbar: Toolbar = findViewById(R.id.upAppbarDonasiRutin)
        toolbar.title = "Donasi Rutin"
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener{
            val mIntent = Intent(this, DashboardActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("berandaAktif", "true")
            mIntent.putExtras(mBundle)
            startActivity(mIntent)
        }

//         set pilihan program dropdown
        val items = listOf("Wujudkan Pondok Pesantren Tahfidz Qur’an Hadist Internasional Pekanbaru, Riau", "Simpan Hartamu dilangit, Sedekah Jariyah Atas Nama Keluarga", "Bangun Rumah di Surga: Sedekah Jariah Renovasi Masjid Pelosok", "Android")
        val adapterProgram = ArrayAdapter(this, R.layout.list_pilih_program_dropdown, items)
        val dropDownProgram : Spinner = findViewById(R.id.spinerProgram)
        dropDownProgram.adapter = adapterProgram

        val frekuensiItems = listOf("Harian", "Mingguan", "Bulanan")
        val adapterFrekuensi = ArrayAdapter(this, R.layout.list_pilih_program_dropdown, frekuensiItems)
        val dropDownFrekuensi : Spinner = findViewById(R.id.spinerFrekuensi)
        dropDownFrekuensi.adapter = adapterFrekuensi

        //set val spinner untuk frekuensi yang dipilih
        val frekuensiMingguans = listOf("senin", "selasa", "rabu", "kamis", "jumat", "minggu")
        val dropDownMingguan : Spinner = findViewById(R.id.frekuensiMingguan)
        val adapterMingguan = ArrayAdapter(this, R.layout.list_pilih_program_dropdown, frekuensiMingguans)
        dropDownMingguan.adapter = adapterMingguan

        val frekuensiBulanans = listOf("1","2","3","5","6","7","8","9","10", "11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28")
        val dropDownBulanan : Spinner = findViewById(R.id.frekuensiBulanan)
        val adapterBulanan = ArrayAdapter(this, R.layout.list_pilih_program_dropdown,frekuensiBulanans)
        dropDownBulanan.adapter = adapterBulanan

        //set layout hidden untuk frekuensi yang dipilih
        val layoutB : LinearLayout = findViewById(R.id.layoutBulanan)
        val layoutM : LinearLayout = findViewById(R.id.layoutMingguan)

        dropDownFrekuensi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (frekuensiItems[p2] == "Mingguan"){
                    show(layoutM)
                    gone(layoutB)
                }else if (frekuensiItems[p2] == "Bulanan"){
                    gone(layoutM)
                    show(layoutB)
                }else{
                    gone(layoutM)
                    gone(layoutB)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }
    fun gone(view: View){
        view.visibility = View.GONE
    }

    fun show(view: View){
        view.visibility = View.VISIBLE
    }
}