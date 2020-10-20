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
        val textNominalLayout: TextInputLayout = view.findViewById(R.id.nominalDonasiLayout)
        val textNominalDonasi : TextInputEditText = view.findViewById(R.id.nominalDonasi)
        val btnDonasiClose: LinearLayout = view.findViewById(R.id.btnCollapse)
        val btnPilihBayar: TextView = view.findViewById(R.id.pilihPembayaranBtn)
        val imgPembayaran: ImageView = view.findViewById(R.id.imgBank)
        val textPembayaran: TextView = view.findViewById(R.id.titleJenisPembayaran)
        val btnLanjutPembayaran: Button = view.findViewById(R.id.donasiLanjutPembayaran)
        val pilihNominal: Spinner = view.findViewById(R.id.spinerPilihNominal)

        //Array pilihan nominal donasi
        val nominalItems = listOf("Pilih Nominal Donasi","Rp 50000 Semua Bisa Sedekah",
            "Rp 100000 Sedekah Pilihan", "Rp 250000 Sedekah Terbaik", "Rp 500000 Sedekah Berkah",
            "Rp 1000000 Sedekah Pilihan", "Masukkan Nominal Lain")
        val adapterNominal = ArrayAdapter(this, R.layout.list_pilih_program_dropdown, nominalItems)
        pilihNominal.adapter = adapterNominal

        //set spinner untuk set keadaan dan nilai variabel yg terpengaruh spinner
        var spinnerPilihNominal : String? = ""

        pilihNominal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if ( nominalItems[p2] == "Masukkan Nominal Lain"){
                    spinnerPilihNominal = nominalItems[p2]
                    show(textNominalLayout)
                }else{
                    gone(textNominalLayout)
                    spinnerPilihNominal = nominalItems[p2]
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        hidden(imgPembayaran)
        gone(textNominalLayout)

        // Receiver intent data
        var dialogPembayaranAktif: String? = intent.getStringExtra("dialogAktif")
        var nominalPembayaran: String? = intent.getStringExtra("nominalDonasi")
        var pilihanPembayaran: String? = intent.getStringExtra("pilihanPembayaran")
        var imgPilihan: String? = intent.getStringExtra("imagePilihan")
        var spinner: String? = intent.getStringExtra("spinner")

        if (spinner == "Masukkan Nominal Lain"){
            pilihNominal.setSelection(adapterNominal.getPosition("Masukkan Nominal Lain"))
        }else{
            pilihNominal.setSelection(adapterNominal.getPosition("Pilih Nominal Donasi"))
        }

        if (dialogPembayaranAktif == "true"  && pilihanPembayaran != ""){
            dialogPembayaran.show()
            show(textNominalLayout)
            textPembayaran.text = pilihanPembayaran
            show(imgPembayaran)
            Glide.with(this).load(imgPilihan).into(imgPembayaran)
            textNominalDonasi.text = nominalPembayaran?.toEditable()
        }else if(dialogPembayaranAktif == "true"){
            dialogPembayaran.show()
            textNominalDonasi.text = nominalPembayaran?.toEditable()
        }else{
            dialogPembayaran.dismiss()
        }

        btnLanjutPembayaran.setOnClickListener { startActivity(Intent(this@ProgramDetailActivity, InvoiceActivity::class.java)) }


        btnDonasi.setOnClickListener {
            dialogPembayaran.show()
        }

        btnDonasiClose.setOnClickListener {
            dialogPembayaran.dismiss()
        }


        //deklarasi variabel untuk intent ke menu pilih pembayaran
        btnPilihBayar.setOnClickListener {
            val mIntent = Intent(this, PilihPembayaranActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("nominal", textNominalDonasi.text.toString())
            mBundle.putString("spinnerValue", spinnerPilihNominal)
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

    fun hidden(view: View){
        view.visibility = View.INVISIBLE
    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
}

