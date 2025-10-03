package com.cdkentertainment.mobilny_usos_enhanced

import com.cdkentertainment.mobilny_usos_enhanced.models.BackendDataSender
import com.cdkentertainment.mobilny_usos_enhanced.usos_installations.UsosAPI
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth10aService

object OAuthSingleton {
    private val apiKey: String = listOf(BuildConfig.ключчасть, BuildConfig.частьключа, BuildConfig.ключевойэлементключа,
        BuildConfig.пампарамбам, BuildConfig.приветмир).joinToString(separator = "")
    private val apiSecret: String = listOf(BuildConfig.секретныйсекрет, BuildConfig.абсолютныйключ, BuildConfig.ключевойабсолют,
        BuildConfig.покадорогоймир, BuildConfig.каждаяисторияимеетсвойконец).joinToString(separator = "")
    var baseUrl: String = "https://usosapps.amu.edu.pl/services/"
    val service: OAuth10aService = ServiceBuilder(apiKey)
        .apiSecret(apiSecret)
        .callback("mobile-usos-enhanced://login")
        .build(UsosAPI.instance())

    fun setTestAccessToken() {
        if (BuildConfig.testAccessSecret != "" && BuildConfig.testAccessToken != "") {
            BackendDataSender.oAuth1AccessToken = OAuth1AccessToken(BuildConfig.testAccessToken, BuildConfig.testAccessSecret)
        }
    }

    fun getTestAccessToken() {
        if (BackendDataSender.oAuth1AccessToken == null) {
            val requestToken = service.getRequestToken()
            val authUrl = service.getAuthorizationUrl(requestToken)
            println(authUrl)
            val pin = readln()
            BackendDataSender.oAuth1AccessToken = service.getAccessToken(requestToken, pin)
            println(BackendDataSender.oAuth1AccessToken?.token)
            println(BackendDataSender.oAuth1AccessToken?.tokenSecret)
        } else {
            println("Token istnieje")
        }
    }

    fun get(url: String): Map<String, String> {
        val result = LinkedHashMap<String, String>()
        val request = OAuthRequest(Verb.GET, baseUrl + url)
        service.signRequest(BackendDataSender.oAuth1AccessToken, request)
        try {
            val response = service.execute(request)
            result["response"] = response.body
            return result
        } catch(e: Exception) {
            result["error"] = e.message.toString()
            return result
        }
    }
}