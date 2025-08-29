package com.cdkentertainment.mobilny_usos_enhanced.view_models

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton.service
import com.cdkentertainment.mobilny_usos_enhanced.UserDataSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.AuthResponse
import com.cdkentertainment.mobilny_usos_enhanced.models.GoogleAuthManager
import com.cdkentertainment.mobilny_usos_enhanced.models.OAuthModel
import com.github.scribejava.core.model.OAuth1RequestToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

class LoginPageViewModel: ViewModel() {
    enum class LoginState {
        GOOGLE_AUTO_LOGIN,
        GOOGLE_LOGIN,
        USOS_AUTO_LOGIN,
        USOS_LOGIN,
        USOS_RETREIVING_REQUEST_TOKEN,
        USOS_OAUTH_VERIFIER,
        SUCCESS
    }
    var loginState: LoginState by mutableStateOf(LoginState.GOOGLE_AUTO_LOGIN)
    var errorMessage: String by mutableStateOf("")
    var googleAuthManager: GoogleAuthManager? = null
    var requestToken: OAuth1RequestToken? = null
    val model: OAuthModel = OAuthModel()

    suspend fun tryGoogleAutoLogIn(context: Context) {
        googleAuthManager = GoogleAuthManager(context)
        val response = googleAuthManager?.sessionExists()
        if (response is AuthResponse.Success) {
            loginState = LoginState.USOS_AUTO_LOGIN
        } else {
            Log.e("auth", "No session found")
            loginState = LoginState.GOOGLE_LOGIN
        }
    }

    fun loginGoogle() {
        googleAuthManager?.loginGoogleUser()
            ?.onEach { result ->
                if (result is AuthResponse.Success) {
                    Log.d("auth", "Google Success")
                    loginState = LoginState.USOS_AUTO_LOGIN
                } else {
                    Log.e("auth", "Google Failed")
                }
            }
            ?.launchIn(viewModelScope)
    }

    suspend fun tryAutoLogin(context: Context): Boolean {
        if (OAuthSingleton.checkIfAccessTokenExists(context)) {
            loginState = LoginState.SUCCESS
            return true
        }
        loginState = LoginState.USOS_LOGIN
        return false
    }

    suspend fun authorize(context: Context) {
        loginState = LoginState.USOS_RETREIVING_REQUEST_TOKEN
        requestToken = model.getRequestToken()
        if (requestToken == null) {
            loginState = LoginState.USOS_LOGIN
            errorMessage = "Something went wrong"
            return
        }
        val authUrl = service.getAuthorizationUrl(requestToken)
        withContext(Dispatchers.Main) {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, authUrl.toUri())
            )
        }
        loginState = LoginState.USOS_OAUTH_VERIFIER
    }

    suspend fun getAccessToken(pin: String, context: Context) {
        withContext(Dispatchers.IO) {
            val result = model.getAccessToken(pin, requestToken!!)
            if (result.containsKey("success")) {
                UserDataSingleton.saveUserCredentials(context, OAuthSingleton.oAuth1AccessToken!!)
                loginState = LoginState.SUCCESS
            } else {
                loginState = LoginState.USOS_LOGIN
                requestToken = null
                errorMessage = result["fail"].toString()
            }
        }
    }
}