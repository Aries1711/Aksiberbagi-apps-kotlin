package com.aksiberbagi.donatur.services

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

/**
 * Created by Shreekrishna on 12/14/2014.
 */
class NumberFormaterDot(var editText: TextInputEditText) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { }
    override fun afterTextChanged(s: Editable) {
        try {
            editText.removeTextChangedListener(this)
            val value = editText.text.toString()
//            val newValue = value.replace("0","")
//            for(i in 0 until 1){
//                editText.setText(newValue)
//            }
            if (value != null && value != "") {
                if (value.startsWith("0")) {
                    editText.setText("")
                }
                val str = editText.text.toString().replace(".","").replace(",00", "").toDouble()
                editText.text = getDotFormatedValue(str)
                editText.setSelection(editText.text.toString().length)
            }
            editText.addTextChangedListener(this)
            return
        } catch (ex: Exception) {
            ex.printStackTrace()
            editText.addTextChangedListener(this)
        }
    }

    companion object {

        fun getDotFormatedValue(value: Double): Editable? {
            var valueFormated = Converter.ribuan(value)
            return valueFormated.toEditable()
        }


        private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    }
}