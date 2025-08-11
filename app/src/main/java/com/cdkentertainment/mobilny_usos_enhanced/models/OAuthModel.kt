package com.cdkentertainment.mobilny_usos_enhanced.models

import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.github.scribejava.core.exceptions.OAuthException
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuth1RequestToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.concurrent.ExecutionException

class OAuthModel {
    suspend fun getRequestToken(): OAuth1RequestToken? {
        return withContext(Dispatchers.IO) {
            try {
                val token: OAuth1RequestToken = OAuthSingleton.service.getRequestToken()
                return@withContext token
            } catch (e: IOException) {
                e.printStackTrace()
                return@withContext null
            } catch (e: InterruptedException) {
                e.printStackTrace()
                return@withContext null
            } catch (e: ExecutionException) {
                e.printStackTrace()
                return@withContext null
            } catch (e: OAuthException) {
                e.printStackTrace()
                return@withContext null
            }
        }
    }

    suspend fun getAccessToken(pin: String, requestToken: OAuth1RequestToken): Map<String, String> {
        val result: LinkedHashMap<String, String> = LinkedHashMap<String, String>()
        return withContext(Dispatchers.IO) {
            try {
                result["success"] = "true"
                val accessToken: OAuth1AccessToken = OAuthSingleton.service.getAccessToken(requestToken, pin)
                OAuthSingleton.oAuth1AccessToken = accessToken
                return@withContext result
            } catch (e: IOException) {
                result["fail"] = e.message.toString()
                println("Network error while getting access token: ${e.message}")
                e.printStackTrace()
                return@withContext result
            } catch (e: InterruptedException) {
                result["fail"] = e.message.toString()
                println("Request was interrupted: ${e.message}")
                e.printStackTrace()
                return@withContext result
            } catch (e: ExecutionException) {
                result["fail"] = e.message.toString()
                println("Execution error while getting access token: ${e.message}")
                e.printStackTrace()
                return@withContext result
            } catch (e: OAuthException) {
                result["fail"] = e.message.toString()
                println("OAuthError")
                e.printStackTrace()
                return@withContext result
            }
        }
    }
}