package com.cdkentertainment.mobilny_usos_enhanced.view_models

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UserDataSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.OAuthModel
import com.github.scribejava.core.model.OAuth1RequestToken
import com.github.scribejava.core.oauth.OAuth10aService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OAuthViewModel(): ViewModel() {
    var context: Context? = null
    var errorMessage: String by mutableStateOf("")
    var requestToken: OAuth1RequestToken? = null
    var isCheckingConnection: Boolean by mutableStateOf(true)
    var authorized: Boolean by mutableStateOf(false)
    var gotRequestToken: Boolean by mutableStateOf(false)
    var oauthVerifierPin: String by mutableStateOf("")
    val service: OAuth10aService = OAuthSingleton.service
    val model: OAuthModel = OAuthModel()

    suspend fun getRequestToken(): OAuth1RequestToken? {
        return withContext(Dispatchers.IO) {
            return@withContext model.getRequestToken()
        }
    }

    suspend fun tryAutoLogin(): Boolean {
        isCheckingConnection = true
        if (OAuthSingleton.checkIfAccessTokenExists(context!!)) {
            authorized = true
            isCheckingConnection = false
            return true
        }
        isCheckingConnection = false
        return false
    }

    suspend fun authorize() {
        requestToken = getRequestToken()
        if (requestToken == null) {
            errorMessage = "Something went wrong"
            return
        }
        val authUrl = service.getAuthorizationUrl(requestToken)
        gotRequestToken = true
        withContext(Dispatchers.Main) {
            context?.startActivity(
                Intent(Intent.ACTION_VIEW, authUrl.toUri())
            )
        }
    }

    suspend fun getAccessToken(pin: String) {
        withContext(Dispatchers.IO) {
            val result = model.getAccessToken(pin, requestToken!!)
            if (result.containsKey("success")) {
                UserDataSingleton.saveUserCredentials(context!!, OAuthSingleton.oAuth1AccessToken!!)
                authorized = true
            } else {
                gotRequestToken = false
                requestToken = null
                errorMessage = result["fail"].toString()
            }
        }
    }
}