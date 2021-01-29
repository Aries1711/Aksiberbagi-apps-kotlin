package com.aksiberbagi.donatur.services

import android.content.ActivityNotFoundException
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import com.aksiberbagi.donatur.view.WebviewAjuanActivity


open class WebviewUpload : WebChromeClient() {
    // reference to activity instance. May be unnecessary if your web chrome client is member class.
    private val myActivity: WebviewAjuanActivity? = null
    override fun onShowFileChooser(
        webView: WebView,
        filePathCallback: ValueCallback<Array<Uri>>,
        fileChooserParams: FileChooserParams
    ): Boolean {
        // make sure there is no existing message
        if (myActivity!!.uploadMessage != null) {
            myActivity.uploadMessage!!.onReceiveValue(null)
            myActivity.uploadMessage = null
        }
        myActivity.uploadMessage = filePathCallback
        val intent = fileChooserParams.createIntent()
        try {
            myActivity.startActivityForResult(intent, myActivity.REQUEST_SELECT_FILE)
        } catch (e: ActivityNotFoundException) {
            myActivity.uploadMessage = null
            Toast.makeText(myActivity, "Cannot open file chooser", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}