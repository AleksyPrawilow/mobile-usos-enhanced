package com.cdkentertainment.mobilny_usos_enhanced.views

import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun OAuthWebView(
    url: String,
    onRedirect: (Uri) -> Unit
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val redirectUri = request?.url
                        if (redirectUri != null &&
                            redirectUri.scheme == "mobile-usos-enhanced" &&
                            redirectUri.host == "login") {
                            onRedirect(redirectUri)
                            return true
                        }
                        return false
                    }
                }
                loadUrl(url)
            }
        },
        update = { it.loadUrl(url) },
        modifier = Modifier.fillMaxSize()
    )
}