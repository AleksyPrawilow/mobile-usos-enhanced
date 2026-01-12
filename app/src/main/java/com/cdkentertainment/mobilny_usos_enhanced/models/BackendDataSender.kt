package com.cdkentertainment.mobilny_usos_enhanced.models


import android.util.Base64
import com.github.scribejava.core.model.OAuth1AccessToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object BackendDataSender {
    private val developmentUrl: String = "http://91.228.196.42"//"http://10.0.2.2:8080"
    private val client = OkHttpClient()
    private var authHeader: String? = null
    private val mediaType =  "application/json; charset=utf-8".toMediaType()
    var oAuth1AccessToken: OAuth1AccessToken? = null
    public data class BackendResponse (
        var statusCode: Int,
        var body: String?
    )
    private fun decodeJwtPayload(token: String): JSONObject? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null

            val payload = parts[1]
            val decodedBytes = Base64.decode(
                payload,
                Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
            )

            val json = String(decodedBytes, Charsets.UTF_8)
            JSONObject(json)
        } catch (e: Exception) {
            null
        }
    }
    private fun isJwtExpiringSoon(token: String?, bufferSeconds: Long = 15): Boolean {
        if (token == null) {
            return true
        }
        val payload = decodeJwtPayload(token) ?: return true
        val exp = payload.optLong("exp", 0L)

        val now = System.currentTimeMillis() / 1000
        return now + bufferSeconds >= exp
    }
    private fun refreshToken() {
        val oauth = oAuth1AccessToken ?: throw IllegalStateException("Missing OAuth token")

        val request = Request.Builder()
            .url("$developmentUrl/Auth/AutoLogin")
            .header("OAuth-Key", oauth.token)
            .header("OAuth-Secret", oauth.tokenSecret)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("Refresh failed: ${response.code}")
            }
            val token = response.body?.string() ?: throw Exception("Empty token")
            setAuthHeader(token)
        }
    }
    private suspend fun sendRequestToBackend(request: Request): BackendResponse = withContext(Dispatchers.IO) {
        try {
            client.newCall(request).execute().use { response ->
                val body = response.body?.string().takeUnless { it.isNullOrEmpty() }
                BackendResponse(response.code, body)
            }
        } catch (e: Exception) {
            BackendResponse(500, null)
        }
    }
    public fun setAuthHeader(accessToken: String) {
        authHeader = "Bearer $accessToken"
    }
    public suspend fun getWithoutHeaders(requestUrl: String, authorization: Boolean = true): BackendResponse {
        return withContext(Dispatchers.IO) {
            if (isJwtExpiringSoon(authHeader) && authorization) {
                refreshToken()
            }
            val requestUrl = "$developmentUrl/$requestUrl"
            val request = Request.Builder()
                .url(requestUrl)
                .build()
            return@withContext sendRequestToBackend(request)
        }
    }
    public suspend fun getWithOnlyAuthHeaders(requestUrl: String): BackendResponse {
        return withContext(Dispatchers.IO) {
            if (isJwtExpiringSoon(authHeader)) {
                refreshToken()
            }
            val accessToken = oAuth1AccessToken!!.token
            val accessSecret = oAuth1AccessToken!!.tokenSecret
            val requestUrl = "$developmentUrl/$requestUrl"
            val request = Request.Builder()
                .url(requestUrl)
                .header("OAuth-Key", accessToken)
                .header("OAuth-Secret", accessSecret)
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
                if (isJwtExpiringSoon(authHeader)) {
                    refreshToken()
                }
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
                if (isJwtExpiringSoon(authHeader)) {
                    refreshToken()
                }
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
    public suspend fun delete(requestUrl: String): BackendResponse {
        return withContext(Dispatchers.IO){
            if (authHeader != null) {
                val requestUrl = "$developmentUrl/$requestUrl"
                val accessToken = oAuth1AccessToken!!.token
                val accessSecret = oAuth1AccessToken!!.tokenSecret
                val request = Request.Builder()
                    .url(requestUrl)
                    .header("Authorization", authHeader!!)
                    .header("OAuth-Key", accessToken)
                    .header("OAuth-Secret", accessSecret)
                    .delete()
                    .build()
                return@withContext sendRequestToBackend(request)
            } else {
                throw(IllegalStateException("Missing authentication"))
            }
        }
    }
    public suspend fun patch(requestUrl: String, json: String): BackendResponse {
        return withContext(Dispatchers.IO) {
            if (oAuth1AccessToken != null && authHeader != null) {
                if (isJwtExpiringSoon(authHeader)) {
                    refreshToken()
                }
                val requestUrl = "$developmentUrl/$requestUrl"
                val accessToken = oAuth1AccessToken!!.token
                val accessSecret = oAuth1AccessToken!!.tokenSecret
                val requestBody = json.toRequestBody(mediaType)

                val request = Request.Builder()
                    .url(requestUrl)
                    .header("Authorization", authHeader?: "")
                    .header("OAuth-Key", accessToken)
                    .header("OAuth-Secret", accessSecret)
                    .patch(requestBody)
                    .build()

                return@withContext sendRequestToBackend(request)
            } else {
                throw(IllegalStateException("Missing authentication"))
            }
        }
    }
}
