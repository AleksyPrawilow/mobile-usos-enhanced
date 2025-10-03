package com.cdkentertainment.mobilny_usos_enhanced.view_models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton.service
import com.cdkentertainment.mobilny_usos_enhanced.getLocalized
import com.cdkentertainment.mobilny_usos_enhanced.models.OAuthModel
import com.cdkentertainment.mobilny_usos_enhanced.models.SharedDataClasses
import com.github.scribejava.core.model.OAuth1RequestToken

class LoginPageViewModel: ViewModel() {
    enum class LoginState {
        USOS_AUTO_LOGIN,
        USOS_LOGIN,
        USOS_RETREIVING_REQUEST_TOKEN,
        USOS_RETREIVING_OAUTH_VERIFIER,
        LAST_STEPS,
        SUCCESS
    }
    var loginState: LoginState by mutableStateOf(LoginState.USOS_AUTO_LOGIN)
    var errorMessage: String by mutableStateOf("")
    var oauthUrl: String by mutableStateOf("")
    var requestToken: OAuth1RequestToken? = null
    val model: OAuthModel = OAuthModel()

    suspend fun tryAutoLogin(context: Context) {
        loginState = model.tryUsosAutoLogin(context)
    }

    suspend fun authorize(context: Context) {
        loginState = LoginState.USOS_RETREIVING_REQUEST_TOKEN
        try {
            requestToken = model.getRequestToken()
            oauthUrl = service.getAuthorizationUrl(requestToken)
            loginState = LoginState.USOS_RETREIVING_OAUTH_VERIFIER
        } catch (e: Exception) {
            val message: SharedDataClasses.LangDict = SharedDataClasses.LangDict(
                pl = "Nie udało się zalogować.",
                en = "Failed to login."
            )
            errorMessage = message.getLocalized(context)
            loginState = LoginState.USOS_LOGIN
            requestToken = null
        }
    }

    fun loginCancelled(context: Context) {
        val message: SharedDataClasses.LangDict = SharedDataClasses.LangDict(
            pl = "Nie udało się zalogować.",
            en = "Failed to login."
        )
        errorMessage = message.getLocalized(context)
        loginState = LoginState.USOS_LOGIN
        requestToken = null
    }

    suspend fun onOAuthRedirect(token: String?, verifier: String?, context: Context) {
        if (token != null && verifier != null) {
            getAccessToken(
                pin = verifier,
                context = context
            )
        } else {
            val message: SharedDataClasses.LangDict = SharedDataClasses.LangDict(
                pl = "Nie udało się zalogować.",
                en = "Failed to login."
            )
            loginState = LoginState.USOS_LOGIN
            errorMessage = message.getLocalized(context)
        }
    }

    suspend fun getAccessToken(pin: String, context: Context) {
        try {
            model.getAccessToken(pin, requestToken!!, context)
            loginState = LoginState.LAST_STEPS
        } catch (e: Exception) {
            val message: SharedDataClasses.LangDict = SharedDataClasses.LangDict(
                pl = "Nie udało się zalogować.",
                en = "Failed to login."
            )
            e.printStackTrace()
            errorMessage = message.getLocalized(context)
            loginState = LoginState.USOS_LOGIN
            requestToken = null
        }
    }

    suspend fun lastSteps(context: Context) {
        try {
            model.loadNecessaryData()
            loginState = LoginState.SUCCESS
        } catch (e: Exception) {
            val message: SharedDataClasses.LangDict = SharedDataClasses.LangDict(
                pl = "Nie udało się pobrać danych z serwera.",
                en = "Failed to fetch data from server."
            )
            errorMessage = message.getLocalized(context)
            loginState = LoginState.USOS_LOGIN
        }
    }
}