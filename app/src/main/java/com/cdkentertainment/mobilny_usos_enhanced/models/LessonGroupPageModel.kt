package com.cdkentertainment.mobilny_usos_enhanced.models

import com.cdkentertainment.mobilny_usos_enhanced.OAuthSingleton
import com.cdkentertainment.mobilny_usos_enhanced.StudentData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class LessonGroupPageModel {
    private val parser = Json {ignoreUnknownKeys = true}
    private val requestUrl = "LessonGroups/Groups"
    private val participantUrl = "LessonGroups/Participants"
    public fun mergeGroupsBySubjects(seasonGroups: List<LessonGroup>): Map<String, List<LessonGroup>> {
        val groupedBySubjects = seasonGroups.groupBy { group ->
            group.course_id
        }
        return groupedBySubjects
    }
    public suspend fun getLessonGroups(): SeasonGroupsGroupedBySubject {
        return withContext(Dispatchers.IO) {
            val response: BackendDataSender.BackendResponse = BackendDataSender.get(requestUrl)
            if (response.statusCode == 200) {
                val parsedLessonGroups: SeasonGroupsGroupedBySubject = parser.decodeFromString<SeasonGroupsGroupedBySubject>(response.body)
                return@withContext parsedLessonGroups
            } else {
                throw(Exception("API Error"))
            }
        }
    }
    public suspend fun getParticipantOfGivenGroup(groupNumber: String, courseUnitId: String): Participants {
       return withContext(Dispatchers.IO) {
           val apiRequest: String = "$participantUrl?courseUnitId=$courseUnitId&groupNumber=$groupNumber"
           val response: BackendDataSender.BackendResponse = BackendDataSender.get(apiRequest)
           if (response.statusCode == 200) {
               val parsedLessonGroups: Participants = parser.decodeFromString<Participants>(response.body)
               return@withContext parsedLessonGroups
           } else {
               throw (Exception("API Error"))
           }
       }
    }
    public suspend fun fetchUserInfo(human: SharedDataClasses.Human): StudentData {
        return withContext(Dispatchers.IO) {
            try {
                val response: String = OAuthSingleton.get("users/user?user_id=${human.id}&fields=id|first_name|last_name|sex|email|has_photo|photo_urls[100x100]|student_status|has_email|mobile_numbers")["response"]!!
                val parsedStudentData: StudentData = parser.decodeFromString<StudentData>(response)
                return@withContext parsedStudentData
            } catch (e: Exception) {
                throw (Exception("API Error"))
            }
        }
    }
}
@Serializable
data class SeasonGroupsGroupedBySubject (
    val groups: MutableMap<String, Map<String, List<LessonGroup>>>
)
@Serializable
data class LessonGroup (
    val course_id: String,
    val course_unit_id: Int,
    val group_number: Int,
    val course_name: SharedDataClasses.LangDict,
    val lecturers: List<SharedDataClasses.Human>,
    val class_type_id: String
)
@Serializable
data class Participants(
    val participants: List<SharedDataClasses.Human>
)