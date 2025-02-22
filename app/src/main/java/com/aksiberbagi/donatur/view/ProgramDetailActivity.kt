package com.aksiberbagi.donatur.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.model.ListDonasi
import com.aksiberbagi.donatur.presenter.ListDonasiAdapter
import com.aksiberbagi.donatur.services.*
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONException
import org.json.JSONObject
import kotlin.toString as toString1


class ProgramDetailActivity : AppCompatActivity() {

    private val arrayDonatur = ArrayList<ListDonasi>()
    private val arrayDonaturDialog = ArrayList<ListDonasi>()
    private val TAG = "Program Detail"
    private val nominalItems: ArrayList<String> = arrayListOf("Pilih Nominal Donasi")
    private val dialogDonaturPilihan: ArrayList<String> = arrayListOf("200 donasi terbaru")
    private val idNominal: ArrayList<Int> = arrayListOf(0)
    private var btnKeteranganStatus: String = ""
    private var btnLaporanStatus: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.program_detail_activity)

        val sharedPreference: Preferences = Preferences(this)
        val imageProgram: ImageView = findViewById(R.id.toolbarImage)
        var allDonasi = findViewById<RecyclerView>(R.id.totalDonasiRecycler)
        val textJumlahDonatur: TextView = findViewById(R.id.jumlahDonasiTextProgram)

        //appbar background Image options
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

        //btn set program ke program FAvorit
        val btnFavoritSet: CardView = findViewById(R.id.btnAddFavorit)


        //deklarasi token user
        val retrivedToken: String? = sharedPreference.getValueString("TOKEN")

        //deklarasi value untuk konten halaman
        var idProgram: String? = ""
        var imgProgram: String? = ""
        var judulProgram: String? = ""
        var capaianProgram: String? = ""
        var sisaHariProgram: String? = ""
        var tanggalMulaiProgram: String? = ""
        var penggalangProgram: String? = ""
        var switchAnonimValue: String? = ""
        var progresProgramValue: Int? = 0
        var targetProgram: String? = ""
        var keyNotification: String? = "empty"

        if (intent.hasExtra("keyNotification")) {
            keyNotification = intent.getStringExtra("keyNotification")!!.toString()
        }

//        Toast.makeText(this, "keyNotification isinya : $keyNotification", Toast.LENGTH_SHORT).show()

        if (keyNotification == "donasi rutin") {
            sharedPreference.save("influencerId", "271")
        } else {
            sharedPreference.save("influencerId", "null")
        }

        idProgram = sharedPreference.getValueString("idProgram")
        imgProgram = sharedPreference.getValueString("img")
        judulProgram = sharedPreference.getValueString("judul")
        capaianProgram = sharedPreference.getValueString("capaian")
        sisaHariProgram = sharedPreference.getValueString("sisaHari")
        tanggalMulaiProgram = sharedPreference.getValueString("tanggalMulai")
        penggalangProgram = sharedPreference.getValueString("penggalang")
        switchAnonimValue = sharedPreference.getValueString("ANONIM")
        progresProgramValue = sharedPreference.getValueInt("progressProgram")
        targetProgram = sharedPreference.getValueString("targetProgram")

