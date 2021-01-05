package com.droidinsight.catalog.activity

import android.annotation.SuppressLint
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import com.droidinsight.catalog.R
import com.droidinsight.catalog.util.ProgressDialogUtils
import com.first_love.constant.EXTRA_DATA
import com.first_love.constant.EXTRA_FLAG
import doBack
import doGone
import doVisible
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class WebViewActivity : AppCompatActivity() {

    private val loadedUrl: String?
        get() = intent.getStringExtra(EXTRA_DATA)
    private val heading: String?
        get() = intent.getStringExtra(EXTRA_FLAG)

    private var URL: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        init()
        initControl()
        loadWebView()
    }

    private fun initControl() {
        URL = loadedUrl
    }

    private fun init() {
        headingToolbar.text = heading
        backBtnToolbar.doBack()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView() {
        mWebView.settings.javaScriptEnabled = true   // Enable Javascript
        mWebView.webViewClient = MyWebViewClient()
        loadUrl(URL)
    }

    private fun loadUrl(url: String?) {
        ProgressDialogUtils().getInstance().showProgress(this)
        mWebView.doGone()
        url?.let { mWebView.loadUrl(it) }
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            url?.let { view!!.loadUrl(it) }
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            ProgressDialogUtils().getInstance().hideProgress()
            mWebView.doVisible()
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            val builder = AlertDialog.Builder(this@WebViewActivity)
            builder.setMessage("Error in loading page")
            builder.setPositiveButton("continue") { _, _ -> handler!!.proceed() }
            builder.setNegativeButton("cancel") { _, _ -> handler!!.cancel() }
            val dialog = builder.create()
            dialog.show()
        }
    }

}