package com.aksiberbagi.donatur.services

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

class IdrFormater(var number : Double) {

    companion object{
        fun formatrupiah(number: Double): String {
            val localeID = Locale("IND", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID) as DecimalFormat
            val formatRp = DecimalFormatSymbols()

            formatRp.currencySymbol = ""
            formatRp.monetaryDecimalSeparator = ','
            formatRp.groupingSeparator = '.'

            numberFormat.decimalFormatSymbols = formatRp

            val formatrupiah = numberFormat.format(number)
            val split: List<String> = formatrupiah.split(",")
            val length = split[0].length
            return split[0].substring(0, 2) + ". " + split[0].substring(2, length)
        }
    }
}