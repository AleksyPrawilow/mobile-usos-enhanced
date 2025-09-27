package com.cdkentertainment.mobilny_usos_enhanced.models

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class LecturerRatesPageModel {
    private val userRateUrl: String = "LecturerRates/UserRates"
    private val lecturerRateUrl: String = "LecturerRates/LecturerRates"
    private val addUserRateUrl: String = "LecturerRates/AddRate"
    private val parser: Json = Json {ignoreUnknownKeys = true}

    public suspend fun getUserRates(userId: Int): List<UserRate> {
        return withContext(Dispatchers.IO) {
            val response: BackendDataSender.BackendResponse = BackendDataSender.get("$userRateUrl?userId=$userId&universityId=1")
            if (response.statusCode == 200) {
                val parsedResponse: List<UserRate> = parser.decodeFromString<List<UserRate>>(response.body)
                return@withContext parsedResponse
            } else {
                throw(Exception("API Error"))
            }
        }
    }

    public suspend fun addUserRate(userRate: UserRate): String {
        return withContext(Dispatchers.IO) {
            val userRateJson = parser.encodeToString(userRate)
            val response = BackendDataSender.post(addUserRateUrl, userRateJson)
            if (response.statusCode == 200) {
                return@withContext "ok"
            } else {
                throw(Exception("API Error: ${response.statusCode}"))
            }
        }
    }
}

@Serializable
data class UserRate(
    val userId: Int,
    val lecturerId: Int,
    val universityId: Int,
    val rate1: Int,
    val rate2: Int,
    val rate3: Int,
    val rate4: Int,
    val rate5: Int,
)

@Serializable
data class LecturerRate(
    val rate_1: Float = 0f,
    val rate_2: Float = 0f,
    val rate_3: Float = 0f,
    val rate_4: Float = 0f,
    val rate_5: Float = 0f
)