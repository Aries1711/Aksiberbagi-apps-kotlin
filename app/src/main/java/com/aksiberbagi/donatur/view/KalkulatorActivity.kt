package com.aksiberbagi.donatur.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.presenter.DialogHasilKalkulatorZakat
import com.aksiberbagi.donatur.services.NumberFormaterDot

class KalkulatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kalkulator_activity)

        val toolbar: Toolbar = findViewById(R.id.upAppbarKalkulatorZakat)
        toolbar.title = "Kalkulator Zakat"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationOnClickListener{ startActivity(Intent(this@KalkulatorActivity, ZakatActivity::class.java))}

        val btnKalkulatorPenghasilan : FrameLayout = findViewById(R.id.btnKalkulatorPenghasilan)
        val layoutPenghasilan : LinearLayout = findViewById(R.id.kalkulatorPenghasilan)

        val btnKalkulatorPerdagangan : FrameLayout = findViewById(R.id.btnKalkulatorPerdagangan)
        val layoutPerdagangan : LinearLayout = findViewById(R.id.kalkulatorPerdagangan)

        val btnKalkulatorEmas : FrameLayout = findViewById(R.id.btnKalkulatorEmas)
        val layoutEmas : LinearLayout = findViewById(R.id.kalkulatorEmas)

        val btnKalkulatorTabungan : FrameLayout = findViewById(R.id.btnKalkulatorTabungan)
        val layoutTabungan : LinearLayout = findViewById(R.id.kalkulatorTabungan)

        layoutPenghasilan.visibility = View.VISIBLE

        btnKalkulatorPenghasilan.setOnClickListener {
            layoutPenghasilan.visibility = View.VISIBLE
            layoutPerdagangan.visibility = View.GONE
            layoutEmas.visibility = View.GONE
            layoutTabungan.visibility = View.GONE
        }

        btnKalkulatorPerdagangan.setOnClickListener {
            layoutPerdagangan.visibility = View.VISIBLE
            layoutPenghasilan.visibility = View.GONE
            layoutEmas.visibility = View.GONE
            layoutTabungan.visibility = View.GONE
        }

        btnKalkulatorEmas.setOnClickListener {
            layoutEmas.visibility = View.VISIBLE
            layoutPenghasilan.visibility = View.GONE
            layoutPerdagangan.visibility = View.GONE
            layoutTabungan.visibility = View.GONE
        }

        btnKalkulatorTabungan.setOnClickListener {
            layoutTabungan.visibility = View.VISIBLE
            layoutPenghasilan.visibility = View.GONE
            layoutEmas.visibility = View.GONE
            layoutPerdagangan.visibility = View.GONE
        }

        // initialisasi nilai semua edittext
        val initialisasiNominal = "0"

        // inisialisasi form atribut layoutPenghasilan
        val inputPenghasilanPokok : TextInputEditText = findViewById(R.id.penghasilanBulananInput)
        val inputBonusPenghasilan : TextInputEditText = findViewById(R.id.bonusPenghasilanInput)
        val inputPengeluaranPenghasilan : TextInputEditText = findViewById(R.id.pengeluaranPenghasilanInput)
        val btnSubmitPenghasilan : Button = findViewById(R.id.btnHitungPenghasilan)
        inputPenghasilanPokok.text = initialisasiNominal.toEditable()
        inputPenghasilanPokok.addTextChangedListener(NumberFormaterDot(inputPenghasilanPokok))
        inputBonusPenghasilan.text = initialisasiNominal.toEditable()
        inputBonusPenghasilan.addTextChangedListener(NumberFormaterDot(inputBonusPenghasilan))
        inputPengeluaranPenghasilan.text = initialisasiNominal.toEditable()
        inputPengeluaranPenghasilan.addTextChangedListener(NumberFormaterDot(inputPengeluaranPenghasilan))

        // inisialisasi form atribut layoutPerdagangan
        val inputModalPerdagangan : TextInputEditText = findViewById(R.id.perdaganganModalInput)
        val inputKeuntunganPerdagangan : TextInputEditText = findViewById(R.id.perdaganganKeuntunganInput)
        val inputPiutangPerdagangan : TextInputEditText = findViewById(R.id.perdaganganPiutangInput)
        val inputHutangPerdagangan : TextInputEditText = findViewById(R.id.perdaganganHutangInput)
        val inputKerugianPerdagangan : TextInputEditText = findViewById(R.id.perdaganganKerugianInput)
        val btnSubmitPerdagangan : Button = findViewById(R.id.btnHitungPerdagangan)
        inputModalPerdagangan.text = initialisasiNominal.toEditable()
        inputModalPerdagangan.addTextChangedListener(NumberFormaterDot(inputModalPerdagangan))
        inputKeuntunganPerdagangan.text = initialisasiNominal.toEditable()
        inputKeuntunganPerdagangan.addTextChangedListener(NumberFormaterDot(inputKeuntunganPerdagangan))
        inputPiutangPerdagangan.text = initialisasiNominal.toEditable()
        inputPiutangPerdagangan.addTextChangedListener(NumberFormaterDot(inputPiutangPerdagangan))
        inputHutangPerdagangan.text = initialisasiNominal.toEditable()
        inputHutangPerdagangan.addTextChangedListener(NumberFormaterDot(inputHutangPerdagangan))
        inputKerugianPerdagangan.text = initialisasiNominal.toEditable()
        inputKerugianPerdagangan.addTextChangedListener(NumberFormaterDot(inputKerugianPerdagangan))

        // inisialisasi form atribut layoutEmas
        val inputEmas : TextInputEditText =findViewById(R.id.emasInput)
        val btnSubmitEmas: Button = findViewById(R.id.btnHitungEmas)
        inputEmas.text = initialisasiNominal.toEditable()
        inputEmas.addTextChangedListener(NumberFormaterDot(inputEmas))

        // inisialisasi form atribut layoutTabungan
        val inputTabungan : TextInputEditText = findViewById(R.id.tabunganInput)
        val btnSubmitTabungan: Button = findViewById(R.id.btnHitungTabungan)
        inputTabungan.text = initialisasiNominal.toEditable()
        inputTabungan.addTextChangedListener(NumberFormaterDot(inputTabungan))

        btnSubmitPenghasilan.setOnClickListener {
            var penghasilanPokok = inputPenghasilanPokok.text.toString().replace(".", "").replace(",00", "")
            if (penghasilanPokok == ""){
                penghasilanPokok = "0"
            }
            var penghasilanBonus = inputBonusPenghasilan.text.toString().replace(".", "").replace(",00", "")
            if (penghasilanBonus == ""){
                penghasilanBonus = "0"
            }
            var penghasilanPengeluaran = inputPengeluaranPenghasilan.text.toString().replace(".", "").replace(",00", "")
            if (penghasilanPengeluaran == ""){
                penghasilanPengeluaran = "0"
            }
            var hasilAkhir: Int = 0
            hasilAkhir = ((penghasilanPokok.toInt() + penghasilanBonus.toInt() - penghasilanPengeluaran.toInt()) * 0.025).toInt()
            var hasilAwal: Int = 0
            hasilAwal= (penghasilanPokok.toInt() + penghasilanBonus.toInt() - penghasilanPengeluaran.toInt())
            var statusZakat = "Tidak wajib"
            if(hasilAwal >= 5720000){
                statusZakat = "Wajib Membayar"
            }
            var dialog = DialogHasilKalkulatorZakat("153", "Penghasilan", hasilAkhir.toString(), statusZakat)
            supportFragmentManager.let { dialog.show(it, "dialogDonasiZakat") }
        }
        
        btnSubmitPerdagangan.setOnClickListener{
            var modal = inputModalPerdagangan.text.toString().replace(".","").replace(",00", "")
            if(modal == ""){
                modal = "0"
            }

            var keuntungan = inputKeuntunganPerdagangan.text.toString().replace(".","").replace(",00", "")
            if(keuntungan == ""){
                keuntungan = "0"
            }

            var piutang = inputPiutangPerdagangan.text.toString().replace(".", "").replace(",00", "")
            if (piutang == ""){
                piutang = "0"
            }

            var hutang = inputHutangPerdagangan.text.toString().replace(".","").replace(",00", "")
            if (hutang == ""){
                hutang ="0"
            }

            var kerugian = inputKerugianPerdagangan.text.toString().replace(".","").replace(",00", "")
            if (kerugian == ""){
                kerugian = "0"
            }

            var hasilAwal = 0
            hasilAwal = (modal.toInt() + keuntungan.toInt()) - (hutang.toInt() + kerugian.toInt())
            var hasilAkhir = 0
            hasilAkhir = (hasilAwal * 0.025).toInt()
            var statusZakat = "Tidak wajib"
            if(hasilAwal >= 78200000){
                statusZakat = "Wajib Membayar"
            }

            var dialog = DialogHasilKalkulatorZakat("151", "Perdagangan", hasilAkhir.toString(), statusZakat)
            supportFragmentManager.let { dialog.show(it, "dialogDonasiZakat") }
        }

        btnSubmitEmas.setOnClickListener {
            var beratEmas = inputEmas.text.toString().replace(".","").replace(",00", "")
            if (beratEmas == ""){
                beratEmas = "0"
            }
            var statusZakat = "Tidak wajib"
            if (beratEmas.toInt() >= 85 ){
                statusZakat = "Wajib Membayar"
            }
            var hasilAkhir = 0
            hasilAkhir = ((beratEmas.toInt() * 920000) * 0.025).toInt()
            var dialog = DialogHasilKalkulatorZakat("152", "Emas dan Perak", hasilAkhir.toString(), statusZakat)
            supportFragmentManager.let { dialog.show(it, "dialogDonasiZakat") }
        }

        btnSubmitTabungan.setOnClickListener {
            var tabungan = inputTabungan.text.toString().replace(".", "").replace(",00", "")
            if (tabungan == ""){
                tabungan = "0"
            }

            var statusZakat = "Tidak wajib"
            if (tabungan.toInt() >= 78200000 ){
                statusZakat = "Wajib Membayar"
            }
            var hasilAkhir = 0
            hasilAkhir = (tabungan.toInt() * 0.0025).toInt()
            var dialog = DialogHasilKalkulatorZakat("150", "Tabungan", hasilAkhir.toString(), statusZakat)
            supportFragmentManager.let { dialog.show(it, "dialogDonasiZakat") }

        }



    }

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
}