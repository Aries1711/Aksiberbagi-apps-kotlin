package com.aksiberbagi.donatur.services

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

class NonNumberFormaterDot(var editText: TextInputEditText) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        try {
            editText.removeTextChangedListener(this)
            val value = editText.text.toString()
            if (value != null && value != "") {
                if (value.startsWith("0")) {
                    editText.setText("")
                }
            }
            editText.addTextChangedListener(this)
            return
        } catch (ex: Exception) {
            ex.printStackTrace()
            editText.addTextChangedListener(this)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
}