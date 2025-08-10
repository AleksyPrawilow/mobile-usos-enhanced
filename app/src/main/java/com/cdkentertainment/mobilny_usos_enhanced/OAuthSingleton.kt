package com.cdkentertainment.mobilny_usos_enhanced

import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.exceptions.OAuthException
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.OAuthResponseException
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth10aService
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

    fun get(url: String): Map<String, String> {
        val result = LinkedHashMap<String, String>()
        val request = OAuthRequest(Verb.GET, baseUrl + url)
        service.signRequest(oAuth1AccessToken, request)
        try {
            val response = service.execute(request)
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