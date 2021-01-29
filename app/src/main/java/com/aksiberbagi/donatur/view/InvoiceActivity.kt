package com.aksiberbagi.donatur.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.services.Converter
import com.aksiberbagi.donatur.services.Preferences


class InvoiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invoice_activity)

        val sharedPreference: Preferences = Preferences(this)
        //deklarasi variabel yg dibutuhkan di judul invoice ambil dari preseference
        val invoiceNominal = sharedPreference.getValueString("invoiceNominal")
        var invoiceNominalFormated = Converter.ribuan(invoiceNominal?.toDouble())
        val invoiceNominalDonasi = sharedPreference.getValueInt("donasiNominal")
        var invoiceNominalDonasiFormated = Converter.ribuan(invoiceNominalDonasi.toDouble())
        val invoiceKodeUnik = sharedPreference.getValueString("invoiceKodeUnik")
        val invoiceKode = sharedPreference.getValueString("invoiceKode")
        val invoiceBank = sharedPreference.getValueString("invoiceBank")
        val invoiceBankAN = sharedPreference.getValueString("invoiceBankAN")
        val invoiceBankUrl = sharedPreference.getValueString("invoiceBankUrl")
        val invoiceBankRekening = sharedPreference.getValueString("invoiceBankRekening")
        val invoiceProgramJudul = sharedPreference.getValueString("invoiceProgramJudul")

        //deklarasi variable layout yang akan di set sesuai data invoice
        val invoiceNominalText : TextView = findViewById(R.id.invoiceNominal)
        invoiceNominalText.text = "Rp $invoiceNominalFormated"
        val invoiceNominalRawText : TextView = findViewById(R.id.invoiceNominalRaw)
        invoiceNominalRawText.text = "Rp $invoiceNominalDonasiFormated"
        val invoiceNominalKodeText : TextView = findViewById(R.id.invoiceNominalKode)
        invoiceNominalKodeText.text = invoiceKodeUnik
        val invoiceBankHeader: TextView = findViewById(R.id.invoiceBankHeader)
        invoiceBankHeader.text = "Pembayaran dilakukan ke rekening A.n $invoiceBankAN"
        val imageLogoBank: ImageView = findViewById(R.id.logoBank)
        Glide.with(this).load(invoiceBankUrl).into(imageLogoBank)
        val invoiceBankNamaText : TextView = findViewById(R.id.invoiceBankNama)
        invoiceBankNamaText.text = invoiceBank
        val invoiceKodeText: TextView = findViewById(R.id.invoiceKode)
        invoiceKodeText.text = invoiceKode
        val invoiceBankRekeningText : TextView = findViewById(R.id.invoiceRekening)
        invoiceBankRekeningText.text = invoiceBankRekening
        val invoiceProgramJudulText : TextView = findViewById(R.id.invoiceProgramJudul)
        invoiceProgramJudulText.text = invoiceProgramJudul

        //deklarasi btn copy to clipboard
        val btnSalinNominal : TextView = findViewById(R.id.salinBtnNominal)
        val btnSalinRekening : TextView = findViewById(R.id.salinBtnRekening)

        btnSalinNominal.setOnClickListener {
            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Berhasil Menyalin Nominal Transfer", invoiceNominal )
            clipboard.setPrimaryClip(clip)

            val toast = Toast.makeText(this@InvoiceActivity,"Nominal Transfer Disalin",Toast.LENGTH_LONG)
            toast.show()
        }

        btnSalinRekening.setOnClickListener {
            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Berhasil Menyalin Rekening Bank", invoiceBankRekening)
            clipboard.setPrimaryClip(clip)

            val toast = Toast.makeText(this@InvoiceActivity,"No Rekening Disalin",Toast.LENGTH_LONG)
            toast.show()
        }


    }

    override fun onBackPressed() {
        startActivity(Intent(this@InvoiceActivity, DashboardActivity::class.java))
    }
}