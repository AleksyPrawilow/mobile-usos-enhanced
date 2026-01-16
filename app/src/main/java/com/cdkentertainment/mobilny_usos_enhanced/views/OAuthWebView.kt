package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@Composable
fun OAuthWebView(
    url: String,
) {
    val context = LocalContext.current

    LaunchedEffect(url) {
        val intent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
            .build()

        intent.launchUrl(context, url.toUri())
    }
}