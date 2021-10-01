package com.aksiberbagi.donatur.services

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

class NumberFormaterDotNew(var editText: TextInputEditText) : TextWatcher {
    private var setEditText : String = editText.text.toString().trim()
    override fun afterTextChanged(s: Editable?) { }

    override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) { }

    override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (s.toString() != setEditText){
            editText.removeTextChangedListener(this)
            val replace : String? = s.toString().replace(",00","").replace("Rp.", "")
            if (replace != null) {
                if (replace.isNotEmpty()){
                    setEditText = IdrFormater.formatrupiah(replace.toDouble())
                }
            }
            editText.text = setEditText.toEditable()
            editText.setSelection(setEditText.length)
            editText.addTextChangedListener(this)
        }
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}