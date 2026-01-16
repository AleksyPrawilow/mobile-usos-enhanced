package com.cdkentertainment.bux.view_models

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cdkentertainment.bux.UserDataSingleton
import com.cdkentertainment.bux.getLocalized
import com.cdkentertainment.bux.models.BackendDataSender
import com.cdkentertainment.bux.models.OAuthModel
import com.cdkentertainment.bux.models.SharedDataClasses
import com.github.scribejava.core.model.OAuth1RequestToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginPageViewModel: ViewModel() {
    enum class LoginState {
        SELECT_UNIVERSITY,
        USOS_AUTO_LOGIN,
        USOS_LOGIN,
        USOS_RETREIVING_REQUEST_TOKEN,
        USOS_RETREIVING_OAUTH_VERIFIER,
        USOS_RETREIVING_ACCESS_TOKEN,
        LAST_STEPS,
        SUCCESS
    }
    var loginState: LoginState by mutableStateOf(LoginState.USOS_AUTO_LOGIN)
    var errorMessage: String by mutableStateOf("")
    var oauthUrl: String by mutableStateOf("")
    var requestToken: OAuth1RequestToken? = null
    val model: OAuthModel = OAuthModel()

    private val _redirects = MutableSharedFlow<Uri>(
        extraBufferCapacity = 1
    )
    val redirects = _redirects.asSharedFlow()

    fun handleRedirect(uri: Uri) {
        _redirects.tryEmit(uri)
    }

    suspend fun readTokenAndUniversity(context: Context) {
        BackendDataSender.oAuth1AccessToken = UserDataSingleton.readAccessToken(context)
        if (UserDataSingleton.selectedUniversity == 0) {
            delay(4000)
            loginState = LoginState.SELECT_UNIVERSITY
        }
    }

    fun tryAutoLogin(context: Context) {
        viewModelScope.launch {
            loginState = model.tryUsosAutoLogin(context)
        }
    }

    suspend fun authorize(context: Context) {
        loginState = LoginState.USOS_RETREIVING_REQUEST_TOKEN
        try {
            val urlAndTokens = model.getRequestToken()
            requestToken = OAuth1RequestToken(urlAndTokens.token, urlAndTokens.tokenSecret)
            oauthUrl = urlAndTokens.url
            loginState = LoginState.USOS_RETREIVING_OAUTH_VERIFIER
        } catch (e: Exception) {
            e.printStackTrace()
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
            loginState = LoginState.USOS_RETREIVING_ACCESS_TOKEN
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
            model.getAccessToken(pin, requestToken!!.token, requestToken!!.tokenSecret,  context)
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