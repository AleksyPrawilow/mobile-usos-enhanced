package com.cdkentertainment.mobilny_usos_enhanced.view_models

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cdkentertainment.mobilny_usos_enhanced.DatabaseSingleton
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton.service
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.UserDataSingleton
import com.cdkentertainment.mobilny_usos_enhanced.models.AuthResponse
import com.cdkentertainment.mobilny_usos_enhanced.models.BackendDataSender
import com.cdkentertainment.mobilny_usos_enhanced.models.GoogleAuthManager
import com.cdkentertainment.mobilny_usos_enhanced.models.OAuthModel
import com.github.scribejava.core.model.OAuth1RequestToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class LoginPageViewModel: ViewModel() {
    enum class LoginState {
        GOOGLE_AUTO_LOGIN,
        GOOGLE_LOGIN,
        USOS_AUTO_LOGIN,
        USOS_LOGIN,
        USOS_RETREIVING_REQUEST_TOKEN,
        USOS_RETREIVING_OAUTH_VERIFIER,
        USOS_OAUTH_VERIFIER,
        DATABASE_SAVING_SESSION,
        SUCCESS
    }
    var loginState: LoginState by mutableStateOf(LoginState.GOOGLE_AUTO_LOGIN)
    var errorMessage: String by mutableStateOf("")
    var oauthUrl: String by mutableStateOf("")
    var googleAuthManager: GoogleAuthManager? = null
    var requestToken: OAuth1RequestToken? = null
    val model: OAuthModel = OAuthModel()

    suspend fun tryGoogleAutoLogIn(context: Context, activity: Activity) {
        loginState = LoginState.USOS_AUTO_LOGIN
        return
        googleAuthManager = GoogleAuthManager(context)
        val response = googleAuthManager?.sessionExists()
        if (response is AuthResponse.Success) {
            loginState = LoginState.USOS_AUTO_LOGIN
        } else {
            Log.e("auth", "No session found")
            loginState = LoginState.GOOGLE_LOGIN
        }
    }

    fun loginGoogle(activity: Activity) {
        googleAuthManager?.loginGoogleUser(activity)
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

    suspend fun tryAutoLogin(context: Context) {
        if (OAuthSingleton.checkIfAccessTokenExists(context)) {
            loginState = LoginState.DATABASE_SAVING_SESSION
        } else {
            loginState = LoginState.USOS_LOGIN
        }
    }

    suspend fun saveUserSession() {
        //FIXME Przenieść do normalnego modelu
        val parser: Json = Json { ignoreUnknownKeys = true }
        val resp = BackendDataSender.get("Grades/TermIds").body
        UIHelper.termIds = parser.decodeFromString<List<String>>(resp)
        println(UIHelper.termIds)
        //FIXME End fixme
        loginState = LoginState.SUCCESS
        return
        if (DatabaseSingleton.updateUserSession(OAuthSingleton.userData!!.basicInfo.id)) {
            Log.e("debug", "Data stored in database")
            loginState = LoginState.SUCCESS
        } else {
            Log.e("debug", "Data not stored in database")
            errorMessage = "Nie udało się zapisać danych o użytkowniku."
            loginState = LoginState.USOS_LOGIN
        }
    }

    suspend fun authorize(context: Context) {
        loginState = LoginState.USOS_RETREIVING_REQUEST_TOKEN
        requestToken = model.getRequestToken()
        if (requestToken == null) {
            loginState = LoginState.USOS_LOGIN
            errorMessage = "Something went wrong"
            return
        }
        oauthUrl = service.getAuthorizationUrl(requestToken)
        loginState = LoginState.USOS_RETREIVING_OAUTH_VERIFIER
//        withContext(Dispatchers.Main) {
//            context.startActivity(
//                Intent(Intent.ACTION_VIEW, authUrl.toUri())
//            )
//        }
//        loginState = LoginState.USOS_OAUTH_VERIFIER
    }

    suspend fun onOAuthRedirect(token: String?, verifier: String?, context: Context) {
        if (token != null && verifier != null) {
            // Exchange for access token here
            getAccessToken(verifier, context)
        }
    }

    suspend fun getAccessToken(pin: String, context: Context) {
        withContext(Dispatchers.IO) {
            val result = model.getAccessToken(pin, requestToken!!)
            if (result.containsKey("success")) {
                UserDataSingleton.saveUserCredentials(context, OAuthSingleton.oAuth1AccessToken!!)
                OAuthSingleton.userData = OAuthSingleton.getUserData()
                if (OAuthSingleton.userData == null) {
                    loginState = LoginState.USOS_LOGIN
                    errorMessage = "Nie udało się pobrać dane o użytkowniku."
                    return@withContext
                }
                loginState = LoginState.DATABASE_SAVING_SESSION
            } else {
                loginState = LoginState.USOS_LOGIN
                requestToken = null
                errorMessage = result["fail"].toString()
            }
        }
    }
}