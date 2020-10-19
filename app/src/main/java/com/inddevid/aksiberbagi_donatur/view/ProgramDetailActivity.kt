package com.inddevid.aksiberbagi_donatur.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
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
        val textNominalDonasi : TextInputEditText = view.findViewById(R.id.nominalDonasi)
        val btnDonasiClose: LinearLayout = view.findViewById(R.id.btnCollapse)
        val btnPilihBayar: TextView = view.findViewById(R.id.pilihPembayaranBtn)
        val imgPembayaran: ImageView = view.findViewById(R.id.imgBank)
        val textPembayaran: TextView = view.findViewById(R.id.titleJenisPembayaran)


        var dialogPembayaranAktif: String? = intent.getStringExtra("dialogAktif")
        var nominalPembayaran: String? = intent.getStringExtra("nominalDonasi")
        var pilihanPembayaran: String? = intent.getStringExtra("pilihanPembayaran")
        var imgPilihan: String? = intent.getStringExtra("imagePilihan")

        if (dialogPembayaranAktif == "true"  && pilihanPembayaran != ""){
            dialogPembayaran.show()
            textPembayaran.text = pilihanPembayaran
            Glide.with(this).load(imgPilihan).into(imgPembayaran)
            textNominalDonasi.text = nominalPembayaran?.toEditable()
        }else if(dialogPembayaranAktif == "true"){
            dialogPembayaran.show()
            textNominalDonasi.text = nominalPembayaran?.toEditable()
        }else{
            dialogPembayaran.dismiss()
        }


        btnDonasi.setOnClickListener {
            dialogPembayaran.show()
        }

        btnDonasiClose.setOnClickListener {
            dialogPembayaran.dismiss()
        }

        btnPilihBayar.setOnClickListener {
            val mIntent = Intent(this, PilihPembayaranActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("nominal", textNominalDonasi.text.toString())
            mIntent.putExtras(mBundle)
            startActivity(mIntent)
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

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
}

