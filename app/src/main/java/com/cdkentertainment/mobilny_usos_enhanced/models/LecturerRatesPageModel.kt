package com.cdkentertainment.mobilny_usos_enhanced.models

import com.cdkentertainment.mobilny_usos_enhanced.Lecturer
import com.cdkentertainment.mobilny_usos_enhanced.LecturerData
import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.PeopleSingleton
import com.cdkentertainment.mobilny_usos_enhanced.PeopleSingleton.lecturers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class LecturerRatesPageModel {
    private val staffUrl: String = "Users/Staff"
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
    public suspend fun getLecturerAvgRates(lecturerId: Int) : LecturerAvgRates? {
        return withContext(Dispatchers.IO) {
            val response: BackendDataSender.BackendResponse = BackendDataSender.get("$lecturerRateUrl?lecturerId=$lecturerId&universityId=1") //TEMPORARY
            if (response.statusCode == 200 && response.body != "") {
                val parsedResponse: LecturerAvgRates = parser.decodeFromString<LecturerAvgRates>(response.body)
                return@withContext parsedResponse
            } else if (response.body == "") {
                return@withContext null
            } else {
                throw(Exception("API Error"))
            }
        }
    }
    public suspend fun getStaffIndex(facultyId: String, offset: Int, pageSize: Int): LecturersIndex {
        return withContext(Dispatchers.IO) {
            val start = offset * pageSize
            val response: BackendDataSender.BackendResponse = BackendDataSender.get("$staffUrl?facultyId=$facultyId&start=$start&pageSize=$pageSize")
            if(response.statusCode == 200 && response.body != "") {
                val parsedResponse: LecturersIndex = parser.decodeFromString<LecturersIndex>(response.body)
                return@withContext parsedResponse
            } else {
                throw(Exception("API Error"))
            }
        }
    }
    public suspend fun getExtendedLecturersInfo(lecturerIds: List<String>) {
        return withContext(Dispatchers.IO) {
            try {
                val linkedString: String = lecturerIds.joinToString("|")
                val response: String = OAuthSingleton.get("users/users?user_ids=$linkedString&fields=id|first_name|last_name|titles|staff_status|phone_numbers|office_hours|has_photo|photo_urls[100x100]|room")["response"]!!
                val lecturersInfo: Map<String, LecturerData> = parser.decodeFromString<Map<String, LecturerData>>(response)
                for ((id, lecturer) in lecturersInfo) {
                    val lecturerRating: LecturerAvgRates? = getLecturerAvgRates(id.toInt())
                    if (lecturerRating == null) {
                        lecturers[lecturer.id] = Lecturer(
                            human = SharedDataClasses.Human(lecturer.id, lecturer.first_name, lecturer.last_name),
                            lecturerData = lecturer,
                            rating = LecturerAvgRates(
                                lecturerId = id.toInt(),
                                universityId = 1,
                                ratesCount = 0,
                                avgRate1 = 0f,
                                avgRate2 = 0f,
                                avgRate3 = 0f,
                                avgRate4 = 0f,
                                avgRate5 = 0f
                            )
                        )
                    } else {
                        lecturers[lecturer.id] = Lecturer(
                            human = SharedDataClasses.Human(lecturer.id, lecturer.first_name, lecturer.last_name),
                            lecturerData = lecturer,
                            rating = lecturerRating
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw(Exception("API Error"))
            }
        }
    }
    public suspend fun getLecturersInfo(lecturers: List<SharedDataClasses.Human>) {
        return withContext(Dispatchers.IO) {
            try {
                for (lecturer in lecturers) {
                    val lecturerRating: LecturerAvgRates? = getLecturerAvgRates(lecturer.id.toInt())
                    if (lecturerRating == null) {
                        PeopleSingleton.lecturers[lecturer.id] = Lecturer(
                            human = SharedDataClasses.Human(lecturer.id, lecturer.first_name, lecturer.last_name),
                            rating = LecturerAvgRates(
                                lecturerId = lecturer.id.toInt(),
                                universityId = 1,
                                ratesCount = 0,
                                avgRate1 = 0f,
                                avgRate2 = 0f,
                                avgRate3 = 0f,
                                avgRate4 = 0f,
                                avgRate5 = 0f
                            )
                        )
                    } else {
                        PeopleSingleton.lecturers[lecturer.id] = Lecturer(
                            human = SharedDataClasses.Human(lecturer.id, lecturer.first_name, lecturer.last_name),
                            rating = lecturerRating
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw(Exception("API Error"))
            }
        }
    }
    public suspend fun queryLecturersSearch(query: String): List<SharedDataClasses.Human> {
        return withContext(Dispatchers.IO) {
            val response: String = OAuthSingleton.get("users/search2?query=$query&lang=pl&fields=items[user[id|first_name|last_name]]&among=current_teachers&num=10")["response"]!!
            val parsedResponse: SearchResult = parser.decodeFromString<SearchResult>(response)
            return@withContext parsedResponse.items.map { it.user }
        }
    }
}
@Serializable
data class LecturerAvgRates(
    val lecturerId: Int,
    val universityId: Int,
    val ratesCount: Int,
    val avgRate1: Float,
    val avgRate2: Float,
    val avgRate3: Float,
    val avgRate4: Float,
    val avgRate5: Float,
)

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

@Serializable
data class LecturersIndex(
    val users: List<SharedDataClasses.Human>,
    val total: Int,
    val next_page: Boolean
)

@Serializable
private data class SearchResult(
    val items: List<UserWrapper>
)

@Serializable
private data class UserWrapper(
    val user: SharedDataClasses.Human
)