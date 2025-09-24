package com.cdkentertainment.mobilny_usos_enhanced.models


import com.cdkentertainment.mobilny_usos_enhanced.BuildConfig
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.Base64

object BackendDataSender {
    private val developmentLogin: String = "admin"
    private val developmentPassword: String = "temp1234"
    private val developmentUrl: String = "http://10.0.2.2:8080"
    private val client = OkHttpClient()
    private val authHeader = "Basic " + Base64.getEncoder().encodeToString("$developmentLogin:$developmentPassword".toByteArray())
    public data class BackendResponse (
        var statusCode: Int,
        var body: String
    )

    public suspend fun get(requestUrl: String): BackendResponse  {
        return withContext(Dispatchers.IO) {
             val requestUrl = "$developmentUrl/$requestUrl"
            println(requestUrl)
            val request = Request.Builder()
                .url(requestUrl)
                .header("Authorization", authHeader)
                .header("OAuth-Key", OAuthSingleton.oAuth1AccessToken?.token ?: "")
                .header("OAuth-Secret", OAuthSingleton.oAuth1AccessToken?.tokenSecret ?: "")
                .build()

            println(authHeader)
            val resp = BackendResponse(0, "")
            client.newCall(request).execute().use { response ->
                println(response)
                resp.body = response.body?.string() ?: ""
                resp.statusCode = response.code
            }

            return@withContext resp
        }
    }
}
