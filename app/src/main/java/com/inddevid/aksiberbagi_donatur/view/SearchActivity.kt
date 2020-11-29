package com.inddevid.aksiberbagi_donatur.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.inddevid.aksiberbagi_donatur.R

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        val btnBackSearch : Button = findViewById(R.id.backSearchBtn)
        val btnClearSearch : Button = findViewById(R.id.clearSearchField)
        val fieldSearch : EditText = findViewById(R.id.searchField)

        hidden(btnClearSearch)
        openSoftKeyboard(this, fieldSearch)

        btnClearSearch.setOnClickListener{
            fieldSearch.clear()
            hidden(btnClearSearch)
        }

        btnBackSearch.setOnClickListener{
            startActivity(Intent(this@SearchActivity, DashboardActivity::class.java))
        }

        fieldSearch.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                val query = fieldSearch.text.toString()
                if(query != ""){
                    show(btnClearSearch)
                }else{
                    hidden(btnClearSearch)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                hidden(btnClearSearch)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
    }

    fun hidden(view: View){
        view.visibility = View.INVISIBLE
    }

    fun show(view: View){
        view.visibility = View.VISIBLE
    }

    fun openSoftKeyboard(context: Context, view: View) {
        view.requestFocus()
        // open the soft keyboard
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    fun EditText.clear() {
        text.clear()
    }

}