//        example condition firebase message
//        if (keyNotification == "donasi rutin") {
//            val toast = Toast.makeText(this, keyNotification, Toast.LENGTH_LONG)
//            toast.show()
//        } else {
//
//        }


        Glide.with(this).load(imgProgram).apply(options).into(imageProgram)
        //progresbar
        var progresBarProgram: ProgressBar = findViewById(R.id.progressBar)
        progresBarProgram.progress = progresProgramValue!!
        //judul
        var textTitle: TextView = findViewById(R.id.titleTextProgram)
        textTitle.text = judulProgram
        //capaian donasi
        var textCapaian: TextView = findViewById(R.id.capaianTextProgram)
        textCapaian.text = "Rp $capaianProgram"
        //target Donasi
        var textTargetCapain: TextView = findViewById(R.id.targetTextProgram)
        if (targetProgram == "100") {
            textTargetCapain.text = "\u221E"
        } else {
            val target = targetProgram.toString1()
            textTargetCapain.text = "Rp $target"
        }
        //sisa hari
        var textSisaHari: TextView = findViewById(R.id.sisaHariTextProgram)
        //tanggal mulai
        var textStart: TextView = findViewById(R.id.tanggalMulaiTextProgram)
        textStart.text = tanggalMulaiProgram
        if (sisaHariProgram == "Tidak terbatas") run {
            textSisaHari.text = "\u221E"
        } else {
            textSisaHari.text = sisaHariProgram
        }
        //penggalang program (nama komunitas)
        var textPenggalang: TextView = findViewById(R.id.kontenPenggalang)
        textPenggalang.text = penggalangProgram

        val toolbar: Toolbar = findViewById(R.id.upAppbarProgramDetail)
        toolbar.setNavigationOnClickListener {
            startActivity(
                Intent(
                    this@ProgramDetailActivity,
                    DashboardActivity::class.java
                )
            )
        }

        toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.programShare -> {
                    val shareIntent = Intent()
                    val pesan =
                        "Mari bersedekah melalui platform aksiberbagi, dengan aksiberbagi bersedekah jadi lebih mudah."
                    val url = sharedPreference.getValueString("urlProgram")
                    val urlProgram = "https://aksiberbagi.com/$url"
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, pesan)
                    shareIntent.putExtra(Intent.EXTRA_TEXT, urlProgram)
                    shareIntent.type = "text/plain"
                    shareIntent.addFlags(
                        Intent.FLAG_ACTIVITY_NO_HISTORY or
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                    )
                    startActivity(Intent.createChooser(shareIntent, "Bagikan kemulian bersedekah"))
                }
            }
            true
        })

        //deklarasi btn Donasi dan dialog swipe
        val btnDonasi: Button = findViewById(R.id.donasiBtn)
        val btnLihatDonatur: Button = findViewById(R.id.lihatSemuaDonasi)
        val dialogPembayaran = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val dialogLihatDonatur = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view: View = layoutInflater.inflate(R.layout.dialog_pembayaran_donasi, null)
        val viewDonatur: View = layoutInflater.inflate(R.layout.dialog_lihat_donatur, null)
        dialogPembayaran.setContentView(view)
        dialogLihatDonatur.setContentView(viewDonatur)
        //view dialog pembayaran donasi
        val textNominalLayout: TextInputLayout = view.findViewById(R.id.nominalDonasiLayout)
        val textNominalDonasi: TextInputEditText = view.findViewById(R.id.nominalDonasi)
        val textNoLayout: TextInputLayout = view.findViewById(R.id.noPembayaranLayout)
        val textNoTelepon: TextInputEditText = view.findViewById(R.id.noPembayaran)
        val textDoaDonatur: TextInputEditText = view.findViewById(R.id.doaDonatur)
        val btnDonasiClose: LinearLayout = view.findViewById(R.id.btnCollapse)
        val metodePembayaranContainer: LinearLayout =
            view.findViewById(R.id.metodePembayaranContainer)
        val btnPilihBayarC: TextView = view.findViewById(R.id.titleJenisPembayaran)
        val imgPembayaran: ImageView = view.findViewById(R.id.imgBank)
        val btnLanjutPembayaran: Button = view.findViewById(R.id.donasiLanjutPembayaran)
        val switchAnonimSet: Switch = view.findViewById(R.id.anonimDonasiBtn)
        val helperNominal: TextView = view.findViewById(R.id.helperNominal)
        //view dialog donasi donatur
        val btnCloseDialog: LinearLayout = viewDonatur.findViewById(R.id.btnCloseDialogDonatur)
        val initialisasiNominal = "0"
        textNominalDonasi.text = initialisasiNominal.toEditable()

        textNominalDonasi.addTextChangedListener(NonNumberFormaterDot(textNominalDonasi))


        if (switchAnonimValue == "T") {
            switchAnonimSet.isChecked = true
        }

        switchAnonimSet.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
            if (message == "Switch1:ON") {
                sharedPreference.save("ANONIM", "T")
            } else {
                sharedPreference.save("ANONIM", "F")
            }
        }

        //ambil semua atribut nilai yang di tampilkan dari database server
        val pilihNominal: Spinner = view.findViewById(R.id.spinerPilihNominal)

        getKoneksi(
            retrivedToken,
            idProgram,
            allDonasi,
            textJumlahDonatur,
            pilihNominal,
            this,
            viewDonatur
        )

        btnFavoritSet.setOnClickListener {
            postProgramFavorit(
                retrivedToken,
                idProgram,
                allDonasi,
                textJumlahDonatur,
                pilihNominal,
                this,
                viewDonatur
            )
        }

        Log.d(TAG, "value pada pilihan nominal $nominalItems")
        //set spinner untuk set keadaan dan nilai variabel yg terpengaruh spinner
        var spinnerPilihNominal: String? = ""
        var spinnerPilihanKeterangan: String? = ""
        var spinnerNominal: Int = 0
        pilihNominal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (nominalItems[p2] == "Masukkan Nominal Lain") {
                    spinnerPilihNominal = nominalItems[p2]
                    spinnerNominal = idNominal[p2]
                    show(textNominalLayout)
                } else {
                    gone(textNominalLayout)
                    spinnerPilihNominal = nominalItems[p2]
                    spinnerNominal = idNominal[p2]
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
        var idPembayaran: String? = sharedPreference.getValueString("idPembayaran")
        var pilihanPembayaran: String? = intent.getStringExtra("pilihanPembayaran")
        var idPilihan: String? = intent.getStringExtra("idPilihan")
        var imgPilihan: String? = intent.getStringExtra("imagePilihan")
        var tipeBank: String? = intent.getStringExtra("tipeBank")

        //input no ovo, link aja, dan dana
        gone(textNoLayout)
        if (idPilihan == "1001") {
            show(textNoLayout)
            textNoLayout.requestFocus()
            textNoLayout.requestFocus()
            textNoLayout.hint = "Masukkan No akun Link Aja"
        } else if (idPilihan == "1002") {
            show(textNoLayout)
            textNoLayout.requestFocus()
            textNoLayout.requestFocus()
            textNoLayout.hint = "Masukkan No akun Ovo"
        } else if (idPilihan == "1003") {
            show(textNoLayout)
            textNoLayout.requestFocus()
            textNoLayout.requestFocus()
            textNoLayout.hint = "Masukkan No akun DANA"
        }


        if (dialogPembayaranAktif == "true" && pilihanPembayaran != "") {
            dialogPembayaran.show()
            show(textNominalLayout)
            btnPilihBayarC.text = pilihanPembayaran
            show(imgPembayaran)
            Glide.with(this).load(imgPilihan).into(imgPembayaran)
            textNominalDonasi.text = nominalPembayaran?.toEditable()
        } else if (dialogPembayaranAktif == "true") {
            dialogPembayaran.show()
            textNominalDonasi.text = nominalPembayaran?.toEditable()
        } else {
            dialogPembayaran.dismiss()
        }

        btnDonasi.setOnClickListener {
            dialogPembayaran.show()
        }

        btnDonasiClose.setOnClickListener {
            dialogPembayaran.dismiss()
        }

        btnLihatDonatur.setOnClickListener {
            dialogLihatDonatur.show()
        }

        btnCloseDialog.setOnClickListener {
            dialogLihatDonatur.dismiss()
        }


        metodePembayaranContainer.setOnClickListener {
            try {
                if (spinnerPilihNominal == "Pilih Nominal Donasi") {
                    helperNominal.text = "Silahkan Pilih Nominal donasi"
                    helperNominal.setTextColor(Color.parseColor("#ed2a18"))
                } else {
                    var nominalCek = textNominalDonasi.text.toString1()
                    if (spinnerPilihNominal == "Masukkan Nominal Lain") {
                        if (nominalCek == "") {
                            nominalCek = "0"
                        }
                        val nominalRaw: Int = nominalCek.replace(".", "").replace(",00", "").toInt()
                        if (nominalRaw > 999) {
                            val mIntent = Intent(this, PilihPembayaranActivity::class.java)
                            val mBundle = Bundle()
                            mBundle.putString("nominal", textNominalDonasi.text.toString1())
                            mBundle.putString("spinnerValue", spinnerPilihNominal)
                            mBundle.putString("keyNotification", keyNotification)
                            mIntent.putExtras(mBundle)
                            startActivity(mIntent)
                        } else {
                            helperNominal.setTextColor(Color.parseColor("#ed2a18"))
                        }
                    } else {
                        val mIntent = Intent(this, PilihPembayaranActivity::class.java)
                        val mBundle = Bundle()
                        mBundle.putString("nominal", textNominalDonasi.text.toString1())
                        mBundle.putString("spinnerValue", spinnerPilihNominal)
                        mBundle.putString("keyNotification", keyNotification)
                        mIntent.putExtras(mBundle)
                        startActivity(mIntent)
                    }
                }
            } catch (e: Exception) {
                val sakkarep: TextView = view.findViewById(R.id.masukkanNominal)
                sakkarep.text = e.message.toString1();
            }
        }
        val doaBtn: Switch = view.findViewById(R.id.doaDonasiBtn)
        val inputLayoutDoa: TextInputLayout = view.findViewById(R.id.doaDonasi)
        var doaStatus = ""
        gone(inputLayoutDoa)
        doaBtn.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
            if (message == "Switch1:ON") {
                doaStatus = "True"
                show(inputLayoutDoa)
            } else {
                doaStatus = "False"
                gone(inputLayoutDoa)
                hideSoftKeyboard(this, inputLayoutDoa)
            }
        }

        //tampilan webview keterangan
        val myWebView: WebView = findViewById(R.id.keteranganProgram)
        myWebView.loadUrl("https://aksiberbagi.com/apk/keterangan/$idProgram")
        //btn lihat semua keterangan
        val btnKeteranganLihat: Button = findViewById(R.id.btnKeteranganProgram)
        val layoutKeterangan: LinearLayout = findViewById(R.id.layoutKeteranganDetail)
        btnKeteranganLihat.setOnClickListener {
            if (btnKeteranganStatus == "") {
                btnShowLayout(btnKeteranganLihat, layoutKeterangan)
            } else {
                btnHideLayout(btnKeteranganLihat, layoutKeterangan)
            }
        }

        // mengambil seluruh deklarasi data yang di perlukan untuk donasi
        var nominalDonasiDonatur = 0
        var doaDonatur = ""

        //klik button lanjut pembayaran post ke server
        btnLanjutPembayaran.setOnClickListener {
//            set deklarasi variabel yang di perlukan
            if (doaStatus == "True") doaDonatur = textDoaDonatur.text?.toString()!!

            if (spinnerNominal == 0) {
                if (textNominalDonasi.text!!.toString() == "") {
                    nominalDonasiDonatur = 0
                } else {
                    nominalDonasiDonatur = Integer.parseInt(
                        textNominalDonasi.text!!.toString().replace(
                            ".",
                            ""
                        ).replace(",00", "")
                    )
                }
            } else {
                nominalDonasiDonatur = spinnerNominal
            }
//            set validator untuk proses donasi
            if (nominalDonasiDonatur < 1000) {
                helperNominal.setTextColor(Color.parseColor("#ed2a18"))
                return@setOnClickListener
            } else {
                helperNominal.setTextColor(Color.parseColor("#86b4ba"))
            }

            if (idPembayaran == null) {
                btnPilihBayarC.setTextColor(Color.parseColor("#ed2a18"))
                return@setOnClickListener
            } else {
                if (nominalDonasiDonatur in 1000..9999 && tipeBank == "Transfer") {
                    btnPilihBayarC.setTextColor(Color.parseColor("#ed2a18"))
                    btnPilihBayarC.text = "Pembayaran Tidak Valid"
                    return@setOnClickListener
                }
                if (idPilihan == "1001") {
                    val noLinkAja = textNoTelepon.text?.toString()
                    sharedPreference.save("penggunaLinkAja", noLinkAja)
                } else if (idPilihan == "1002") {
                    val noOvo = textNoTelepon.text?.toString()
                    sharedPreference.save("penggunaOvo", noOvo)
                } else if (idPilihan == "1003") {
                    val noDana = textNoTelepon.text?.toString()
                    sharedPreference.save("penggunaDana", noDana)
                }

                sharedPreference.save("donasiProgramId", idProgram)
                sharedPreference.save("donasiPembayaranId", idPembayaran)
                sharedPreference.save("donasiNominal", nominalDonasiDonatur)
                sharedPreference.save("donasiDoa", doaDonatur)
                postDonasiDonatur(retrivedToken, textNoLayout)
            }
        }

