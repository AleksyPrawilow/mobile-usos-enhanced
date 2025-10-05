package com.cdkentertainment.mobilny_usos_enhanced.models


import com.github.scribejava.core.model.OAuth1AccessToken
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
    private val parser: Json = Json { ignoreUnknownKeys = true }
    private val mediaType =  "application/json; charset=utf-8".toMediaType()
    var oAuth1AccessToken: OAuth1AccessToken? = null
    public data class BackendResponse (
        var statusCode: Int,
        var body: String?
    )

    private fun sendRequestToBackend(request: Request): BackendResponse {
        val apiCall = client.newCall(request).execute()
        val response = BackendResponse(apiCall.code, apiCall.body?.toString())
        return response
    }

    public suspend fun getWithAuthHeaders(requestUrl: String, pin: String, token: String, tokenSecret: String): BackendResponse {
        return withContext(Dispatchers.IO) {
            val requestUrl = "$developmentUrl/$requestUrl"
            val request = Request.Builder()
                .url(requestUrl)
                .header("Authorization", authHeader)
                .header("pin", pin)
                .header("token", token)
                .header("tokenSecret", tokenSecret)
                .build()

            return@withContext sendRequestToBackend(request)
        }
    }

    public suspend fun get(requestUrl: String): BackendResponse  {
        return withContext(Dispatchers.IO) {
             val requestUrl = "$developmentUrl/$requestUrl"
            val request = Request.Builder()
                .url(requestUrl)
                .header("Authorization", authHeader)
                .header("OAuth-Key", oAuth1AccessToken?.token ?: "")
                .header("OAuth-Secret", oAuth1AccessToken?.tokenSecret ?: "")
                .build()

            return@withContext sendRequestToBackend(request)
        }
    }

    public suspend fun postHeaders(requestUrl: String, json: String): BackendResponse {
        return withContext(Dispatchers.IO) {
            val requestUrl = "$developmentUrl/$requestUrl"
            val requestBody = json.toRequestBody(mediaType)

            val request = Request.Builder()
                .url(requestUrl)
                .header("Authorization", authHeader)
                .header("OAuth-Key", oAuth1AccessToken?.token ?: "")
                .header("OAuth-Secret", oAuth1AccessToken?.tokenSecret ?: "")
                .post(requestBody)
                .build()

            return@withContext sendRequestToBackend(request)
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

            return@withContext sendRequestToBackend(request)
        }
    }
}
