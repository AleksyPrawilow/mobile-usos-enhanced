package com.cdkentertainment.mobilny_usos_enhanced.models

import android.content.Context
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
    private val userInfoUrl: String = "Users/User"

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
            if (UserDataSingleton.selectedUniversity == 0) {
                return@withContext false
            }
            if (BackendDataSender.oAuth1AccessToken != null) {
                return@withContext !checkIfTokenExpired()
            }
            return@withContext false
        }
    }
    public suspend fun checkIfTokenExpired(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val token = BackendDataSender.getWithOnlyAuthHeaders("Auth/AutoLogin?universityId=${UserDataSingleton.selectedUniversity}").body
                BackendDataSender.setAuthHeader(token!!)
                UserDataSingleton.userData = getUserData()
                return@withContext UserDataSingleton.userData == null
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext true
            }
        }
    }
    public suspend fun getUserData(): UserInfo {
        return withContext(Dispatchers.IO) {
            val responseString: String = BackendDataSender.get(userInfoUrl).body!!
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
            val response: BackendDataSender.BackendResponse = BackendDataSender.getWithoutHeaders("$tokenAndUrl?universityId=${UserDataSingleton.selectedUniversity}", false)
            if (response.statusCode == 200 && response.body != null) {
                val parsedResponse: UrlAndToken = parser.decodeFromString<UrlAndToken>(response.body!!)
                return@withContext parsedResponse
            } else {
                throw(Exception("API Login Error"))
            }
        }
    }
    public suspend fun getAccessToken(pin: String, requestToken: String, tokenSecret: String, context: Context): OAuth1AccessToken {
        return withContext(Dispatchers.IO) {
            val result: BackendDataSender.BackendResponse = BackendDataSender.getWithAuthHeaders("$finalTokensUrl?universityId=${UserDataSingleton.selectedUniversity}", pin, requestToken, tokenSecret)
            if (result.statusCode == 200  && result.body != null) {
                val parsedLoginData: TokenSet = parser.decodeFromString<TokenSet>(result.body!!)
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
            UserDataSingleton.userFaculties = UserDataSingleton.getUserFaculties(UserDataSingleton.userData!!)
            val termsResponse: BackendDataSender.BackendResponse = BackendDataSender.get("Grades/TermIds")
            if (UIHelper.termIds.isEmpty()) {
                if (termsResponse.statusCode == 200 && termsResponse.body != null) {
                    val responseString: String = termsResponse.body!!
                    val parsedResponse: List<SharedDataClasses.IdAndName> = parser.decodeFromString<List<SharedDataClasses.IdAndName>>(responseString)
                    UIHelper.termIds = parsedResponse
                } else {
                    throw Exception("API Error")
                }
            }
            if (UIHelper.classTypeIds.isEmpty()) {
                val classTypesResponse: BackendDataSender.BackendResponse = BackendDataSender.get("Grades/ClassTypeIds")
                if (classTypesResponse.statusCode == 200  && classTypesResponse.body != null) {
                    val responseString: String = classTypesResponse.body!!
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
    val student_status: Int?,
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