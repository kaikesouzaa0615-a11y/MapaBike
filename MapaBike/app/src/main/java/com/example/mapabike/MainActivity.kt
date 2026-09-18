package com.example.mapabike

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var web: WebView
    private var pendingDestination: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pendingDestination = extractDestination(intent)

        web = WebView(this)
        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.settings.setGeolocationEnabled(true)
        web.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                pendingDestination?.let { sendDestinationToWeb(it) }
                pendingDestination = null
            }
        }
        web.webChromeClient = object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback?
            ) {
                callback?.invoke(origin, true, false)
            }
        }
        setContentView(web)

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                10
            )
        }

        web.loadUrl("file:///android_asset/index.html")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        val destination = intent?.let { extractDestination(it) }
        if (!destination.isNullOrBlank()) {
            if (::web.isInitialized) sendDestinationToWeb(destination) else pendingDestination = destination
        }
    }

    private fun sendDestinationToWeb(destination: String) {
        val escaped = destination.replace("\\", "\\\\").replace("'", "\\'")
        web.post {
            web.evaluateJavascript("window.setDestinationFromIntent('$escaped')", null)
        }
        Toast.makeText(this, "Destino recebido: $destination", Toast.LENGTH_SHORT).show()
    }

    private fun extractDestination(intent: Intent): String? {
        val data: Uri? = intent.data
        val action = intent.action

        if (Intent.ACTION_VIEW == action && data != null) {
            val scheme = data.scheme?.lowercase()
            if (scheme == "geo") {
                val q = data.getQueryParameter("q")
                if (!q.isNullOrBlank()) return q
                val ssp = data.schemeSpecificPart?.substringBefore("?")
                if (!ssp.isNullOrBlank() && !ssp.contains("0.0,0.0")) return ssp
            }
            if (scheme == "google.navigation") {
                val q = data.getQueryParameter("q")
                if (!q.isNullOrBlank()) return q
                val raw = data.schemeSpecificPart?.substringBefore("?")
                if (!raw.isNullOrBlank()) return raw
            }
            if (scheme == "http" || scheme == "https") {
                val q = data.getQueryParameter("q")
                    ?: data.getQueryParameter("query")
                    ?: data.getQueryParameter("address")
                if (!q.isNullOrBlank()) return q
                return data.toString()
            }
        }

        val text = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (!text.isNullOrBlank()) return text
        return null
    }
}
