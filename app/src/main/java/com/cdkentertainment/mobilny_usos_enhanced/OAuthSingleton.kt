package com.cdkentertainment.mobilny_usos_enhanced

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.cdkentertainment.mobilny_usos_enhanced.models.HomePageModel
import com.cdkentertainment.mobilny_usos_enhanced.models.UserInfoClass
import com.cdkentertainment.mobilny_usos_enhanced.usos_installations.UsosAPI
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.exceptions.OAuthException
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.OAuthResponseException
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth10aService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.concurrent.ExecutionException

object OAuthSingleton {
    private val apiKey: String = listOf(BuildConfig.ключчасть, BuildConfig.частьключа, BuildConfig.ключевойэлементключа,
        BuildConfig.пампарамбам, BuildConfig.приветмир).joinToString(separator = "")
    private val apiSecret: String = listOf(BuildConfig.секретныйсекрет, BuildConfig.абсолютныйключ, BuildConfig.ключевойабсолют,
        BuildConfig.покадорогоймир, BuildConfig.каждаяисторияимеетсвойконец).joinToString(separator = "")

    var baseUrl: String = "https://usosapps.amu.edu.pl/services/"
    val service: OAuth10aService = ServiceBuilder(apiKey)
        .apiSecret(apiSecret)
        .build(UsosAPI.instance())
    var oAuth1AccessToken: OAuth1AccessToken? = null
    var userData: UserInfoClass? by mutableStateOf(null)

    fun setTestAccessToken() {
        if (BuildConfig.testAccessSecret != "" && BuildConfig.testAccessToken != "") {
            oAuth1AccessToken = OAuth1AccessToken(BuildConfig.testAccessToken, BuildConfig.testAccessSecret)
        }
    }

    fun getTestAccessToken() {
        if (oAuth1AccessToken == null) {
            val requestToken = service.getRequestToken()
            val authUrl = service.getAuthorizationUrl(requestToken)
            println(authUrl)
            val pin = readln()
            oAuth1AccessToken = service.getAccessToken(requestToken, pin)
            println(oAuth1AccessToken?.token)
            println(oAuth1AccessToken?.tokenSecret)
        } else {
            println("Token istnieje")
        }
    }

    suspend fun checkIfAccessTokenExists(context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            if (oAuth1AccessToken != null) {
                return@withContext !checkIfTokenExpired()
            }
            val accessToken: OAuth1AccessToken? = UserDataSingleton.readAccessToken(context)
            if (accessToken != null) {
                oAuth1AccessToken = accessToken
                return@withContext !checkIfTokenExpired()
            }
            return@withContext false
        }
    }

    suspend fun getUserData(): UserInfoClass? {
        val homePageModel: HomePageModel = HomePageModel()
        return withContext(Dispatchers.IO) {
            return@withContext homePageModel.fetchUserData()
        }
    }

    suspend fun checkIfTokenExpired(): Boolean {
        val homePageModel: HomePageModel = HomePageModel()
        return withContext(Dispatchers.IO) {
            userData = getUserData()
            Log.d("OAUTH", userData.toString())
            if (userData != null) {
                return@withContext false
            } else {
                return@withContext true
            }
        }
    }

    fun get(url: String): Map<String, String> {
        val result = LinkedHashMap<String, String>()
        val request = OAuthRequest(Verb.GET, baseUrl + url)
        service.signRequest(oAuth1AccessToken, request)
        try {
            val response = service.execute(request) //tutaj można zrobić weryfikację przez status code
            result["response"] = response.body
            return result
        } catch(e: InterruptedException) {
            result["interruptedException"] = e.message.toString()
            return result
        } catch (e: ExecutionException) {
            result["executionException"] = e.message.toString()
            return result
        } catch (e: IOException) {
            result["ioException"] = e.message.toString()
            return result
        } catch (e: OAuthException) {
            result["oauthException"] = e.message.toString()
            return result
        } catch (e: OAuthResponseException) {
            result["oauthResponseException"] = e.message.toString()
            return result
        }
    }
}