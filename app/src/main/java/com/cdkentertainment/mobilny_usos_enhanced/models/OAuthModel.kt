package com.cdkentertainment.mobilny_usos_enhanced.models

import android.content.Context
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.UIHelper
import com.cdkentertainment.mobilny_usos_enhanced.UserDataSingleton
import com.cdkentertainment.mobilny_usos_enhanced.view_models.LoginPageViewModel
import com.github.scribejava.core.model.OAuth1AccessToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class OAuthModel {
    private val tokenAndUrl: String = "Auth/Url"
    private val finalTokensUrl: String = "Auth/AccessToken"
    private val parser: Json = Json { ignoreUnknownKeys = true }
    private val userInfoUrl: String = "users/user?fields=id|first_name|last_name|student_number|sex|email|photo_urls[100x100]|mobile_numbers|student_programmes[id|programme[all_faculties|id|name]]"

    @Serializable
    data class UrlAndToken(
        val url: String,
        val token: String,
        val tokenSecret: String
    )
    @Serializable
    data class TokenPair(
        val token: String,
        val tokenSecret: String
    )
    @Serializable
    data class TokenSet(
        val usosToken: String,
        val usosTokenSecret: String,
        val backendToken: String
    )
    public suspend fun checkIfAccessTokenExists(context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            if (BackendDataSender.oAuth1AccessToken != null) {
                return@withContext !checkIfTokenExpired()
            }
            val accessToken: OAuth1AccessToken? = UserDataSingleton.readAccessToken(context)
            if (accessToken != null) {
                BackendDataSender.oAuth1AccessToken = accessToken
                return@withContext !checkIfTokenExpired()
            }
            return@withContext false
        }
    }
    public suspend fun checkIfTokenExpired(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                UserDataSingleton.userData = getUserData()
                // FIXME FIXME FIXME FIXME
                val token = BackendDataSender.get("tests?userId=${UserDataSingleton.userData!!.id}").body
                BackendDataSender.setAuthHeader(token)
                // FIXME FIXME FIXME FIXME
                return@withContext UserDataSingleton.userData == null
            } catch (e: Exception) {
                return@withContext true
            }
        }
    }
    public suspend fun getUserData(): UserInfo {
        return withContext(Dispatchers.IO) {
            val responseString: String = OAuthSingleton.get(userInfoUrl)["response"]!!
            val parsedResponse: UserInfo = parser.decodeFromString<UserInfo>(responseString)
            return@withContext parsedResponse
        }
    }
    public suspend fun tryUsosAutoLogin(context: Context): LoginPageViewModel.LoginState {
        return if (checkIfAccessTokenExists(context)) {
            LoginPageViewModel.LoginState.LAST_STEPS
        } else {
            LoginPageViewModel.LoginState.USOS_LOGIN
        }
    }
    public suspend fun getRequestToken(): UrlAndToken {
        return withContext(Dispatchers.IO) {
            val response: BackendDataSender.BackendResponse = BackendDataSender.get(tokenAndUrl)
            if (response.statusCode == 200) {
                val parsedResponse: UrlAndToken = parser.decodeFromString<UrlAndToken>(response.body)
                return@withContext parsedResponse
            } else {
                throw(Exception("API Login Error"))
            }
        }
    }
    public suspend fun getAccessToken(pin: String, requestToken: String, tokenSecret: String, context: Context): OAuth1AccessToken {
        return withContext(Dispatchers.IO) {
            val result: BackendDataSender.BackendResponse = BackendDataSender.getWithAuthHeaders(finalTokensUrl, pin, requestToken, tokenSecret)
            if (result.statusCode == 200) {
                val parsedLoginData: TokenSet = parser.decodeFromString<TokenSet>(result.body)
                val oAuthToken = OAuth1AccessToken(parsedLoginData.usosToken, parsedLoginData.usosTokenSecret)
                BackendDataSender.oAuth1AccessToken = oAuthToken
                BackendDataSender.setAuthHeader(parsedLoginData.backendToken)
                UserDataSingleton.saveUserCredentials(context, BackendDataSender.oAuth1AccessToken!!)
                UserDataSingleton.userData = getUserData()
                return@withContext oAuthToken
            } else {
                throw (Exception("API Login Error"))
            }
        }
    }
    public suspend fun loadNecessaryData() {
        withContext(Dispatchers.IO) {
            val termsResponse: BackendDataSender.BackendResponse = BackendDataSender.get("Grades/TermIds")
            if (UIHelper.termIds.isEmpty()) {
                if (termsResponse.statusCode == 200) {
                    val responseString: String = termsResponse.body
                    val parsedResponse: List<String> = parser.decodeFromString<List<String>>(responseString)
                    UIHelper.termIds = parsedResponse
                } else {
                    throw Exception("API Error")
                }
            }
            if (UIHelper.classTypeIds.isEmpty()) {
                val classTypesResponse: BackendDataSender.BackendResponse = BackendDataSender.get("Grades/ClassTypeIds")
                if (classTypesResponse.statusCode == 200) {
                    val responseString: String = classTypesResponse.body
                    val parsedResponse: Map<String, SharedDataClasses.IdAndName> = parser.decodeFromString<Map<String, SharedDataClasses.IdAndName>>(responseString)
                    UIHelper.classTypeIds = parsedResponse
                } else {
                    throw(Exception("API Error"))
                }
            }
        }
    }
}

@Serializable
data class UserInfo(
    val id: String,
    val first_name: String,
    val last_name: String,
    val student_number: String,
    val sex: String,
    val email: String,
    val photo_urls: Map<String, String>,
    val mobile_numbers: List<String>,
    val student_programmes: List<StudentProgramme>
)

@Serializable
data class StudentProgramme(
    val id: String,
    val programme: ProgrammeData,
)

@Serializable
data class ProgrammeData(
    val id: String,
    val name: SharedDataClasses.LangDict,
    val all_faculties: List<SharedDataClasses.IdAndName>
)