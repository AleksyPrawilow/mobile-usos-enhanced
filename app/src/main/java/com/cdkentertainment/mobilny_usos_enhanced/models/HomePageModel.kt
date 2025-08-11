package com.cdkentertainment.mobilny_usos_enhanced.models

import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class HomePageModel {
    suspend fun fetchUserData(): UserInfoClass? {
        return withContext(Dispatchers.IO) {
            val parser: Json = Json { ignoreUnknownKeys = true }

            var basicUserData: BasicUserData
            var studentProgramData: List<StudentProgramData>

            var response: Map<String, String> = OAuthSingleton.get("users/user?fields=first_name|last_name|sex|email|photo_urls[100x100]|mobile_numbers")
            if (response.containsKey("response") && response["response"] != null) {
                val responseString: String = response["response"]!!
                basicUserData = parser.decodeFromString<BasicUserData>(responseString)
            } else {
                return@withContext null
            }
            response = OAuthSingleton.get("progs/student?fields=id|programme")
            if (response.containsKey("response") && response["response"] != null) {
                val responseString: String = response["response"]!!
                studentProgramData = parser.decodeFromString<List<StudentProgramData>>(responseString)
            } else {
                return@withContext null
            }
            return@withContext UserInfoClass(basicUserData, studentProgramData)
        }
    }
}

@Serializable
data class UserInfoClass(
    val basicInfo: BasicUserData,
    val programme: List<StudentProgramData>
)

@Serializable
data class BasicUserData(
    val first_name: String,
    val last_name: String,
    val sex: String,
    val email: String,
    val photo_urls: Map<String, String>,
    val mobile_numbers: List<String>
)

@Serializable
data class StudentProgramData(
    val id: String,
    val programme: ProgramData
)

@Serializable
data class ProgramData(
    val id: String,
    val description: Map<String, String>
)