//        deklarasi tombol share ke facebook dan whatsapp
        val btnShareFacebook: Button = findViewById(R.id.shareFacebook)
        btnShareFacebook.setOnClickListener {
            val shareIntent = Intent()
            val pesan =
                "Mari bersedekah melalui platform aksiberbagi, dengan aksiberbagi bersedekah jadi lebih mudah."
            val url = sharedPreference.getValueString("urlProgram")
            val urlProgram = "https://aksiberbagi.com/$url"
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, pesan)
            shareIntent.putExtra(Intent.EXTRA_TEXT, urlProgram)
            shareIntent.type = "text/plain"
            shareIntent.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            startActivity(Intent.createChooser(shareIntent, "Bagikan kemulian bersedekah"))
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun gone(view: View) {
        view.visibility = View.GONE
    }

    fun show(view: View) {
        view.visibility = View.VISIBLE
    }

    private fun hidden(view: View) {
        view.visibility = View.INVISIBLE
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun postDonasiDonatur(
        tokenValue: String?, inputLayout: TextInputLayout
    ) {
        val body = JSONObject()
        val sharedPreference: Preferences = Preferences(this)
        body.put("program_id", sharedPreference.getValueString("donasiProgramId"))
        body.put("metode_pembayaran_id", sharedPreference.getValueString("donasiPembayaranId"))
        body.put("nominal", sharedPreference.getValueInt("donasiNominal"))
        body.put("pesan_doa", sharedPreference.getValueString("donasiDoa"))
        body.put("hamba_Allah", sharedPreference.getValueString("ANONIM"))
        body.put("is_android", 1)
        body.put("no_wa", sharedPreference.getValueString("penggunaWA"))
        if( sharedPreference.getValueString("influencerId") != "null" ){
            body.put("influencer_id", sharedPreference.getValueString("influencerId")!!.toInt())
        }else{
            body.put("influencer_id",null)
        }
        body.put("email", sharedPreference.getValueString("penggunaEmail"))
        body.put("nama_donatur", sharedPreference.getValueString("penggunaNAMA"))
        body.put("kode_negara_no_hp", sharedPreference.getValueString("penggunaKodeNegara"))
        body.put("panggilan", sharedPreference.getValueString("penggunaPanggilan"))

        body.put("no_link_aja", sharedPreference.getValueString("penggunaLinkAja"))
        body.put("no_ovo", sharedPreference.getValueString("penggunaOvo"))
        body.put("no_dana", sharedPreference.getValueString("penggunaDana"))
        body.put("nominal_flash_sale_id", null)
        Log.d(TAG, " ini json datanya: $body");
//        return
        ApiService.postDonasi(tokenValue, body).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                if (response?.getString("message").equals("Donasi berhasil diproses")) {
                    val data: JSONObject? = response?.getJSONObject("data")
                    val eWallet = data?.getString("isEwallet")
                    if (eWallet == "false") {
                        val dataDonasi = data?.getJSONObject("donasi")
                        sharedPreference.save(
                            "invoiceNominal",
                            dataDonasi?.getString("tbldonasi_nominal")
                        )
                        sharedPreference.save(
                            "invoiceKodeUnik",
                            dataDonasi?.getString("tbldonasi_nourut")
                        )
                        sharedPreference.save(
                            "invoiceKode",
                            dataDonasi?.getString("tbldonasi_invoice")
                        )
                        val dataDonasiBank = dataDonasi?.getJSONObject("bank")
                        sharedPreference.save(
                            "invoiceBank",
                            dataDonasiBank?.getString("tblbank_nama")
                        )
                        sharedPreference.save(
                            "invoiceBankAN",
                            dataDonasiBank?.getString("tblbank_namapemilik")
                        )
                        sharedPreference.save(
                            "invoiceBankUrl",
                            dataDonasiBank?.getString("logo_url")
                        )
                        sharedPreference.save(
                            "invoiceBankRekening",
                            dataDonasiBank?.getString("tblbank_rekening")
                        )
                        val dataDonasiProgram = dataDonasi?.getJSONObject("program")
                        sharedPreference.save(
                            "invoiceProgramJudul",
                            dataDonasiProgram?.getString("tblprogram_judul")
                        )
                        startActivity(
                            Intent(
                                this@ProgramDetailActivity,
                                InvoiceActivity::class.java
                            )
                        )
                    } else {
                        val midtransStatus = data?.getString("midtrans")
                        val xenditStatus = data?.getString("xendit")
                        val faspayStatus = data?.getString("faspay")
                        var redirectUrl: String? = ""
                        if (midtransStatus == "null" && faspayStatus == "null") {
                            val xendit = data?.getJSONObject("xendit")
                            redirectUrl = xendit?.getString("redirect_url")
                        } else if (xenditStatus == "null" && faspayStatus == "null") {
                            val midtrans = data?.getJSONObject("midtrans")
                            redirectUrl = midtrans?.getString("redirect_url")
                        } else if (xenditStatus == "null" && midtransStatus == "null") {
                            val faspay = data?.getJSONObject("faspay")
                            redirectUrl = faspay?.getString("deeplink")
                        }
                        val dataDonasi = data?.getJSONObject("donasi")
                        sharedPreference.save("invoiceUrl", redirectUrl)
                        sharedPreference.save(
                            "invoiceNominal",
                            dataDonasi?.getString("tbldonasi_nominal")
                        )
                        sharedPreference.save(
                            "invoiceKodeUnik",
                            dataDonasi?.getString("tbldonasi_nourut")
                        )
                        sharedPreference.save(
                            "invoiceKode",
                            dataDonasi?.getString("tbldonasi_invoice")
                        )
                        val dataDonasiBank = dataDonasi?.getJSONObject("bank")
                        sharedPreference.save(
                            "invoiceBank",
                            dataDonasiBank?.getString("tblbank_nama")
                        )
                        sharedPreference.save(
                            "invoiceBankAN",
                            dataDonasiBank?.getString("tblbank_namapemilik")
                        )
                        sharedPreference.save(
                            "invoiceBankUrl",
                            dataDonasiBank?.getString("logo_url")
                        )
                        sharedPreference.save(
                            "invoiceBankRekening",
                            dataDonasiBank?.getString("tblbank_rekening")
                        )
                        val dataDonasiProgram = dataDonasi?.getJSONObject("program")
                        sharedPreference.save(
                            "invoiceProgramJudul",
                            dataDonasiProgram?.getString("tblprogram_judul")
                        )
                        startActivity(
                            Intent(
                                this@ProgramDetailActivity,
                                WebviewInvoiceActivity::class.java
                            )
                        )
                    }
                }
            }

            override fun onError(anError: ANError?) {
                val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                if (apiError?.message == "Silakan masukkan no ovo yang valid dan sudah terdaftar") {
                    val toast = Toast.makeText(
                        this@ProgramDetailActivity,
                        "Silakan masukkan no ovo yang valid dan sudah terdaftar",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    inputLayout.requestFocus()
                    inputLayout.boxStrokeColor = ContextCompat.getColor(
                        this@ProgramDetailActivity,
                        R.color.error_color
                    )
                    inputLayout.helperText =
                        "Silakan masukkan no ovo yang valid dan sudah terdaftar"
                }
                Log.d(TAG, "OnErrorProsesDonasiBody " + anError?.errorBody)
                Log.d(TAG, "OnErrorProsesDonasiCode " + anError?.errorCode)
                Log.d(TAG, "OnErrorProsesDonasiDetail " + anError?.errorDetail)
            }

        })
    }

    private fun postProgramFavorit(
        tokenValue: String?,
        idProgram: String?,
        donatur: RecyclerView,
        jumlahDonatur: TextView,
        spinner: Spinner,
        context: ProgramDetailActivity,
        donaturDialog: View
    ) {
        val body = JSONObject()
        val sharedPreference: Preferences = Preferences(this)
        body.put("program_id", idProgram)
        ApiService.postFavorit(tokenValue, body).getAsJSONObject(object :
            JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                if (response?.getString("message").equals("Program favorit berhasil disimpan")) {
                    val toast = Toast.makeText(
                        this@ProgramDetailActivity,
                        "Berhasil Menyimpan Program Favorit",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                } else {
                    val toast = Toast.makeText(
                        this@ProgramDetailActivity,
                        "Ada Kesalahan Sistem",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                }
            }

            override fun onError(anError: ANError?) {
                val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                if (apiError?.message == "Token expired") {
                    val donatur = context.findViewById<RecyclerView>(R.id.totalDonasiRecycler)
                    val jumlahDonatur = context.findViewById<TextView>(R.id.jumlahDonasiTextProgram)
                    refreshToken(
                        tokenValue,
                        idProgram,
                        donatur,
                        jumlahDonatur,
                        spinner,
                        context,
                        donaturDialog
                    )
                    val toast = Toast.makeText(
                        this@ProgramDetailActivity,
                        "Cobalah beberapa saat lagi",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                }
            }

        })

    }

    private fun getKoneksi(
        tokenValue: String?,
        idProgram: String?,
        donatur: RecyclerView,
        jumlahDonatur: TextView,
        spinner: Spinner,
        context: ProgramDetailActivity,
        donaturDialog: View
    ) {
        val header: String? = tokenValue
        ApiService.getKoneksi(header).getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                getDonaturProgramA(
                    tokenValue,
                    idProgram,
                    donatur,
                    jumlahDonatur,
                    context,
                    donaturDialog
                )
                getNominal(tokenValue, idProgram, spinner)
            }

            override fun onError(anError: ANError?) {
                val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                if (anError?.errorDetail!!.equals("connectionError")) {
                    val toast = Toast.makeText(
                        this@ProgramDetailActivity,
                        "Ada masalah dengan Koneksi Internet Anda",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    return
                } else {
                    refreshToken(
                        tokenValue,
                        idProgram,
                        donatur,
                        jumlahDonatur,
                        spinner,
                        context,
                        donaturDialog
                    )
                }
            }

        })
    }

    private fun refreshToken(
        tokenValue: String?,
        idProgram: String?,
        donatur: RecyclerView,
        jumlahDonatur: TextView,
        spinner: Spinner,
        context: ProgramDetailActivity,
        donaturDialog: View
    ) {
        val header: String? = tokenValue
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
                                getKoneksi(
                                    tokenValue,
                                    idProgram,
                                    donatur,
                                    jumlahDonatur,
                                    spinner,
                                    context,
                                    donaturDialog
                                )
                            }
                        } else if (response?.getString("message")
                                .equals("Token expired berhasil di refresh")
                        ) {
                            val token: String? = response?.getString("token")
                            if (token != null) {
                                sharedPreference.save("TOKEN", token)
                                getKoneksi(
                                    tokenValue,
                                    idProgram,
                                    donatur,
                                    jumlahDonatur,
                                    spinner,
                                    context,
                                    donaturDialog
                                )
                            }
                        } else {
                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                    val intent = Intent(
                                        this@ProgramDetailActivity,
                                        IntroActivity::class.java
                                    )
                                    startActivity(intent)
                                }, 2500)
                            }
                        }

                    } catch (e: JSONException) {
                        val toast = Toast.makeText(
                            this@ProgramDetailActivity,
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
                                this@ProgramDetailActivity,
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
        } catch (e: JSONException) {
            val toast = Toast.makeText(
                this,
                "Kesalahan Header",
                Toast.LENGTH_LONG
            )
            toast.show()
        }
    }


    private fun getDonaturProgramA(
        tokenValue: String?,
        idProgram: String?,
        donatur: RecyclerView,
        jumlahDonatur: TextView,
        context: ProgramDetailActivity,
        donaturDialog: View
    ) {
        ApiService.getDonatur(tokenValue, idProgram).getAsJSONObject(object :
            JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {

                val laporanLayoutKosong =
                    context.findViewById<LinearLayout>(R.id.kontenLaporanDetailA)
                val laporanLayoutAda = context.findViewById<LinearLayout>(R.id.kontenLaporanDetailB)
                val buttonLihatLaporan = context.findViewById<Button>(R.id.btnLaporanProgram)
                val webViewLaporan = context.findViewById<WebView>(R.id.laporanWeb)

                if (response?.getString("berita").equals("true")) {
                    laporanLayoutAda.visibility = View.VISIBLE
                    buttonLihatLaporan.visibility = View.VISIBLE
                    webViewLaporan.loadUrl("https://aksiberbagi.com/apk/berita/$idProgram")
                    buttonLihatLaporan.setOnClickListener {
                        if (btnLaporanStatus == "") {
                            btnShowLayout(buttonLihatLaporan, laporanLayoutAda)
                        } else {
                            btnHideLayout(buttonLihatLaporan, laporanLayoutAda)
                        }
                    }

                } else {
                    laporanLayoutKosong.visibility = View.VISIBLE
                }

                val jsonArray = response?.getJSONArray("data")
                val totalDonatur: String? = response?.getString("total_donatur")
                jumlahDonatur.text = "($totalDonatur)"
                if (jsonArray?.length()!! > 4) {
                    for (i in 0 until 5) {
                        val item = jsonArray.getJSONObject(i)
                        val img: String? = "https://aksiberbagi.com/assets/images/user_akber.png"
                        val namaDonatur: String? = item?.getString("tbldonatur_nama")
                        val donasiDonatur: Double? =
                            item?.getString("tbldonasi_nominal")!!.toDouble()
                        val doaDonatur: String? = item?.getString("tbldonasi_doa")
                        val waktuDonasi: String? = item?.getString("waktu_donasi")
                        arrayDonatur.add(
                            ListDonasi(
                                img,
                                namaDonatur,
                                donasiDonatur,
                                doaDonatur,
                                waktuDonasi
                            )
                        )
                    }
                    val myAdapterListDonasi = ListDonasiAdapter(
                        arrayDonatur,
                        applicationContext
                    )
                    donatur.layoutManager = LinearLayoutManager(applicationContext)
                    donatur.adapter = myAdapterListDonasi

                    for (i in 0 until jsonArray?.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val img: String? = "https://aksiberbagi.com/assets/images/user_akber.png"
                        val namaDonatur: String? = item?.getString("tbldonatur_nama")
                        val donasiDonatur: Double? =
                            item?.getString("tbldonasi_nominal")!!.toDouble()
                        val doaDonatur: String? = item?.getString("tbldonasi_doa")
                        val waktuDonasi: String? = item?.getString("waktu_donasi")
                        arrayDonaturDialog.add(
                            ListDonasi(
                                img,
                                namaDonatur,
                                donasiDonatur,
                                doaDonatur,
                                waktuDonasi
                            )
                        )
                    }
                    val myAdapterListDonasiDialog = ListDonasiAdapter(
                        arrayDonaturDialog,
                        applicationContext
                    )
                    val dialogTotalDonatur =
                        donaturDialog.findViewById<TextView>(R.id.dialogDonaturTextTotal)
                    dialogTotalDonatur.text = "Donasi ($totalDonatur)"
                    val recyclerViewDialog =
                        donaturDialog.findViewById<RecyclerView>(R.id.recycler200Donasi)
                    recyclerViewDialog.layoutManager = LinearLayoutManager(applicationContext)
                    recyclerViewDialog.adapter = myAdapterListDonasiDialog
                    val spinnerDialogDonatur =
                        donaturDialog.findViewById<Spinner>(R.id.spinerPilihJenis)
                    val adapterNominal = ArrayAdapter(
                        this@ProgramDetailActivity,
                        R.layout.list_pilih_program_dropdown,
                        dialogDonaturPilihan
                    )
                    spinnerDialogDonatur.adapter = adapterNominal
                }


            }

            override fun onError(anError: ANError?) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getNominal(tokenValue: String?, idProgram: String?, spinner: Spinner) {
        ApiService.getPilihanNominal(tokenValue, idProgram).getAsJSONObject(object :
            JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                val jsonArray = response?.getJSONArray("data")
                if (jsonArray?.length()!! > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val nominalRaw = item?.getString("tblnominal_nominal")!!.toDouble()
                        val nominal = Converter.rupiah(nominalRaw)
                        val keterangan = item?.getString("tblnominal_keterangan")
                        val pilihanAdapter = "$nominal $keterangan"
                        nominalItems.add(pilihanAdapter)
                        idNominal.add(nominalRaw!!.toInt())
                    }
                    var spinnerString: String? = intent.getStringExtra("spinner")
                    nominalItems.add("Masukkan Nominal Lain")
                    idNominal.add(0)
                    val adapterNominal = ArrayAdapter(
                        this@ProgramDetailActivity,
                        R.layout.list_pilih_program_dropdown,
                        nominalItems
                    )
                    spinner.adapter = adapterNominal

                    nominalItems.forEach {
                        if (spinnerString == it) {
                            spinner.setSelection(adapterNominal.getPosition(it))
                        }
                    }
                }
            }

            override fun onError(anError: ANError?) {
                val apiError: ApiError? = anError?.getErrorAsObject(ApiError::class.java)
                apiError?.message == "Nomor telepon telah terdaftar"
                Log.d(TAG, "OnErrorBody " + anError?.errorBody)
                Log.d(TAG, "OnErrorCode " + anError?.errorCode)
                Log.d(TAG, "OnErrorDetail " + anError?.errorDetail)
            }

        })
    }

    private fun hideSoftKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun btnShowLayout(btn: Button, layout: LinearLayout) {
        val params: ViewGroup.LayoutParams = layout.layoutParams
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        layout.layoutParams = params
        btn.text = "Sembunyikan"
        btnKeteranganStatus = "On"
        btnLaporanStatus = "On"
    }

    private fun btnHideLayout(btn: Button, layout: LinearLayout) {
        val params: ViewGroup.LayoutParams = layout.layoutParams
        params.height = 1000
        layout.layoutParams = params
        btn.text = "Lihat Semua"
        btnKeteranganStatus = ""
        btnLaporanStatus = ""
    }

    override fun onBackPressed() {
        val sharedPreference: Preferences = Preferences(this)
        val navigasi: String? = sharedPreference.getValueString("navigasi")
        if (navigasi == "Beranda") {
            startActivity(Intent(this@ProgramDetailActivity, DashboardActivity::class.java))
        } else if (navigasi == "ProgramAll") {
            startActivity(Intent(this@ProgramDetailActivity, ProgramAllActivity::class.java))
        } else if (navigasi == "donasiRutin") {
            startActivity(Intent(this@ProgramDetailActivity, DonasiRutinActivity::class.java))
        } else if (navigasi == "Favorit") {
            val mIntent = Intent(this, DashboardActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("favoritAktif", "true")
            mIntent.putExtras(mBundle)
            startActivity(mIntent)
        } else {
            startActivity(Intent(this@ProgramDetailActivity, DashboardActivity::class.java))
        }
    }

    //                        val toast = Toast.makeText(
////                            this@ProgramDetailActivity,
////                            "Oke Donasi Ewallet",
////                            Toast.LENGTH_LONG
////                        )
////                        toast.show()

}


