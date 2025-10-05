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
    private val developmentUrl: String = "http://10.0.2.2:8080"
    private val client = OkHttpClient()
    private var authHeader: String? = null
    private val mediaType =  "application/json; charset=utf-8".toMediaType()
    var oAuth1AccessToken: OAuth1AccessToken? = null
    public data class BackendResponse (
        var statusCode: Int,
        var body: String?
    )

    private fun sendRequestToBackend(request: Request): BackendResponse {
        var code: Int = 0
        var body: String? = null
        val apiCall = client.newCall(request).execute().use { response ->
            code = response.code
            body = response.body.toString()
        }
        return BackendResponse(code, body)
    }
    public fun setAuthHeader(accessToken: String) {
        authHeader = "Bearer $accessToken"
    }

    public suspend fun getWithoutHeaders(requestUrl: String): BackendResponse {
        return withContext(Dispatchers.IO) {
            val requestUrl = "$developmentUrl/$requestUrl"
            val request = Request.Builder()
                .url(requestUrl)
                .build()
            return@withContext sendRequestToBackend(request)
        }
    }
    public suspend fun getWithAuthHeaders(requestUrl: String, pin: String, token: String, tokenSecret: String): BackendResponse {
        return withContext(Dispatchers.IO) {
            val requestUrl = "$developmentUrl/$requestUrl"
            val request = Request.Builder()
                .url(requestUrl)
                .header("pin", pin)
                .header("token", token)
                .header("tokenSecret", tokenSecret)
                .build()

            return@withContext sendRequestToBackend(request)
        }
    }

    public suspend fun get(requestUrl: String): BackendResponse  {
        return withContext(Dispatchers.IO) {
            if (authHeader != null && oAuth1AccessToken != null) {
                val requestUrl = "$developmentUrl/$requestUrl"
                val accessToken = oAuth1AccessToken!!.token
                val accessSecret = oAuth1AccessToken!!.tokenSecret

                val request = Request.Builder()
                    .url(requestUrl)
                    .header("Authorization", authHeader!!)
                    .header("OAuth-Key", accessToken)
                    .header("OAuth-Secret", accessSecret)
                    .build()

                return@withContext sendRequestToBackend(request)
            } else {
                throw(IllegalStateException("Missing Authentication"))
            }
        }
    }

    public suspend fun postHeaders(requestUrl: String, json: String): BackendResponse {
        return withContext(Dispatchers.IO) {
            if (oAuth1AccessToken != null && authHeader != null) {
                val requestUrl = "$developmentUrl/$requestUrl"
                val accessToken = oAuth1AccessToken!!.token
                val accessSecret = oAuth1AccessToken!!.tokenSecret
                val requestBody = json.toRequestBody(mediaType)

                val request = Request.Builder()
                    .url(requestUrl)
                    .header("Authorization", authHeader?: "")
                    .header("OAuth-Key", accessToken)
                    .header("OAuth-Secret", accessSecret)
                    .post(requestBody)
                    .build()

                return@withContext sendRequestToBackend(request)
            } else {
                throw(IllegalStateException("Missing authentication"))
            }
        }
    }

    public suspend fun post(requestUrl: String, json: String): BackendResponse {
        return withContext(Dispatchers.IO){
            if (authHeader != null) {
                val requestUrl = "$developmentUrl/$requestUrl"
                val requestBody = json.toRequestBody(mediaType)
                val request = Request.Builder()
                    .url(requestUrl)
                    .header("Authorization", authHeader!!)
                    .post(requestBody)
                    .build()

                return@withContext sendRequestToBackend(request)
            } else {
                throw(IllegalStateException("Missing authentication"))
            }
        }
    }
}
