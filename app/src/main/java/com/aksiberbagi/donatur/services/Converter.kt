package com.aksiberbagi.donatur.services

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

class Converter {
    companion object {
        fun rupiah(number: Double?): String{
            val localeID =  Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            return numberFormat.format(number).toString()
        }

        fun ribuan(number: Double?): String{
            val kursIndonesia: DecimalFormat = DecimalFormat.getCurrencyInstance() as DecimalFormat
            val formatRp = DecimalFormatSymbols()

            formatRp.currencySymbol = ""
            formatRp.monetaryDecimalSeparator = ','
            formatRp.groupingSeparator = '.'

            kursIndonesia.decimalFormatSymbols = formatRp
            val valueFormated: String = kursIndonesia.format(number)
            return valueFormated
        }
    }
}