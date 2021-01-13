package com.example.yandexwebviewtest

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebSettings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWeb()
    }

    private fun initWeb() {
        val webView = webView
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        @SuppressLint("SetJavaScriptEnabled")
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = AppWebViewClient(this)
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        } else {
            CookieManager.getInstance().setAcceptCookie(true)
        }
        getUrl()?.let { webView.loadUrl(it) }
    }

    private fun getUrl(): String? {
        val sharedPreferences = getSharedPreferences(
            "SP_WEBVIEW_LAST_PAGE", MODE_PRIVATE
        )
        return sharedPreferences.getString("LAST_URL", "https://yandex.ru/")
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ -> this.finish() }
                .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }
    }
}