package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.ListDonasi
import com.inddevid.aksiberbagi_donatur.presenter.ListDonasiAdapter


class ProgramDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.program_detail_activity)

        val imageUrl:String ="https://aksiberbagi.com/storage/program/Jumat%20Berkah%20Bersedekah%20Jariyah%20Atas%20Nama%20Keluarga-banner.jpeg"
        val imageProgram: ImageView = findViewById(R.id.toolbarImage)

        //deklarasi variabel sementara untuk List Donasi Donatur (Recycler view)
        val imagDonatur:String = ""
        val nmDonatur:String = "Anonim"
        val donDonatur:String = "20.987"
        val pDonaturA:String = "Sesurga bersama Rasulullah Aamiin semoga terwujud"
        val pDonaturB:String = ""
        val tDonatur:String = "13 jam yang lalu"
        val arrayDonasi = ArrayList<ListDonasi>()
        arrayDonasi.add(ListDonasi(imagDonatur, nmDonatur, donDonatur, pDonaturA, tDonatur))
        arrayDonasi.add(ListDonasi(imagDonatur, nmDonatur, donDonatur, pDonaturB, tDonatur))
        arrayDonasi.add(ListDonasi(imagDonatur, nmDonatur, donDonatur, pDonaturA, tDonatur))
        arrayDonasi.add(ListDonasi(imagDonatur, nmDonatur, donDonatur, pDonaturB, tDonatur))
        arrayDonasi.add(ListDonasi(imagDonatur, nmDonatur, donDonatur, pDonaturB, tDonatur))
        val myAdapterListDonasi = ListDonasiAdapter(arrayDonasi, this)

        // inflate the vertical total Donasi
        var mainMenuAll = findViewById<RecyclerView>(R.id.totalDonasiRecycler)
        mainMenuAll.layoutManager = LinearLayoutManager(this)
        mainMenuAll.adapter = myAdapterListDonasi

        //appbar background Image
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(900, 470)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

        Glide.with(this).load(imageUrl).apply(options).into(imageProgram)

        val toolbar: Toolbar = findViewById(R.id.upAppbarProgramDetail)
        toolbar.setNavigationOnClickListener{startActivity(
            Intent(
                this@ProgramDetailActivity,
                DashboardActivity::class.java
            )
        )}
        val myWebView: WebView = findViewById(R.id.keteranganProgram)
        myWebView.loadUrl("https://aksiberbagi.com/sedekahair")

        //deklarasi btn Donasi dan dialog swipe
        val btnDonasi: Button = findViewById(R.id.donasiBtn)
        val dialogPembayaran = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view : View  = layoutInflater.inflate(R.layout.dialog_pembayaran_donasi, null)
        dialogPembayaran.setContentView(view)
        val btnDonasiClose: LinearLayout = view.findViewById(R.id.btnCollapse)
        dialogPembayaran.dismiss()
        btnDonasi.setOnClickListener {
            dialogPembayaran.show()
        }
        btnDonasiClose.setOnClickListener {
            dialogPembayaran.dismiss()
        }

        val doaBtn: Switch = view.findViewById(R.id.doaDonasiBtn)
        val inputLayoutDoa: TextInputLayout = view.findViewById(R.id.doaDonasi)
        gone(inputLayoutDoa)
        doaBtn?.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
            if (message == "Switch1:ON"){
                show(inputLayoutDoa)
            }else{
                gone(inputLayoutDoa)
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun gone(view: View){
        view.visibility = View.GONE
    }

    fun show(view: View){
        view.visibility = View.VISIBLE
    }

}

