package com.example.yandexwebviewtest

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity


class AppWebViewClient(private val context: Context) : WebViewClient() {

    //For Api >= 24
    @TargetApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        val uri = request.url
        return handleUri(view, uri)
    }

    //For Api < 24
    override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
        val uri = Uri.parse(url)
        return handleUri(view, uri)
    }

    private fun handleUri(view: WebView, uri: Uri): Boolean {
        return when {
            uri.toString().startsWith("https://yandex.ru/maps") -> {
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(context, intent, null)
                true
            }
            uri.toString().startsWith("https://yandex.ru/pogoda") -> {
                val packageManager = context.packageManager
                val intent = packageManager.getLaunchIntentForPackage("ru.yandex.weatherplugin")
                if (intent != null) {
                    Log.d("TEST_OF", "Success")
                    startActivity(context, intent, null)
                    true
                } else {
                    Log.d("TEST_OF", "Error")
                    view.loadUrl(uri.toString())
                    true
                }
            }
            else -> {
                view.loadUrl(uri.toString())
                true
            }
        }
    }

    private fun saveUrl(url: String) {
        val sharedPreferences = context.getSharedPreferences(
            "SP_WEBVIEW_LAST_PAGE",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putString("LAST_URL", url)
        editor.apply()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        if (url != null) {
            saveUrl(url)
        }
    }
}