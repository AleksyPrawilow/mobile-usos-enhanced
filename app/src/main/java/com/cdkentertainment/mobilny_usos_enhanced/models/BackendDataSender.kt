package com.cdkentertainment.mobilny_usos_enhanced.models


import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.Base64

object BackendDataSender {
    private val developmentLogin: String = "admin"
    private val developmentPassword: String = "temp1234"
    private val developmentUrl: String = "http://10.0.2.2:8080"
    private val client = OkHttpClient()
    private val authHeader = "Basic " + Base64.getEncoder().encodeToString("$developmentLogin:$developmentPassword".toByteArray())
    private val parser: Json = Json{}
    private val mediaType =  "application/json; charset=utf-8".toMediaType()
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

    public suspend fun post(requestUrl: String, json: String): BackendResponse {
        return withContext(Dispatchers.IO){
            val requestUrl = "$developmentUrl/$requestUrl"
            val requestBody = json.toRequestBody(mediaType)

            val request = Request.Builder()
                .url(requestUrl)
                .header("Authorization", authHeader)
                .post(requestBody)
                .build()
            val resp = BackendResponse(0, "")
            client.newCall(request).execute().use {response ->
                resp.body = response.body?.string() ?: ""
                resp.statusCode = response.code
            }

            return@withContext resp
        }
    }
}
