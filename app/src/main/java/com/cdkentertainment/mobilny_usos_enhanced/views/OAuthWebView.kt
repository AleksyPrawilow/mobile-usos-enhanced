package com.cdkentertainment.mobilny_usos_enhanced.views

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun OAuthWebView(
    url: String,
    onRedirect: (Uri) -> Unit
) {
    var webView: WebView? by remember { mutableStateOf(null) }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                this.clearHistory()
                this.clearCache(true)
                this.clearFormData()
                settings.javaScriptEnabled = true
                importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
                CookieManager.getInstance().removeAllCookies(null)
                CookieManager.getInstance().flush()
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
                webView = this
                loadUrl(url)
            }
        },
        update = { it.loadUrl(url) },
        modifier = Modifier.fillMaxSize()
    )

    DisposableEffect(Unit) {
        onDispose {
            webView?.apply {
                loadUrl("about:blank")
                clearHistory()
                removeAllViews()
                destroy()
            }
            webView = null
        }
    }
}